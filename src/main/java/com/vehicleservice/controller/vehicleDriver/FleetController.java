package com.vehicleservice.controller.vehicleDriver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vehicleservice.entity.vehicleDriver.Fleet;
import com.vehicleservice.exception.ErrorCode;
import com.vehicleservice.exception.GeneralizedException;
import com.vehicleservice.service.vehicleDriver.FleetService;

import jakarta.validation.Valid;

@RestController
public class FleetController {
	
	@Autowired
	FleetService fleetService;
	
	@GetMapping("/fleets")
	public ResponseEntity<Object> getAllFleets() {
		
		return new ResponseEntity<>(fleetService.getAllFleets(), HttpStatus.OK);
		
	}

	@DeleteMapping("/fleets/{route}")
	public ResponseEntity<Object> deleteFleetByRoute(@PathVariable("route") String route) {
		
		if(fleetService.deleteFleetByRoute(route)) {
			
			return new ResponseEntity<>("Fleet with route " + route + " deleted successfully", HttpStatus.OK);
			
		}else {
			
			throw new GeneralizedException(ErrorCode.FLEET_NOT_FOUND.getReasonPhrase(), ErrorCode.FLEET_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		
	}

	@PostMapping("/fleets")
	public ResponseEntity<Fleet> saveFleet(@RequestBody @Valid Fleet fleet) {
	
		if(fleet == null || fleet.getRoute() == null ||
				fleet.getRoute().isBlank()) {
			
			throw new GeneralizedException("Route cannot be null or empty",
					ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Fleet>(fleetService.saveFleet(fleet), HttpStatus.CREATED);
	}
	
}
