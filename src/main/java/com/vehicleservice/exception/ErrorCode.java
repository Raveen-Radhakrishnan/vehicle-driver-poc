package com.vehicleservice.exception;

public enum ErrorCode {
	
	SOMETHING_WENT_WRONG(1000, "Something went wrong"),
	VEHICLE_NOT_FOUND(1001, "Vehicle Not Found"),
	DRIVER_NOT_FOUND(1002, "Driver Not Found"),
	DUPLICATE_REGISTRATION_NUMBER(1003, "Vehicle with same registration number already exists"),
	DUPLICATE_LICENCE_NUMBER(1004, "Driver with same licence number already exists"),
	BEST_PERFORMER_NOT_AVAILABLE(1005, "Best Performer not available for this time range"),
	TELEMETRY_RECORDS_NOT_FOUND(1006, "Telemetry records not found for this time range"),
	FLEET_NOT_FOUND(1007, "Fleet with given route not found"),
	INVALID_REQUEST(1008, "Invalid or missing request parameter");
	
	ErrorCode(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}
	
	private final int value;
	
	private final String reasonPhrase;
	
	public int getValue() {
		return this.value;
	}
	
	public String getReasonPhrase() {
		return this.reasonPhrase;
	}
	
}
