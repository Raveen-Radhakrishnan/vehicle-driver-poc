package com.vehicleservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestPerformerResponse {
	
	private Double score;
	
	private DriverResponse driverDetails;
	
}
