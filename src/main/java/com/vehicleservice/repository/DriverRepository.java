package com.vehicleservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer>{
	
	Optional<Driver> findByLicenseNumber(@Param("licenseNumber") String licenseNumber);
	
}
