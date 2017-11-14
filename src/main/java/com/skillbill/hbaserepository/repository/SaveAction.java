package com.skillbill.hbaserepository.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SaveAction<T> extends BaseAction<T> {
    private final T item;

    SaveAction(Connection connection, HTable tableAnnotation, T item) {
        super(connection, tableAnnotation, item);
        this.item = item;
    }

    private HTableDescriptor createTableDesc(TableName requestTableName, Set<byte[]> families) {
        final HTableDescriptor tableDesc = new HTableDescriptor(requestTableName);
        families.forEach(family -> tableDesc.addFamily(new HColumnDescriptor(family)));

        return tableDesc;
    }

    private Table createIfNotExists(TableName tableName, Set<byte[]> families) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            final String namespace = tableName.getNamespaceAsString();

            if (StringUtils.isNoneBlank(namespace)) {
                try {
                    final NamespaceDescriptor[] namespaces = admin.listNamespaceDescriptors();

                    if (namespaces == null) {
                        admin.createNamespace(NamespaceDescriptor.create(namespace).build());
                    } else {
                        long count = Arrays
                            .stream(namespaces)
                            .filter(d -> StringUtils.equals(d.getName(),namespace))
                            .count();

                        if (count == 0) {
                            admin.createNamespace(NamespaceDescriptor.create(namespace).build());
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Namespace already exist: '{}'", namespace);
                }
            }

            if (!admin.tableExists(tableName)) {
                final HTableDescriptor requestTableDesc = createTableDesc(tableName, families);

                try {
                    admin.createTable(requestTableDesc);
                } catch (Exception e) {
                    LOGGER.error("Table already exist: '{}'", requestTableDesc.getNameAsString());
                }
            }
        }

        return connection.getTable(tableName);
    }

    boolean save() {
        final TableName hTableName = buildTableName(item);
        
        final List<ColumnField> columnFields = buildColumnFields(item.getClass().getDeclaredFields(), item);
        if (columnFields.isEmpty()) {
            throw new RuntimeException("HColumn annotation is not present in class " + item.getClass());
        }

        final Optional<Object> key = resolveHKeyFieldValue(item);
        if (!key.isPresent()) {
            throw new RuntimeException("HKey annotation is not present in class " + item.getClass());
        }

        final Set<byte[]> families = columnFields.stream()
            .map(cf -> new String(cf.getFamily()))
            .distinct() //remove duplicates!!!
            .map(s -> HBaseHelper.toB(s))
            .collect(Collectors.toSet());

        final Function<Table, Object> function = new Function<Table, Object>() {
            @Override
            public Object apply(Table table) {
                boolean result = false;
                try {
                    createIfNotExists(hTableName, families);
                    
                    final Put put = new Put(HBaseHelper.toB(key.get()));
                    columnFields.forEach(cf ->
                        put.addColumn(cf.getFamily(), cf.getName(), cf.getValue())
                    );
                    
                    table.put(put);
                    result = true;
                } catch (IOException e) {
                    LOGGER.error("", e);
                }
                return result;
            }
        };

        return (boolean) execute(hTableName, function);
    }
}
