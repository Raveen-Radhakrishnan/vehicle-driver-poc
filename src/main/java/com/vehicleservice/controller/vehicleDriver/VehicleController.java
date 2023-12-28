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

import com.vehicleservice.entity.vehicleDriver.Vehicle;
import com.vehicleservice.exception.ErrorCode;
import com.vehicleservice.exception.GeneralizedException;
import com.vehicleservice.exception.VehicleNotFoundException;
import com.vehicleservice.request.VehicleMassUpdateRequest;
import com.vehicleservice.request.VehicleRequest;
import com.vehicleservice.response.VehicleResponse;
import com.vehicleservice.service.vehicleDriver.VehicleService;

import jakarta.validation.Valid;

@RestController
public class VehicleController {
	
	@Autowired
	VehicleService vehicleService;
	
	@PostMapping("/vehicles")
	public ResponseEntity<VehicleResponse> addVehicle(@RequestBody @Valid VehicleRequest vehicleRequest) {
		
		return new ResponseEntity<>(vehicleService.addVehicle(vehicleRequest), HttpStatus.CREATED);
		
	}
	
	@GetMapping("/vehicles")
	public ResponseEntity<Object> getAllVehicles(@RequestParam(name = "page", required = false) Integer page) {
		
		return new ResponseEntity<>(vehicleService.getAllVehicles(page), HttpStatus.OK);
//		return new ResponseEntity<>(vehicleService.getAllVehiclesSlice(page), HttpStatus.OK);
		
	}
	
	@GetMapping("/vehicles/{id}")
	public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable(name = "id") Integer vehicleId) {
		
		return new ResponseEntity<>(vehicleService.getVehicleById(vehicleId), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/vehicles/{id}")
	public ResponseEntity<Object> deleteVehicleById(@PathVariable(name = "id") Integer vehicleId) {
		
		if(vehicleService.deleteVehicleById(vehicleId)) {
			
			return new ResponseEntity<>("Vehicle with id " + vehicleId + " deleted successfully", HttpStatus.OK);
			
		}else {
			
			throw new VehicleNotFoundException("Vehicle with id " + vehicleId + " not found", ErrorCode.VEHICLE_NOT_FOUND);
		}
		
	}

	@PutMapping("/vehicles")
	public ResponseEntity<VehicleResponse> updateVehicle(@RequestBody @Valid VehicleRequest vehicleRequest) {
		
		if(vehicleRequest == null || vehicleRequest.getRegistrationNumber() == null ||
				vehicleRequest.getRegistrationNumber().isBlank()) {
			
			throw new GeneralizedException("Vehicle registration number cannot be null or empty",
					ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(vehicleService.updateVehicle(vehicleRequest), HttpStatus.OK);
		
	}

	@PutMapping("/vehicles/massUpdate")
	public ResponseEntity<List<VehicleResponse>> massUpdateVehicle(@RequestBody @Valid VehicleMassUpdateRequest vehicleMassUpdateRequest) {
		
		if(vehicleMassUpdateRequest == null || vehicleMassUpdateRequest.getVehicleIdList() == null ||
				vehicleMassUpdateRequest.getVehicleIdList().isEmpty()) {
			
			throw new GeneralizedException("Vehicle ID list cannot be null or empty",
					ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}

		if(vehicleMassUpdateRequest.getStatus() == null ||
				vehicleMassUpdateRequest.getStatus().isBlank()) {
			
			throw new GeneralizedException("Vehicle Status field cannot be null or empty",
					ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(vehicleService.massUpdateVehicle(vehicleMassUpdateRequest), HttpStatus.OK);
		
	}

	
	@GetMapping("vehicles/getSpeedLimit")
	public ResponseEntity<Object> getSpeedLimit() {
		
		return new ResponseEntity<>(vehicleService.getSpeedLimit(), HttpStatus.OK);
		
	}
	
	
}
