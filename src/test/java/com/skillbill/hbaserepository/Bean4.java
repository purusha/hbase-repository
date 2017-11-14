package com.skillbill.hbaserepository;

import java.util.UUID;

import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@HTable(name = "B4")
@AllArgsConstructor
@NoArgsConstructor 
public class Bean4 {
	
	@HKey
	private String recipient;

	@HColumn(name = "integerField", family = "U")
	private Integer integerField;

	@HColumn(name = "longField", family = "U")
	private Long longField;

    @HColumn(name = "stringField", family = "X")
    private String stringField;

    @HColumn(name = "uuidField", family = "Z")
    private UUID uuidField;
	
}
