package com.vehicleservice.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.datetime.standard.DateTimeFormatterFactory;

import com.vehicleservice.entity.vehicleDriver.Telemetry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelemetryResponse {
	
	private int id;
	
	private DriverResponse driver;
	
	private VehicleResponse vehicle;
	
	private String parameter;
	
	private Double paramValue;
	
	private String timestamp;
	
	public TelemetryResponse(Telemetry telemetry) {
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd HH:mm:ss").createDateTimeFormatter();
		
		this.id = telemetry.getId();
		this.driver = new DriverResponse(telemetry.getDriver());
		this.vehicle = new VehicleResponse(telemetry.getVehicle());
		this.parameter = telemetry.getParameter();
		this.paramValue = telemetry.getParamValue();
		this.timestamp = telemetry.getTime().format(dateTimeFormatter);
		
	}
	
}
