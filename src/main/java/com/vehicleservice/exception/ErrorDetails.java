package com.vehicleservice.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
	
	private String status = "FAILED";
	private LocalDateTime timestamp;
	private String description;
	private ErrorCode errorMessage;
	private Integer errorCode;
	
	public ErrorDetails(LocalDateTime timestamp, String description, ErrorCode errorMessage, Integer errorCode) {
		this.timestamp = timestamp;
		this.description = description;
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
	
	
	
}
