package com.skillbill.hbaserepository.repository.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.FIELD;

@Target({ FIELD })
@Retention(RUNTIME)
public @interface HColumn {

    String name() default "";

    String family();        

}