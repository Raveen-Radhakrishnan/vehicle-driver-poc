package com.vehicleservice.repository.vehicleDriver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.vehicleDriver.CentralConfigs;

@Repository
public interface CentralConfigRepository extends JpaRepository<CentralConfigs, String>{
	
	
}
