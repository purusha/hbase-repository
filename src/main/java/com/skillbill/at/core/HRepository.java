package com.skillbill.at.core;

import java.util.List;

public interface HRepository<T> {

	boolean save(T item);

	List<T> get(T... items); /* use only annotated key to retrieve data */

	T remove(T item);

	boolean update(T item);

}