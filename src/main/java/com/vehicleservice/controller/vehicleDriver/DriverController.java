package com.vehicleservice.controller.vehicleDriver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vehicleservice.entity.vehicleDriver.Driver;
import com.vehicleservice.exception.DriverNotFoundException;
import com.vehicleservice.exception.ErrorCode;
import com.vehicleservice.exception.GeneralizedException;
import com.vehicleservice.exception.VehicleNotFoundException;
import com.vehicleservice.request.DriverMassUpdateRequest;
import com.vehicleservice.request.DriverRequest;
import com.vehicleservice.request.VehicleMassUpdateRequest;
import com.vehicleservice.response.DriverResponse;
import com.vehicleservice.response.VehicleResponse;
import com.vehicleservice.service.vehicleDriver.DriverService;

import jakarta.validation.Valid;

@RestController
public class DriverController {
	
	@Autowired
	DriverService driverService;
	
	@PostMapping("/drivers")
	public ResponseEntity<DriverResponse> addDriver(@RequestBody @Valid DriverRequest driverRequest) throws VehicleNotFoundException {
		
//		if(driverRequest == null || driverRequest.getName() == null)
//			throw new VehicleNotFoundException("Name cannot be null", HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(driverService.addDriver(driverRequest), HttpStatus.CREATED);
		
	}
	
	@GetMapping("/drivers")
	public ResponseEntity<Object> getAllDrivers(@RequestParam(name = "page", required = false) Integer page) {
		
		return new ResponseEntity<>(driverService.getAllDrivers(page), HttpStatus.OK);
		
	}

	@GetMapping("/drivers/{id}")
	public ResponseEntity<DriverResponse> getDriverById(@PathVariable("id") Integer driverId) {
		
		return new ResponseEntity<>(driverService.getDriverById(driverId), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/drivers/{id}")
	public ResponseEntity<Object> deleteDriverById(@PathVariable(name = "id") Integer driverId) {
		
		if(driverService.deleteDriverById(driverId)) {
			
			return new ResponseEntity<>("Driver with id " + driverId + " deleted successfully", HttpStatus.OK);
			
		}else {
			
			throw new DriverNotFoundException("Driver with id " + driverId + " not found", ErrorCode.DRIVER_NOT_FOUND);
		}
		
	}
	
	@PutMapping("/drivers")
	public ResponseEntity<DriverResponse> updateDriver(@RequestBody @Valid DriverRequest driverRequest) throws VehicleNotFoundException {
		
		if(driverRequest == null || driverRequest.getLicenseNumber() == null ||
				driverRequest.getLicenseNumber().isBlank()) {
			
			throw new GeneralizedException("Licence number cannot be null or empty",
					ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(driverService.updateDriver(driverRequest), HttpStatus.OK);
		
	}
	
	@PutMapping("/drivers/massUpdate")
	public ResponseEntity<List<DriverResponse>> massUpdateDriver(@RequestBody @Valid DriverMassUpdateRequest driverMassUpdateRequest) {
		
		if(driverMassUpdateRequest == null || driverMassUpdateRequest.getDriverIdList() == null ||
				driverMassUpdateRequest.getDriverIdList().isEmpty()) {
			
			throw new GeneralizedException("Driver ID list cannot be null or empty",
					ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}

		if(driverMassUpdateRequest.getStatus() == null ||
				driverMassUpdateRequest.getStatus().isBlank()) {
			
			throw new GeneralizedException("Driver Status field cannot be null or empty",
					ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(driverService.massUpdateDriver(driverMassUpdateRequest), HttpStatus.OK);
		
	}
	
}
