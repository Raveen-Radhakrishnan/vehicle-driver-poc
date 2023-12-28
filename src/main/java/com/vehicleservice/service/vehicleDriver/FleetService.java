package com.vehicleservice.service.vehicleDriver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vehicleservice.entity.vehicleDriver.Fleet;
import com.vehicleservice.entity.vehicleDriver.Vehicle;
import com.vehicleservice.repository.vehicleDriver.FleetRepository;
import com.vehicleservice.repository.vehicleDriver.VehicleRepository;

import jakarta.validation.Valid;

@Service
public class FleetService {

	@Autowired
	FleetRepository fleetRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	public List<Fleet> getAllFleets() {

		return fleetRepository.findAll();

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteFleetByRoute(String route) {
		
		Optional<Fleet> fleet = fleetRepository.findByRoute(route);
		
		if(fleet.isPresent()) {
			
			updateFleetInVehicle(fleet.get());
			fleetRepository.delete(fleet.get());
			return true;
		}
		
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void updateFleetInVehicle(Fleet fleet) {
		
		if(fleet.getCount() != 0) {
			
			List<Vehicle> vehicleList = vehicleRepository.findByFleetId(fleet.getId());
			
			List<Vehicle> updatedVehicles = vehicleList.stream().map(vehicle -> new Vehicle(vehicle.getId(), null,
					vehicle.getRegistrationNumber(), vehicle.getModel(), vehicle.getStyle(), vehicle.getStatus()))
					.collect(Collectors.toList());
			
			vehicleRepository.saveAll(updatedVehicles);
		}
		
	}

	public Fleet saveFleet(@Valid Fleet fleet) {

		fleet.setRoute(fleet.getRoute().toUpperCase());
		return fleetRepository.save(fleet);

	}

}
