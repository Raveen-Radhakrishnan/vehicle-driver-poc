package com.vehicleservice.entity.vehicleDriver;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "telemetry")
public class Telemetry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
//	@Column(name = "driver_id")
////	@NotEmpty(message = "driverId cannot be null or empty")
//	private Integer driverId;
//	
//	@Column(name = "vehicle_id")
////	@NotEmpty(message = "vehicleId cannot be null or empty")
//	private Integer vehicleId;
	
	@ManyToOne
	@JoinColumn(name = "driver_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Driver driver;

	@ManyToOne
	@JoinColumn(name = "vehicle_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Vehicle vehicle;
	
	private String parameter;
	
	@Column(name = "param_value")
	private Double paramValue;

	@JsonIgnore
	private LocalDateTime time;

	@Transient
	private String timestamp;
	
	@JsonIgnore
	public Integer getDriverId() {
		return this.driver.getId();
	}
	
	@JsonIgnore
	public Integer getVehicleId() {
		return this.vehicle.getId();
	}

	public Telemetry(Driver driver, Vehicle vehicle, String parameter, Double paramValue, LocalDateTime time) {
		this.driver = driver;
		this.vehicle = vehicle;
		this.parameter = parameter;
		this.paramValue = paramValue;
		this.time = time;
	}
	
	
}
