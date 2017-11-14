package com.skillbill.hbaserepository.repository;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;

import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class RemoveAction<T> extends BaseAction<T> {
    private final T item;

    RemoveAction(Connection connection, HTable tableAnnotation, T item) {
        super(connection, tableAnnotation, item);
        this.item = item;
    }

    boolean remove() {
        final TableName hTableName = buildTableName(item);

        final List<ColumnField> columnFields = buildColumnFields(item.getClass().getDeclaredFields(), item);
        if (columnFields.isEmpty()) {
            throw new RuntimeException("HColumn annotation is not present in class " + item.getClass());
        }

        return execute(hTableName, (table) -> {
            final Optional<Object> key = resolveHKeyFieldValue(item);
            if (!key.isPresent()) {
                throw new RuntimeException("HKey annotation is not present in class " + item.getClass());
            }

            boolean result = false;
            try {
                if(connection.getAdmin().tableExists(table.getName())) {
                    final Get get = new Get(HBaseHelper.toB(key.get()));

                    if(!table.get(get).isEmpty()) {
                        final Delete delete = new Delete(HBaseHelper.toB(key.get()));

                        table.delete(delete);
                        result = true;
                    }
                }
            } catch (IOException e) {
                LOGGER.error("", e);
            }

            return result;
        });
    }
}
