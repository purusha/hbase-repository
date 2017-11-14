package com.skillbill.hbaserepository.repository;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.hbase.client.Connection;

import com.skillbill.hbaserepository.repository.annotation.HTable;
import com.skillbill.hbaserepository.repository.annotation.ResolveConnection;

@ResolveConnection
public class SimpleRepository<T> implements HRepository<T> {

	private Connection connection;

    @Override
    public boolean save(T item) {
        return new SaveAction<>(connection, item.getClass().getAnnotation(HTable.class), item).save();
    }

	@Override
    public List<T> get(HbaseRowKey... keys) {
    	final List<HbaseRowKey> items = Arrays.asList(keys);
    	
		return new GetterAction(connection, items).get();
    }

    @Override
    public T remove(T item) {
    	return new RemoveAction<>(connection, item.getClass().getAnnotation(HTable.class), item).remove() ? item : null;
    }

    @Override
    public boolean update(T item) {
    	return new SaveAction<>(connection, item.getClass().getAnnotation(HTable.class), item).save();
    	
    	/*
    	 	if item contains just persisted key field ... this is really up update!!
    	 	for this reason is the same implementation of save(...)
    	 */
    }

}
