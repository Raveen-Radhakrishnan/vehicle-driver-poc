package com.vehicleservice.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralizedException extends RuntimeException {

	public GeneralizedException(String message) {
		super(message);
		this.errorCode = ErrorCode.SOMETHING_WENT_WRONG;
	}
	
	private ErrorCode errorCode;
	private LocalDateTime timestamp;
	private HttpStatus httpStatus;
	private String optionalDescription;

	public GeneralizedException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
		super(message);
//		this.message = message;
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.timestamp = LocalDateTime.now();
	}

	public GeneralizedException(String message, ErrorCode errorCode, HttpStatus httpStatus, String optionalDescription) {
		super(message);
//		this.message = message;
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.timestamp = LocalDateTime.now();
		this.optionalDescription = optionalDescription;
	}

	public GeneralizedException(GeneralizedException generalizedException, String optionalDescription) {
		
		this(generalizedException.getMessage(), generalizedException.getErrorCode(),
				generalizedException.getHttpStatus(), optionalDescription);
		
	}
	
}
