package com.vehicleservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.Fleet;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, Integer>{
	
	Optional<Fleet> findByRoute(@Param("route") String route);
	
}
