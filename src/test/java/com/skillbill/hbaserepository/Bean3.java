package com.skillbill.hbaserepository;

import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@HTable(name = "B3")
@AllArgsConstructor
@NoArgsConstructor 
public class Bean3 {
	
	@HKey
	private String recipient;

	@HColumn(name = "reason", family = "U")
	private String reason;
	
}
