package com.vehicleservice.repository.vehicleDriver;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.vehicleDriver.Fleet;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, Integer>{
	
	Optional<Fleet> findByRoute(@Param("route") String route);
	
}
