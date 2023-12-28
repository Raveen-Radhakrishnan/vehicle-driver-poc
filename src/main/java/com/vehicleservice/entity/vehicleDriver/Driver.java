package com.vehicleservice.entity.vehicleDriver;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.vehicleservice.auditing.Auditable;
import com.vehicleservice.constants.DriverStatus;
import com.vehicleservice.request.DriverRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "driver")
public class Driver extends Auditable<String> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String address;
	
	@Column(name = "license_number")
	private String licenseNumber;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	private DriverStatus status;
	
//	@Column(name = "created_date", nullable = true, updatable = false)
//    @CreatedDate
//    private long createdDate;
//
//    @Column(name = "modified_date")
//    @LastModifiedDate
//    private long modifiedDate;
	
	public Driver(DriverRequest driverRequest) {
		
		this.name = driverRequest.getName();
		this.address = driverRequest.getAddress();
		this.licenseNumber = driverRequest.getLicenseNumber();
		this.phoneNumber = driverRequest.getPhoneNumber();
		this.status = driverRequest.getStatus() != null ? DriverStatus.of(driverRequest.getStatus()) : DriverStatus.DRIVING;
		
	}

//	public Driver(int id, String name, String address, String licenseNumber, String phoneNumber, DriverStatus status) {
//		this.id = id;
//		this.name = name;
//		this.address = address;
//		this.licenseNumber = licenseNumber;
//		this.phoneNumber = phoneNumber;
//		this.status = status;
//	}
	
	
}
