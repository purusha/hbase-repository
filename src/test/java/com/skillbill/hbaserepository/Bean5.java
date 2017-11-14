package com.skillbill.hbaserepository;

import java.util.UUID;

import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@HTable(name = "B5")
@AllArgsConstructor
@NoArgsConstructor 
public class Bean5 {
	
	@HKey
	private UUID recipient;

	@HColumn(name = "integerField", family = "U")
	private Integer integerField;
	
}
