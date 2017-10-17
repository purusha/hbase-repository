package com.skillbill.at.core;

public @interface HColumn {

	String name() default "[unassigned]";

	String family();

}
