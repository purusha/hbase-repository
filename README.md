# hbase-repository

How to use in a real project (that use [guice](https://github.com/google/guice/wiki/GettingStarted) as DI framework)

* step1 (configure library)    
provide a binding of class com.skillbill.hbaserepository.HBaseConfig
  
* step2 (add guice module)    
add com.skillbill.hbaserepository.repository.HBaseRepositoryModule to app Modules
  
* step3 (add data bean model)  
create a bean class that expose you data (see examples in src/test/java)
  
* step4 (enjoy) 
use into the project like
```
      class MyService {
        @Inject
        HRepository<YourBean> repository
        ...
      }
```
