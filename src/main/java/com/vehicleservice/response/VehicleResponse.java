package com.vehicleservice.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vehicleservice.constants.VehicleStatus;
import com.vehicleservice.entity.vehicleDriver.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
	
	private int id;
	
	@JsonIgnoreProperties("vehicles")
	private FleetDetails fleet;
	
	private String registrationNumber;
	
	private String model;
	
	private String style;

	private VehicleStatus status;
	
	public VehicleResponse(Vehicle vehicle) {
		
		this.id = vehicle.getId();
		this.registrationNumber = vehicle.getRegistrationNumber();
		this.model = vehicle.getModel();
		this.style = vehicle.getStyle();
		this.fleet = new FleetDetails(vehicle.getFleet());
		this.status = vehicle.getStatus();
	}
	
}
