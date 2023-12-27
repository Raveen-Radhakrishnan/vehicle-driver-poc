package com.vehicleservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.Data;

@Data
public class VehicleNotFoundException extends RuntimeException {

//	public VehicleServiceException(String message, HttpStatus httpStatus) {
//		super(httpStatus, message);
//	}

	public VehicleNotFoundException(String message) {
		super(message);
		this.errorCode = ErrorCode.VEHICLE_NOT_FOUND;
	}
	
	private ErrorCode errorCode;
//	private String message;

	public VehicleNotFoundException(String message, ErrorCode errorCode) {
		super(message);
//		this.message = message;
		this.errorCode = errorCode;
	}
	
}
