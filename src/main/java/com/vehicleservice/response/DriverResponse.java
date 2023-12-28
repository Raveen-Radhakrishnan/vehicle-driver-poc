package com.vehicleservice.response;

import com.vehicleservice.constants.DriverStatus;
import com.vehicleservice.entity.vehicleDriver.Driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

	private int id;

	private String name;

	private String address;

	private String licenseNumber;

	private String phoneNumber;

	private DriverStatus status;
	
	public DriverResponse(Driver driver) {
		
		this.id = driver.getId();
		this.name = driver.getName();
		this.address = driver.getAddress();
		this.licenseNumber = driver.getLicenseNumber();
		this.phoneNumber = driver.getPhoneNumber();
		this.status = driver.getStatus();
	}
}
