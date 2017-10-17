package com.skillbill.at.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Connection;

@ResolveConnection
public /* XXX final */ class SimpleRepository<T> implements HRepository<T> {

	private Connection connection;
	
	@Override
	public boolean save(T item) {
		return true;
	}
	
	@Override
	public List<T> get(T ... items) {
		final List<T> result = new ArrayList<T>();
		
		return result;
	}

	@Override
	public T remove(T item) {
		return null;
	}

	@Override
	public boolean update(T item) {
		return true;
	}

}
