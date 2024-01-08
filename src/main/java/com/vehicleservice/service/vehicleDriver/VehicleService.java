package com.vehicleservice.service.vehicleDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicleservice.constants.VehicleEnum;
import com.vehicleservice.constants.VehicleStatus;
import com.vehicleservice.entity.vehicleDriver.Fleet;
import com.vehicleservice.entity.vehicleDriver.Vehicle;
import com.vehicleservice.exception.ErrorCode;
import com.vehicleservice.exception.GeneralizedException;
import com.vehicleservice.exception.VehicleNotFoundException;
import com.vehicleservice.helper.ExcelHelper;
import com.vehicleservice.repository.vehicleDriver.FleetRepository;
import com.vehicleservice.repository.vehicleDriver.VehicleRepository;
import com.vehicleservice.request.VehicleMassUpdateRequest;
import com.vehicleservice.request.VehicleRequest;
import com.vehicleservice.response.Pagination;
import com.vehicleservice.response.VehicleResponse;
import com.vehicleservice.response.VehicleResponseList;
import com.vehicleservice.response.VehicleResponseListSlice;

import jakarta.validation.Valid;

@Service
public class VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private FleetRepository fleetRepository;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	ExcelHelper excelHelper;

//	@Transactional
//	public VehicleResponse addVehicle(VehicleRequest vehicleRequest) {
//
//		Vehicle vehicle = transformVehicleRequest(vehicleRequest);
//		
//		Fleet fleet = vehicle.getFleet();
//		String route = fleet.getRoute();
//		
//		Fleet fleetByRoute = fleetRepository.findByRoute(route);
//		
//		if(fleetByRoute != null) {
//			
//			fleetByRoute.setCount(fleetByRoute.getCount() + 1);
//			Fleet updatedFleet = fleetRepository.saveAndFlush(fleetByRoute);
//			vehicle.setFleet(updatedFleet);
//		}else {
//			fleet.setCount(1);
//			Fleet updatedFleet = fleetRepository.save(fleet);
//			vehicle.setFleet(updatedFleet);
//			
//		}
//		
////		Vehicle vehicle = new Vehicle();
////		vehicle.setModel(vehicleRequest.getModel());
////		vehicle.setRegistrationNumber(vehicleRequest.getRegistrationNumber());
////		vehicle.setStyle(vehicleRequest.getStyle());
////		vehicle.setFleetId(new fleet);
//
//		Vehicle savedVehicle = vehicleRepository.save(vehicle);
//	
//		return new VehicleResponse(savedVehicle);
//
//	}

	@Transactional
	public VehicleResponse addVehicle(VehicleRequest vehicleRequest) {
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
		
		if(vehicleOptional.isPresent())
			throw new GeneralizedException(ErrorCode.DUPLICATE_REGISTRATION_NUMBER.getReasonPhrase(),
					ErrorCode.DUPLICATE_REGISTRATION_NUMBER, HttpStatus.BAD_REQUEST);
		
		Vehicle vehicle = transformVehicleRequest(vehicleRequest);
		
		Fleet fleet = vehicle.getFleet();
		String route = fleet.getRoute();
		
		Optional<Fleet> fleetByRoute = fleetRepository.findByRoute(route);
		
		if(fleetByRoute.isPresent()) {
			
			fleetByRoute.get().setCount(fleetByRoute.get().getCount() + 1);
			Fleet updatedFleet = fleetRepository.saveAndFlush(fleetByRoute.get());
			vehicle.setFleet(updatedFleet);
		}else {
			fleet.setCount(1);
			Fleet updatedFleet = fleetRepository.save(fleet);
			vehicle.setFleet(updatedFleet);
			
		}
		
		Vehicle savedVehicle = vehicleRepository.save(vehicle);
		
		return new VehicleResponse(savedVehicle);
		
	}
	
	private Vehicle transformVehicleRequest(VehicleRequest vehicleRequest) {
		
		Vehicle vehicle = new Vehicle();
		
		vehicle.setFleet(new Fleet(0, 0, vehicleRequest.getRoute().toUpperCase()));
		vehicle.setModel(vehicleRequest.getModel());
		vehicle.setRegistrationNumber(vehicleRequest.getRegistrationNumber());
		vehicle.setStyle(vehicleRequest.getStyle());
		vehicle.setStatus(vehicleRequest.getStatus() != null ? VehicleStatus.of(vehicleRequest.getStatus()) : VehicleStatus.NEW);
		
//		vehicle.setStatus("NEW");
		
		return vehicle;
		
	}

	public List<VehicleResponse> getAllVehiclesOld(Integer page) {
		
		List<Vehicle> vehicles = vehicleRepository.findAll();
		
		List<VehicleResponse> vehicleList = vehicles.stream().map(vehicle -> new VehicleResponse(vehicle))
				.collect(Collectors.toList());
		
		return vehicleList;
		
	}
	
	public VehicleResponseList getAllVehicles(Integer page) {
		
		Integer pageSize = Integer.parseInt(environment.getProperty(VehicleEnum.PAGE_SIZE, "5"));
		
		Pageable pageable = PageRequest.of(page == null ? 0 : page, pageSize);
		
		Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);

		VehicleResponseList vehicleResponseList = new VehicleResponseList();
		
		Pagination pagination = new Pagination(vehicles.getTotalPages(), vehicles.getTotalElements(),
				vehicles.getNumber());

		List<VehicleResponse> vehicleList = vehicles.getContent().stream().map(vehicle -> new VehicleResponse(vehicle))
				.collect(Collectors.toList());
		
		vehicleResponseList.setVehicleList(vehicleList);
		vehicleResponseList.setPagination(pagination);
		
		return vehicleResponseList;

	}
	
	public VehicleResponseListSlice getAllVehiclesSlice(Integer page) {
		
		Integer pageSize = Integer.parseInt(environment.getProperty(VehicleEnum.PAGE_SIZE, "5"));
		
		Pageable pageable = PageRequest.of(page == null ? 0 : page, pageSize);
		
		Slice<Vehicle> vehicles = vehicleRepository.findAll(pageable);
		
		VehicleResponseListSlice vehicleResponseListSlice = new VehicleResponseListSlice();
		
		List<VehicleResponse> vehicleList = vehicles.getContent().stream().map(vehicle -> new VehicleResponse(vehicle))
				.collect(Collectors.toList());
		
		vehicleResponseListSlice.setVehicleList(vehicleList);
		vehicleResponseListSlice.setHasNextPage(vehicles.hasNext());
		
		return vehicleResponseListSlice;
		
	}

	public VehicleResponse getVehicleById(int id) {
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
		
		Vehicle vehicle = vehicleOptional.orElseThrow(() -> new VehicleNotFoundException("Vehicle with id " + id + " not found",
				ErrorCode.VEHICLE_NOT_FOUND));
		
		return new VehicleResponse(vehicle);
		
//		return vehicle.orElse(new Vehicle());
		
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean deleteVehicleById(Integer vehicleId) {
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
		
		if(vehicleOptional.isPresent()) {
			
			Vehicle vehicle = vehicleOptional.get();
			if(vehicle.getFleet() != null) {
				
				Integer fleetId = vehicle.getFleet().getId();
				
				reduceFleetCount(fleetId);
				
			}
			vehicleRepository.deleteById(vehicleId);
			
			
			return true;
		}
		
		return false;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	private void reduceFleetCount(Integer fleetId) {
		
		Optional<Fleet> fleetOptional = fleetRepository.findById(fleetId);
		
		if(fleetOptional.isPresent()) {
			
			Fleet fleet = fleetOptional.get();
			
			fleet.setCount(fleet.getCount() - 1);
			
			fleetRepository.save(fleet);
			
		}
		
	}
	
	public Vehicle updateVehicles(VehicleRequest vehicleRequest, Optional<Vehicle> vehicleOptional) {
		
		if(!vehicleOptional.isPresent())
			throw new GeneralizedException(
					"Vehicle with registration number " + vehicleRequest.getRegistrationNumber() + " not found",
					ErrorCode.VEHICLE_NOT_FOUND, HttpStatus.BAD_REQUEST);
		
		vehicleRequest.setRoute(vehicleRequest.getRoute().toUpperCase().trim());
		boolean updateVehicle = false;
		Vehicle vehicle = vehicleOptional.get();
		
		if(vehicleRequest.getModel() != null && !vehicleRequest.getModel().equals(vehicle.getModel())) {
			
			vehicle.setModel(vehicleRequest.getModel());
			updateVehicle = true;
		}
		if(vehicleRequest.getStyle() != null && !vehicleRequest.getStyle().equals(vehicle.getStyle())) {
			
			vehicle.setStyle(vehicleRequest.getStyle());
			updateVehicle = true;
		}
		if (vehicleRequest.getStatus() != null
				&& VehicleStatus.of(vehicleRequest.getStatus()).compareTo(vehicle.getStatus()) != 0) {
			
			vehicle.setStatus(VehicleStatus.of(vehicleRequest.getStatus()));
			updateVehicle = true;
		}
		if (vehicleRequest.getRoute() != null
				&& (vehicle.getFleet() == null || !vehicleRequest.getRoute().equalsIgnoreCase(vehicle.getFleet().getRoute()))) {
			
			Fleet existingFleet = vehicle.getFleet();
			
			if(existingFleet != null) {
				
				reduceFleetCount(existingFleet.getId());
				
				incrementFleetCount(vehicleRequest.getRoute(), vehicle);
				
			}else {
				
				incrementFleetCount(vehicleRequest.getRoute(), vehicle);
			}
			updateVehicle = true;
		}
		return vehicle;
		
	}
	
	public void updateVehicle(List<VehicleRequest> vehicleRequest) {
		
//		List<Vehicle> vehicleOptional = vehicleRepository.findByRegistrationNumber(List<getRegistrationNumber));
//		
//		for()
		
		
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public VehicleResponse updateVehicle(VehicleRequest vehicleRequest) {
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
		
		if(!vehicleOptional.isPresent())
			throw new GeneralizedException(
					"Vehicle with registration number " + vehicleRequest.getRegistrationNumber() + " not found",
					ErrorCode.VEHICLE_NOT_FOUND, HttpStatus.BAD_REQUEST);
		
		vehicleRequest.setRoute(vehicleRequest.getRoute().toUpperCase().trim());
		boolean updateVehicle = false;
		Vehicle vehicle = vehicleOptional.get();
		
		if(vehicleRequest.getModel() != null && !vehicleRequest.getModel().equals(vehicle.getModel())) {
			
			vehicle.setModel(vehicleRequest.getModel());
			updateVehicle = true;
		}
		if(vehicleRequest.getStyle() != null && !vehicleRequest.getStyle().equals(vehicle.getStyle())) {
			
			vehicle.setStyle(vehicleRequest.getStyle());
			updateVehicle = true;
		}
		if (vehicleRequest.getStatus() != null
				&& VehicleStatus.of(vehicleRequest.getStatus()).compareTo(vehicle.getStatus()) != 0) {
			
			vehicle.setStatus(VehicleStatus.of(vehicleRequest.getStatus()));
			updateVehicle = true;
		}
		if (vehicleRequest.getRoute() != null
				&& (vehicle.getFleet() == null || !vehicleRequest.getRoute().equalsIgnoreCase(vehicle.getFleet().getRoute()))) {
			
			Fleet existingFleet = vehicle.getFleet();
			
			if(existingFleet != null) {
				
				reduceFleetCount(existingFleet.getId());
				
				incrementFleetCount(vehicleRequest.getRoute(), vehicle);
				
			}else {
				
				incrementFleetCount(vehicleRequest.getRoute(), vehicle);
			}
			updateVehicle = true;
		}
		
		if(updateVehicle)
			return new VehicleResponse(vehicleRepository.save(vehicle));
		else
			return new VehicleResponse(vehicle);
		
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void incrementFleetCount(String route, Vehicle vehicle) {
		
		Fleet updatedFleet = null;
		Optional<Fleet> fleetByRoute = fleetRepository.findByRoute(route);
		
		if(fleetByRoute.isPresent()) {
			
			fleetByRoute.get().setCount(fleetByRoute.get().getCount() + 1);
			updatedFleet = fleetRepository.saveAndFlush(fleetByRoute.get());

		}else {
			Fleet fleet = new Fleet();
			fleet.setCount(1);
			fleet.setRoute(route);
			updatedFleet = fleetRepository.save(fleet);
			
		}
		vehicle.setFleet(updatedFleet);
	}

	public Object getSpeedLimit() {
		
		return environment.getProperty(VehicleEnum.SPEED_LIMIT, "100");
	}

	public List<VehicleResponse> massUpdateVehicle(@Valid VehicleMassUpdateRequest vehicleMassUpdateRequest) {
		
		List<Vehicle> listOfVehicles = vehicleRepository.findAllById(vehicleMassUpdateRequest.getVehicleIdList());
		
		if (listOfVehicles == null || listOfVehicles.isEmpty())
			throw new GeneralizedException(ErrorCode.VEHICLE_NOT_FOUND.getReasonPhrase(), ErrorCode.VEHICLE_NOT_FOUND,
					HttpStatus.BAD_REQUEST);

//		listOfVehicles.forEach(vehicle -> vehicle.setStatus(vehicleMassUpdateRequest.getStatus()));
		listOfVehicles.forEach(vehicle -> vehicle.setStatus(VehicleStatus.of(vehicleMassUpdateRequest.getStatus())));
		
		List<Vehicle> savedVehicles = vehicleRepository.saveAll(listOfVehicles);
		
		List<VehicleResponse> vehicleList = savedVehicles.stream().map(vehicle -> new VehicleResponse(vehicle))
				.collect(Collectors.toList());
		
		return vehicleList;
		
	}

	public Object processVehiclesUsingFile(MultipartFile file) {
		
		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());
		System.out.println(file.getContentType());
		
		Map<String, List<Vehicle>> vehicles = excelHelper.excelToVehicles(file);
		
		List<Integer> vehicleIds = vehicles.get(VehicleEnum.DELETE).stream().map(v -> v.getId()).collect(Collectors.toList());
		
		deleteVehicles(vehicleIds);
		
		createVehicles(vehicles.get(VehicleEnum.CREATE));
		
		return vehicles;
	}

	
	private List<Integer> deleteVehicles(List<Integer> vehicleIds) {
		
		List<Integer> vehicleIdsDeleted = new ArrayList<>();
		
		for(Integer vehicleId: vehicleIds) {
			
			if(deleteVehicleById(vehicleId)) {
				System.out.println("Deleted vehcileId: " + vehicleId);
				vehicleIdsDeleted.add(vehicleId);
				
			}else {
				System.out.println("Issue in deleting vehcileId: " + vehicleId);
			}
		}
		return vehicleIdsDeleted;
	}
	
	private List<String> createVehicles(List<Vehicle> vehicles) {
		
		List<String> vehicleRegistrationIdsCreated = new ArrayList<>();
		
		List<VehicleRequest> vehicleRequests = vehicles.stream().map(v -> new VehicleRequest(v.getFleet().getRoute(),
				v.getRegistrationNumber(), v.getModel(), v.getStyle(), v.getStatus().getStatusCode()))
				.collect(Collectors.toList());
		
		for(VehicleRequest vehicleRequest : vehicleRequests) {
			
			try {
				VehicleResponse vehicleResponse = addVehicle(vehicleRequest);
				
				vehicleRegistrationIdsCreated.add(vehicleResponse.getRegistrationNumber());
				
				
			} catch (Exception e) {
				System.out.println("Issue in creating vehcile with registration number: " + vehicleRequest.getRegistrationNumber());
				e.printStackTrace();
			}
			
		}
		
		return vehicleRegistrationIdsCreated;
	}
}
