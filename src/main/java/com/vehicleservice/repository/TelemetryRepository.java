package com.vehicleservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vehicleservice.entity.Driver;
import com.vehicleservice.entity.Telemetry;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Integer>{
	
	List<Telemetry> findAllByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

	Page<Telemetry> findAllByTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

	List<Telemetry> findByParameterAndTimeBetween(String parameter, LocalDateTime startTime, LocalDateTime endTime);

	Page<Telemetry> findByParameterAndTimeBetween(String parameter, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

//	List<Telemetry> findByDriverIdAndParameterAndTimeBetween(@Param("driver_id") Integer driverId, String parameter, LocalDateTime startTime, LocalDateTime endTime);
//
//	List<Telemetry> findByDriverIdAndTimeBetween(@Param("driver_id") Integer driverId, LocalDateTime startTime, LocalDateTime endTime);

	List<Telemetry> findByDriverAndParameterAndTimeBetween(@Param("driver_id") Driver driver, String parameter, LocalDateTime startTime, LocalDateTime endTime);
	
	List<Telemetry> findByDriverAndTimeBetween(@Param("driver_id") Driver driver, LocalDateTime startTime, LocalDateTime endTime);
	
}
