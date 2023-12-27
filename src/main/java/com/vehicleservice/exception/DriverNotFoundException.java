package com.vehicleservice.exception;

import lombok.Data;

@Data
public class DriverNotFoundException extends RuntimeException {

	private ErrorCode errorCode;
	
	public DriverNotFoundException(String message) {
		super(message);
		this.errorCode = ErrorCode.DRIVER_NOT_FOUND;
	}
	
	public DriverNotFoundException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}
