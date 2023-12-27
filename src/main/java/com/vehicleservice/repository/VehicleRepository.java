package com.vehicleservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{
	
	Optional<Vehicle> findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
	
	
	List<Vehicle> findByFleetId(@Param("fleet_id") Integer fleetId);
	
}
