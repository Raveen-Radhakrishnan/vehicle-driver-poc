package com.vehicleservice.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
//public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
public class ExceptionControllerAdvice {
	
//	@ExceptionHandler(VehicleServiceException.class)
////	@ExceptionHandler(Exception.class)
//	public final ResponseEntity<ErrorDetails> handleAllException(VehicleServiceException ex, WebRequest request) throws Exception {
//		
//		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
//				ex.getMessage(), request.getDescription(false));
//		
//		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
////		return new ResponseEntity<ErrorDetails>(errorDetails, ex.getStatusCode());
//		
//	}

	@ExceptionHandler(VehicleNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleVehicleNotFoundException(VehicleNotFoundException ex) throws Exception {
		
//		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
//				ex.getMessage(), ErrorCode.VEHICLE_NOT_FOUND, ErrorCode.VEHICLE_NOT_FOUND.getValue());
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
				ex.getMessage(), ex.getErrorCode(), ex.getErrorCode().getValue());
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
		
	}

	@ExceptionHandler(DriverNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleDriverNotFoundException(DriverNotFoundException ex) throws Exception {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
				ex.getMessage(), ErrorCode.DRIVER_NOT_FOUND, ErrorCode.DRIVER_NOT_FOUND.getValue());
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
		
	}

	@ExceptionHandler(RuntimeException.class)
	public final ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException ex) throws Exception {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
				ex.getMessage(), ErrorCode.SOMETHING_WENT_WRONG, ErrorCode.SOMETHING_WENT_WRONG.getValue());
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException methodArgumentNotValidException) throws Exception {
		
//		ErrorDetails build = ErrorDetails.builder().errorCode((HttpStatus.BAD_REQUEST.value())).description(
//				(methodArgumentNotValidException.getBindingResult().getFieldErrors().get(0).getDefaultMessage()))
//				.localDateTime(LocalDateTime.now()).build();
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
				methodArgumentNotValidException.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
				ErrorCode.INVALID_REQUEST, ErrorCode.INVALID_REQUEST.getValue());
		
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(GeneralizedException.class)
	public final ResponseEntity<ErrorDetails> handleGeneralizedException(GeneralizedException ex) throws Exception {

		ErrorDetails errorDetails = new ErrorDetails(ex.getTimestamp(),
				ex.getOptionalDescription() != null ? ex.getErrorCode().getReasonPhrase() + ex.getOptionalDescription()
						: ex.getErrorCode().getReasonPhrase(),
				ex.getErrorCode(), ex.getErrorCode().getValue());
		
		return new ResponseEntity<ErrorDetails>(errorDetails, ex.getHttpStatus());
		
	}
	
}
