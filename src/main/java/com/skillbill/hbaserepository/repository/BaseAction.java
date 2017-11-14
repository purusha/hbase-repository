package com.skillbill.hbaserepository.repository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class BaseAction<T> {

    private final String tableName;
    protected Connection connection;

    BaseAction(Connection connection, HTable tableAnnotation, T item) {
        this(connection, tableAnnotation, item.getClass());
    }

    BaseAction(Connection connection, HTable tableAnnotation, Class<?> clazz) {
        this.connection = connection;
        
        if (tableAnnotation == null) {
            throw new RuntimeException("HTable annotation is not present in class " + clazz);
        }

        this.tableName = tableAnnotation.name();
    }

    TableName buildTableName(Object item) {
        final Long namespaceId;

        if (item instanceof HRepository.HbaseRowKey) {
            namespaceId = ((HRepository.HbaseRowKey)item).getNamespaceId();
        } else if(item instanceof NamespacedEntity) {
            namespaceId = ((NamespacedEntity) item).getNamespaceId();
        } else {
            namespaceId = null;
        }

        return (namespaceId != null) ?
            TableName.valueOf(String.valueOf(namespaceId), tableName) :
            TableName.valueOf(tableName);
    }

    Optional<Object> resolveHKeyFieldValue(T item) {
        return Arrays.stream(item.getClass().getDeclaredFields())
            .filter(f -> f.getAnnotation(HKey.class) != null)
            .findFirst()
            .map(f -> {
                try {
                    f.setAccessible(true);
                    return f.get(item);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    Class<?> getKeyClassOf(T item) {
        return Arrays.stream(item.getClass().getDeclaredFields())
            .filter(f -> f.getAnnotation(HKey.class) != null)
            .findFirst().get().getType();
    }

    List<ColumnField> buildColumnFields(Field[] fields, Object item) { //XXX item => @Nullable
        return
            Arrays.stream(fields)
                .filter(f -> f.getAnnotation(HColumn.class) != null)
                .map((Field f) -> {
                    try {
                        f.setAccessible(true);
                        final HColumn annColumn = f.getAnnotation(HColumn.class);

                        final byte[] name;
                        if (!StringUtils.isEmpty(annColumn.name())) { //annotation default is ""
                            name = HBaseHelper.toB(annColumn.name());
                        } else {
                            name = HBaseHelper.toB(f.getName()); //field name is the last choice!!!
                        }

                        final byte[] family = HBaseHelper.toB(annColumn.family());
                        byte[] value = HBaseHelper.toB(f, item);

                        return new ColumnField(name, family, value, f.getType());
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                    	LOGGER.error("Some problem building ColumnField's", e);
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    <T> T execute(TableName hTableName, Function<Table, T> fun) {
        try (final Table table = connection.getTable(hTableName)) {
            return fun.apply(table);
        } catch (Exception e) {
            LOGGER.error("Some problem communicating with hbase", e);

            try {
                connection.close();
            } catch (IOException e1) {
                LOGGER.error("Some problem close connection with hbase",  e1);
            }

            connection = null;
            throw new RuntimeException("Some problem communicating with hbase", e);
        }
    }
}
