package com.vehicleservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverScoreResponse {

	private Double score;

	private DriverResponse driverDetails;

}
