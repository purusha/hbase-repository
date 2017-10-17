package com.skillbill.at;

import java.util.Date;

import com.skillbill.at.core.HColumn;
import com.skillbill.at.core.HKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleBean {

	@HKey
	private String id;
	
	@HColumn(name = "name", family = "family1")
	private String name;
	
	@HColumn(family = "family2")
	private Date birthday;
	
}
