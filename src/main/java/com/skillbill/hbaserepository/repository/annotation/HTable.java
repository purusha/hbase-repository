package com.skillbill.hbaserepository.repository.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;

@Target({ TYPE })
@Retention(RUNTIME)
public @interface HTable {

    String name();

}