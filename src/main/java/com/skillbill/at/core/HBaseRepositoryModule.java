package com.skillbill.at.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.skillbill.at.SimpleBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class HBaseRepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		
		//XXX da fare per per ogni bean !!?
		//XXX come faccio a conosceli tutti ?
		bind(new TypeLiteral<HRepository<SimpleBean>>(){})
	      .to(new TypeLiteral<SimpleRepository<SimpleBean>>(){});		

		final MethodInterceptor interceptor = new ConnectionResolver();
	    requestInjection(interceptor);	    
	    
	    bindInterceptor(Matchers.annotatedWith(ResolveConnection.class), Matchers.any(), interceptor);		
		
	}
	
	private final class ConnectionResolver implements MethodInterceptor {

//		@Inject
//		private final XXX;		
		
		public ConnectionResolver() {
			
			/*
			 * build an instance of HConnection, and use it when is usefull
			 */
			
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			
			final Object instance = invocation.getThis();			
			final String methodCalled = invocation.getMethod().getName();
			log.info("called {} on {}", methodCalled, instance);
						
			Object connection = FieldUtils.readField(instance, "connection", true);
			if (connection == null) {
				log.info("connection is NULL, please build a valid instance!!!");
			}
			
			return invocation.proceed();
		}
		
	}

}
