package com.skillbill.hbaserepository.repository;

import java.util.List;

import lombok.Getter;

public interface HRepository<T> {

    boolean save(T item);

	List<T> get(HbaseRowKey... items); 

    T remove(T item);

    boolean update(T item);
    
    @Getter
    class HbaseRowKey { 
    	private final Long namespaceId;
		private final byte[] key;
        private final Class<?> beanClass;

        public HbaseRowKey(Long namespaceId, Object key, Class<?> persistentClass) {
            this.namespaceId = namespaceId;
            this.key = HBaseHelper.toB(key);
            this.beanClass = persistentClass;
        }
    }

}
