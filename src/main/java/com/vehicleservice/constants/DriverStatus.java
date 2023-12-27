package com.vehicleservice.constants;

import java.util.stream.Stream;

public enum DriverStatus {
	
	DRIVING("D"),
	REST("R"),
	NO_TRIP("N"),
	BREAK("B");

	private String driverStatusCode;
	
	private DriverStatus(String driverStatusCode) {
		
		this.driverStatusCode = driverStatusCode;
	}
	
	public String getStatusCode() {
		
		return this.driverStatusCode;
	}
	
	public static DriverStatus of(String driverStatusCode) {
        return Stream.of(DriverStatus.values())
          .filter(d -> d.getStatusCode().equalsIgnoreCase(driverStatusCode))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
	
}
