package com.vehicleservice.constants;

import java.util.stream.Stream;

public enum VehicleStatus {
	
	NEW("N"),
	WORK_IN_PROGRESS("I"),
	TESTING("T"),
	REJECT("R"),
	PRODUCTION("P");

	private String vehicleStatusCode;
	
	private VehicleStatus(String vehicleStatusCode) {
		
		this.vehicleStatusCode = vehicleStatusCode;
	}
	
	public String getStatusCode() {
		
		return this.vehicleStatusCode;
	}
	
	public static VehicleStatus of(String vehicleStatusCode) {
        return Stream.of(VehicleStatus.values())
          .filter(v -> v.getStatusCode().equalsIgnoreCase(vehicleStatusCode))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
	
}
