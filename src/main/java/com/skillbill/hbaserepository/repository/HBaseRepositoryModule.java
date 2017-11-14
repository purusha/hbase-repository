package com.skillbill.hbaserepository.repository;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.skillbill.hbaserepository.HBaseConfig;
import com.skillbill.hbaserepository.repository.annotation.ResolveConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HBaseRepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        final MethodInterceptor interceptor = new ConnectionResolver();
        requestInjection(interceptor);

        bindInterceptor(Matchers.annotatedWith(ResolveConnection.class), Matchers.any(), interceptor);
    }

    private final class ConnectionResolver implements MethodInterceptor {
		private static final String FIELD_NAME = "connection";

		@Inject
		private Injector injector;
		
		private Connection connection;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
        	if (connection == null) {
        		final HBaseConfig configuration = injector.getInstance(HBaseConfig.class);
        		
                try {
                    HBaseAdmin.checkHBaseAvailable(configuration.getConfiguration());
                    connection = ConnectionFactory.createConnection(configuration.getConfiguration());
                } catch (Exception e) {
                    LOGGER.error("Cannot create HBase connection", e);
                    throw new RuntimeException(e);
                }        		
        	}
        	
            final Object instance = invocation.getThis();
            final Object connectionField = FieldUtils.readField(instance, FIELD_NAME, true);
            
            if (connectionField == null) {
                FieldUtils.writeField(instance, FIELD_NAME, connection, true);
            }

            return invocation.proceed();
        }
    }
    
}
