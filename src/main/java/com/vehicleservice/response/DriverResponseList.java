package com.vehicleservice.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponseList {
	
	private List<DriverResponse> driverList;
	
	private Pagination pagination;
	
}
