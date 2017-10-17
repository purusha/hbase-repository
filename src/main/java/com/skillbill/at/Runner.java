package com.skillbill.at;

import java.util.Date;
import java.util.UUID;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.skillbill.at.core.HBaseRepositoryModule;
import com.skillbill.at.core.HRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Runner {

	public static void main(String[] args) {

		final Injector injector = Guice.createInjector(new HBaseRepositoryModule());
		log.info("{}", injector);
		
		final HRepository<SimpleBean> repository = injector.getInstance(new Key<HRepository<SimpleBean>>(){});
		log.info("{}", repository);
		
		final SimpleBean sb = new SimpleBean();
		sb.setId(UUID.randomUUID().toString());
		sb.setName("alan");
		sb.setBirthday(new Date());
		
		repository.save(sb);		

	}

}
