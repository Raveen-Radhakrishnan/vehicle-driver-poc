package com.vehicleservice.controller.vehicleDriver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vehicleservice.entity.vehicleDriver.Telemetry;
import com.vehicleservice.response.BestPerformerResponse;
import com.vehicleservice.response.DriverScoreResponse;
import com.vehicleservice.response.TelemetryResponseList;
import com.vehicleservice.service.vehicleDriver.TelemetryService;

import jakarta.validation.Valid;

@RestController
public class TelemetryController {
	
	@Autowired
	TelemetryService telemetryService;
	
	@PostMapping("/telemetries")
	public ResponseEntity<Telemetry> addTelemetryRecord(@RequestBody @Valid Telemetry telemetry) {
		
		return new ResponseEntity<>(telemetryService.addTelemetryRecord(telemetry), HttpStatus.CREATED);
		
	}
	
	@GetMapping("/telemetries/{startTime}/{endTime}")
	public ResponseEntity<Object> getTelemetryRecords(@PathVariable String startTime, @PathVariable String endTime, 
			@RequestParam(name = "page", required = false) Integer page){
		
		return new ResponseEntity<>(telemetryService.getTelemetryRecords(startTime, endTime, page), HttpStatus.OK);
//		return new ResponseEntity<>(telemetryService.getTelemetryRecordsOld(startTime, endTime), HttpStatus.OK);
		
	}

	@GetMapping("/telemetries/best-performer/{startTime}/{endTime}")
	public ResponseEntity<BestPerformerResponse> getBestPerformer(@PathVariable String startTime, @PathVariable String endTime){
		
		return new ResponseEntity<>(telemetryService.getBestPerformer(startTime, endTime), HttpStatus.OK);
		
	}

	@GetMapping("/telemetries/scores")
	public ResponseEntity<List<DriverScoreResponse>> getScoresForLastHour(){
		
		return new ResponseEntity<>(telemetryService.getScoresForLastHour(), HttpStatus.OK);
		
	}
	
//	@GetMapping("/telemetries/{parameter_type}/{startTime}/{endTime}")
//	public ResponseEntity<TelemetryResponseList> getTelemetryRecordsBasedOnParamType(
//			@PathVariable("parameter_type") String parameterType, @PathVariable String startTime,
//			@PathVariable String endTime, @RequestParam(name = "page", required = false) Integer page) {
//
//		return new ResponseEntity<>(
//				telemetryService.getTelemetryRecordsBasedOnParamType(parameterType, startTime, endTime, page), HttpStatus.OK);
//		
//	}

	@GetMapping("/telemetries/{parameter_type}/{startTime}/{endTime}")
	public ResponseEntity<List<Telemetry>> getTelemetryRecordsBasedOnParamType(
			@PathVariable("parameter_type") String parameterType, @PathVariable String startTime,
			@PathVariable String endTime, @RequestParam(name = "driverId", required = true) Integer driverId) {
		
		return new ResponseEntity<>(
				telemetryService.getTelemetryRecordsBasedOnParamTypeOld(driverId, parameterType, startTime, endTime), HttpStatus.OK);
		
	}

	@GetMapping("/telemetries/driver/{driverId}/{startTime}/{endTime}")
	public ResponseEntity<List<Telemetry>> getTelemetryRecordsBasedOnDriverId(
			@PathVariable(name = "driverId") Integer driverId, @PathVariable String startTime,
			@PathVariable String endTime) {
		
		return new ResponseEntity<>(
				telemetryService.getTelemetryRecordsBasedOnDriverId(driverId, startTime, endTime), HttpStatus.OK);
		
	}
	
}
