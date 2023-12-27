package com.vehicleservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
	
	private Integer totalPages;
	private Long totalNumberOfRecords;
	private Integer currentPage;
	
}
