package com.skillbill.hbaserepository.repository;

import java.net.URL;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Modules;
import com.skillbill.hbaserepository.HBaseConfig;
import com.skillbill.hbaserepository.Bean1;
import com.skillbill.hbaserepository.Bean2;
import com.skillbill.hbaserepository.Bean3;
import com.skillbill.hbaserepository.Bean4;
import com.skillbill.hbaserepository.Bean5;
import com.skillbill.hbaserepository.Bean6;
import com.skillbill.hbaserepository.Bean7;

public class BasicTest {
	
	public BasicTest() {
		final Injector injector;
		
		final URL resource = this.getClass().getClassLoader().getResource(".");		
		
		final AbstractModule single = new AbstractModule() {
			@Override
			protected void configure() {
				
				bind(HBaseConfig.class)
					.toProvider(new Provider<HBaseConfig>() {

						@Override
						public HBaseConfig get() {
							return new HBaseConfig("/tmp/hbase-developer/hbase", resource.getPath(), false, null);
						}
						
					});
								
	            bind(new TypeLiteral<HRepository<Bean1>>(){})
		            .to(new TypeLiteral<SimpleRepository<Bean1>>(){});
				
	            bind(new TypeLiteral<HRepository<Bean2>>(){})
	            	.to(new TypeLiteral<SimpleRepository<Bean2>>(){});

	            bind(new TypeLiteral<HRepository<Bean3>>(){})
            		.to(new TypeLiteral<SimpleRepository<Bean3>>(){});

                bind(new TypeLiteral<HRepository<Bean4>>(){})
	                .to(new TypeLiteral<SimpleRepository<Bean4>>(){});

                bind(new TypeLiteral<HRepository<Bean5>>(){})
                	.to(new TypeLiteral<SimpleRepository<Bean5>>(){});
                
				bind(new TypeLiteral<HRepository<Bean6>>(){})
					.to(new TypeLiteral<SimpleRepository<Bean6>>(){});

                bind(new TypeLiteral<HRepository<Bean7>>(){})
                    .to(new TypeLiteral<SimpleRepository<Bean7>>(){});
			}
		};
        
        if (getModule() != null) {
        	injector = Guice.createInjector(
    			Modules.override(single, new HBaseRepositoryModule()).with(getModule())
			);
        } else {
        	injector = Guice.createInjector(
    			single,
                new HBaseRepositoryModule()
			);
        }

        injector.injectMembers(this);
    }

    protected Module getModule() {
        return null;
    }
	
}
