package com.skillbill.hbaserepository;

import java.util.Map;

import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@HTable(name = "B6")
@AllArgsConstructor
@NoArgsConstructor 
public class Bean6 {

	@HKey
	private String recipient;

	@HColumn(name = "reason", family = "U")
	private Map<String, Integer> reason;
	
}
