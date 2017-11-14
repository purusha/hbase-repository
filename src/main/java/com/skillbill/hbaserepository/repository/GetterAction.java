package com.skillbill.hbaserepository.repository;

import java.util.List;

import org.apache.hadoop.hbase.client.Connection;

import com.skillbill.hbaserepository.repository.annotation.HTable;

class GetterAction<T> extends BaseAction<T> {
    private final List<HRepository.HbaseRowKey> items;

    GetterAction(Connection connection, List<HRepository.HbaseRowKey> items) {
        super(connection, items.get(0).getBeanClass().getAnnotation(HTable.class), items.get(0).getBeanClass());
        this.items = items;
    }

    List<T> get() {
    	return new GetterStatic<T>(connection, items).get();
    }
}