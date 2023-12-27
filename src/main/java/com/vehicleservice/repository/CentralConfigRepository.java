package com.vehicleservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.CentralConfigs;

@Repository
public interface CentralConfigRepository extends JpaRepository<CentralConfigs, String>{
	
	
}
