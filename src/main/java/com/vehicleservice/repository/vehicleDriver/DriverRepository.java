package com.vehicleservice.repository.vehicleDriver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.vehicleDriver.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer>{
	
	Optional<Driver> findByLicenseNumber(@Param("licenseNumber") String licenseNumber);
	
}
