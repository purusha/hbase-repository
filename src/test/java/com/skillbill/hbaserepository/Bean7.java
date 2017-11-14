package com.skillbill.hbaserepository;

import java.util.Date;

import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@HTable(name = "B7")
@AllArgsConstructor
@NoArgsConstructor 
public class Bean7 {

	@HKey
	private Date recipient;

	@HColumn(name = "reason", family = "U")
	private String reason;
	
}
