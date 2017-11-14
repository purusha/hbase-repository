package com.skillbill.hbaserepository.repository;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GetterStatic<T> extends BaseAction<T> {

    private final List<HRepository.HbaseRowKey> items;

    GetterStatic(Connection connection, List<HRepository.HbaseRowKey> items) {
        super(connection, items.get(0).getBeanClass().getAnnotation(HTable.class), items.get(0).getBeanClass());
        this.items = items;
    }

    List<T> get() {
        final Long namespaceId = items.get(0).getNamespaceId();
        final TableName hTableName = buildTableName(items.get(0));
        final List<ColumnField> columnFields = buildColumnFields(items.get(0).getBeanClass().getDeclaredFields(), null);

        if (columnFields.isEmpty()) {
            throw new RuntimeException("HColumn annotation is not present in class " + items.get(0).getBeanClass());
        }

        final Field hKeyField = Arrays.stream(items.get(0).getBeanClass().getDeclaredFields())
            .filter(f -> f.getAnnotation(HKey.class) != null)
            .findFirst().get();
        hKeyField.setAccessible(true);

        return execute(hTableName, (Table table) -> {
            try {
                final List<T> toReturn = new ArrayList<>();

                if(connection.getAdmin().tableExists(hTableName)) {
                    final List<Result> hbaseResults = Arrays.asList(table.get(
                        items.stream()
                            .map(rowKey -> {
                                final Get get = new Get(rowKey.getKey());
                                columnFields.forEach(cf -> {
                                    get.addColumn(cf.getFamily(), cf.getName());
                                });

                                return get;
                            })
                            .collect(Collectors.toList())
                    ));

                    LOGGER.debug("found {} results", hbaseResults.size());

                    for(int i = 0; i < hbaseResults.size(); i++) {
                        final Result r = hbaseResults.get(i);
                        final T instance = (T) items.get(0).getBeanClass().newInstance();

                        if(!r.isEmpty()) {
                            for (ColumnField cf : columnFields) {
                                FieldUtils.writeField(
                                    instance,
                                    new String(cf.getName()),
                                    HBaseHelper.toObject(
                                        cf.getType(), r.getValue(cf.getFamily(), cf.getName())
                                    ),
                                    true
                                );
                            }

                            if(instance instanceof NamespacedEntity) {
                                FieldUtils.writeField(instance, "namespaceId", namespaceId, true); //set namespaceId
                            }

                            final Object keyValue = HBaseHelper.toObject(getKeyClassOf(instance), r.getRow());
                            FieldUtils.writeField(hKeyField, instance, keyValue); //set HKey

                            toReturn.add(instance);
                        }
                    }
                }

                return toReturn;
            } catch (Exception e) {
                LOGGER.error("", e);
                return null;
            }
        });

    }
}
