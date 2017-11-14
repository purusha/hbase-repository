package com.skillbill.hbaserepository;

import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@HTable(name = "B2")
@AllArgsConstructor
@NoArgsConstructor 
public class Bean2 {
	
	@HKey
	private String recipient;

	@HColumn(family = "U")
	private String reason;
	
}
