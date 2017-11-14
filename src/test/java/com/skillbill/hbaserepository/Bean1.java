package com.skillbill.hbaserepository;

import com.skillbill.hbaserepository.repository.NamespacedEntity;
import com.skillbill.hbaserepository.repository.annotation.HColumn;
import com.skillbill.hbaserepository.repository.annotation.HKey;
import com.skillbill.hbaserepository.repository.annotation.HTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@HTable(name = "B1")
@AllArgsConstructor
@NoArgsConstructor 
public class Bean1 implements NamespacedEntity {

	private Long namespaceId;

	@HKey
	private String recipient;

	@HColumn(name = "reason", family = "U")
	private String reason;
	
}
