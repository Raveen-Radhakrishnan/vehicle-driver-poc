package com.vehicleservice.service.vehicleDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.vehicleservice.helper.ExcelGenerator;
import com.vehicleservice.helper.ExcelHelper;
import com.vehicleservice.repository.vehicleDriver.FleetRepository;
import com.vehicleservice.repository.vehicleDriver.VehicleRepository;
import com.vehicleservice.request.VehicleMassUpdateRequest;
import com.vehicleservice.request.VehicleRequest;
import com.vehicleservice.response.Pagination;
import com.vehicleservice.response.VehicleResponse;
import com.vehicleservice.response.VehicleResponseList;
import com.vehicleservice.response.VehicleResponseListSlice;

import jakarta.servlet.http.HttpServletResponse;
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

	@Autowired
	ExcelGenerator excelGenerator;

//	@Transactional(propagation = Propagation.REQUIRED)
//	public VehicleResponse addVehicle(VehicleRequest vehicleRequest) {
//		
//		Optional<Vehicle> vehicleOptional = vehicleRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
//		
//		if(vehicleOptional.isPresent())
//			throw new GeneralizedException(ErrorCode.DUPLICATE_REGISTRATION_NUMBER.getReasonPhrase(),
//					ErrorCode.DUPLICATE_REGISTRATION_NUMBER, HttpStatus.BAD_REQUEST);
//		
//		Vehicle vehicle = transformVehicleRequest(vehicleRequest);
//		
//		Fleet fleet = vehicle.getFleet();
//		String route = fleet.getRoute();
//		
//		Optional<Fleet> fleetByRoute = fleetRepository.findByRoute(route);
//		
//		if(fleetByRoute.isPresent()) {
//			
//			fleetByRoute.get().setCount(fleetByRoute.get().getCount() + 1);
//			Fleet updatedFleet = fleetRepository.saveAndFlush(fleetByRoute.get());
//			vehicle.setFleet(updatedFleet);
//		}else {
//			fleet.setCount(1);
//			Fleet updatedFleet = fleetRepository.save(fleet);
//			vehicle.setFleet(updatedFleet);
//			
//		}
//		
//		Vehicle savedVehicle = vehicleRepository.save(vehicle);
//		
//		return new VehicleResponse(savedVehicle);
//		
//	}

	@Transactional(propagation = Propagation.REQUIRED)
	public VehicleResponse addVehicle(VehicleRequest vehicleRequest) {
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());
		
		if(vehicleOptional.isPresent())
			throw new GeneralizedException(ErrorCode.DUPLICATE_REGISTRATION_NUMBER.getReasonPhrase(),
					ErrorCode.DUPLICATE_REGISTRATION_NUMBER, HttpStatus.BAD_REQUEST);
		
		Vehicle vehicle = transformVehicleRequest(vehicleRequest);
		
		Fleet fleet = vehicle.getFleet();
		String route = fleet.getRoute();
		
		Optional<Fleet> fleetByRoute = fleetRepository.findByRoute(route);
		
		System.out.println(fleetByRoute.get().getRoute().equalsIgnoreCase("mumbai")
				? " mumbai count: " + fleetByRoute.get().getCount()
				: "not mumbai");
		
		if(fleetByRoute.isPresent()) {
			
			fleetByRoute.get().setCount(fleetByRoute.get().getCount() + 1);
//			Fleet updatedFleet = fleetRepository.saveAndFlush(fleetByRoute.get());
//			vehicle.setFleet(updatedFleet);
			vehicle.setFleet(fleetByRoute.get());
		}else {
			fleet.setCount(1);
//			Fleet updatedFleet = fleetRepository.save(fleet);
//			vehicle.setFleet(updatedFleet);
//			vehicle.setFleet(fleet);
		}
		
		Vehicle savedVehicle = vehicleRepository.save(vehicle);
		
		return new VehicleResponse(savedVehicle);
		
	}
	
	private Vehicle transformVehicleRequest(VehicleRequest vehicleRequest) {
		
		Fleet fleet = Fleet.builder().id(0).count(0).route(vehicleRequest.getRoute().trim().toUpperCase()).build();
		
		Vehicle vehicle = Vehicle.builder().fleet(fleet).model(vehicleRequest.getModel())
				.registrationNumber(vehicleRequest.getRegistrationNumber()).style(vehicleRequest.getStyle())
				.status(vehicleRequest.getStatus() != null ? VehicleStatus.of(vehicleRequest.getStatus())
						: VehicleStatus.NEW)
				.build();
		
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

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteVehicleById(Integer vehicleId) {
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
		
		if(vehicleOptional.isPresent()) {
			
			Vehicle vehicle = vehicleOptional.get();
			if(vehicle.getFleet() != null) {
				
				Integer fleetId = vehicle.getFleet().getId();
				
				reduceFleetCountInDb(fleetId);
				
			}
			vehicleRepository.deleteById(vehicleId);
			
			
			return true;
		}
		
		return false;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	private void reduceFleetCountInDb(Integer fleetId) {
		
		Optional<Fleet> fleetOptional = fleetRepository.findById(fleetId);
		
		if(fleetOptional.isPresent()) {
			
			Fleet fleet = fleetOptional.get();
			
			fleet.setCount(fleet.getCount() - 1);
			
			fleetRepository.save(fleet);
			
		}
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void reduceFleetCount(Integer fleetId, Vehicle vehicle) {
		
		Optional<Fleet> fleetOptional = fleetRepository.findById(fleetId);
		
		if(fleetOptional.isPresent()) {
			
			Fleet fleet = fleetOptional.get();
			
			fleet.setCount(fleet.getCount() - 1);
			
			vehicle.setFleet(fleet);
			
//			fleetRepository.save(fleet);
			
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
				
				reduceFleetCountInDb(existingFleet.getId());
				
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
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public VehicleResponse updateVehicle(VehicleRequest vehicleRequest) {
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findByRegistrationNumber(vehicleRequest.getRegistrationNumber());

		if (!vehicleOptional.isPresent())
			throw new GeneralizedException(ErrorCode.VEHICLE_NOT_FOUND.getReasonPhrase(), ErrorCode.VEHICLE_NOT_FOUND,
					HttpStatus.BAD_REQUEST,
					" [ vehicle registration number: " + vehicleRequest.getRegistrationNumber() + " ]");
		
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
				
				System.out.println("update opr");
				System.out.println(existingFleet.getRoute().equalsIgnoreCase("mumbai")
						? " mumbai count: " + existingFleet.getCount()
						: "not mumbai");
				
				reduceFleetCountInDb(existingFleet.getId());
//				reduceFleetCount(existingFleet.getId(), vehicle);
				
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
//			updatedFleet = fleetRepository.saveAndFlush(fleetByRoute.get());
			vehicle.setFleet(fleetByRoute.get());

		}else {
			Fleet fleet = new Fleet();
			fleet.setCount(1);
			fleet.setRoute(route);
//			updatedFleet = fleetRepository.save(fleet);
			vehicle.setFleet(fleet);
		}
//		vehicle.setFleet(updatedFleet);
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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Object processVehiclesUsingFile(MultipartFile file) {
		
		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());
		System.out.println(file.getContentType());
		
		try {
			Map<String, List<Vehicle>> vehicles = excelHelper.excelToVehicles(file);
			
			List<Integer> vehicleIds = vehicles.get(VehicleEnum.DELETE).stream().map(v -> v.getId()).collect(Collectors.toList());
			
			deleteVehicles(vehicleIds);
//			deleteAllVehicles(vehicles.get(VehicleEnum.DELETE));
			
			createVehicles(vehicles.get(VehicleEnum.CREATE));

			updateVehicles(vehicles.get(VehicleEnum.UPDATE));
			
			return vehicles;

		} catch (GeneralizedException e) {
			
			throw e;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private List<Integer> deleteVehicles(List<Integer> vehicleIds) {
		
		List<Integer> vehicleIdsDeleted = new ArrayList<>();
		
		for(Integer vehicleId: vehicleIds) {
			
			if(deleteVehicleById(vehicleId)) {
				System.out.println("Deleted vehcileId: " + vehicleId);
				vehicleIdsDeleted.add(vehicleId);
				
			} else {
				System.out.println("Issue in deleting vehcileId: " + vehicleId);
				throw new GeneralizedException(ErrorCode.VEHICLE_NOT_FOUND.getReasonPhrase(),
						ErrorCode.VEHICLE_NOT_FOUND, HttpStatus.BAD_REQUEST, " [ vehicleId: " + vehicleId + " ]");
			}
		}
		return vehicleIdsDeleted;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	private List<String> createVehicles(List<Vehicle> vehicles) {
		
		List<String> vehiclesCreated = new ArrayList<>();
		
		List<VehicleRequest> vehicleRequests = vehicles.stream().map(v -> new VehicleRequest(v.getFleet().getRoute(),
				v.getRegistrationNumber(), v.getModel(), v.getStyle(), v.getStatus().getStatusCode()))
				.collect(Collectors.toList());
		
		for(VehicleRequest vehicleRequest : vehicleRequests) {
			
			try {
				VehicleResponse vehicleResponse = addVehicle(vehicleRequest);
				
				vehiclesCreated.add(vehicleResponse.getRegistrationNumber());
				
				
			} catch (GeneralizedException e) {
				System.out.println("Issue in creating vehcile with registration number: " + vehicleRequest.getRegistrationNumber());
				throw new GeneralizedException(e, " [ vehicle registration number: " + vehicleRequest.getRegistrationNumber() + " ]");
			}
			
		}
		
		return vehiclesCreated;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private List<String> updateVehicles(List<Vehicle> vehicles) {
		
		List<String> vehiclesModified = new ArrayList<>();
		
		List<VehicleRequest> vehicleRequests = vehicles.stream().map(v -> new VehicleRequest(v.getFleet().getRoute(),
				v.getRegistrationNumber(), v.getModel(), v.getStyle(), v.getStatus().getStatusCode()))
				.collect(Collectors.toList());
		
		for(VehicleRequest vehicleRequest : vehicleRequests) {
			
			try {
				VehicleResponse vehicleResponse = updateVehicle(vehicleRequest);
				
				vehiclesModified.add(vehicleResponse.getRegistrationNumber());
				
			} catch (GeneralizedException e) {
				System.out.println("Issue in modifying vehcile with registration number: " + vehicleRequest.getRegistrationNumber());
				throw e;
			}
			
		}
		
		return vehiclesModified;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	private void deleteAllVehicles(List<Vehicle> vehicles) {
		
		List<Integer> vehicleIds = vehicles.stream().map(v -> v.getId()).collect(Collectors.toList());
		
		List<Vehicle> vehicleEntities = vehicleRepository.findAllById(vehicleIds);
		
		Map<String, List<Vehicle>> routeVehicleMap = vehicleEntities.stream()
				.collect(Collectors.groupingBy(v -> v.getFleet().getRoute()));
		
//		for(Map.Entry<String, List<Vehicle>> entry : routeVehicleMap.entrySet()) {
//			
//			
//			
//			
//		}
		
		Map<String, Fleet> updatedFleet = calculateFleetCountAfterDeletion(routeVehicleMap);
		
		fleetRepository.saveAll(updatedFleet.values());
		
		vehicleRepository.deleteAllById(vehicleIds);
		
		
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	private Map<String, Fleet> calculateFleetCountAfterDeletion(Map<String, List<Vehicle>> routeVehicleMap){
		
		List<String> routeList = routeVehicleMap.keySet().stream().collect(Collectors.toList());
		
		List<Fleet> listOfFleet = fleetRepository.findByRouteIn(routeList);
		
		Map<String, Fleet> updatedFleet = new HashMap<>();
		
		for(Fleet fleet : listOfFleet) {
			
			int updatedCount = fleet.getCount() - routeVehicleMap.get(fleet.getRoute()).size();
			
			if(updatedCount < 0)
				updatedCount = 0;

			fleet.setCount(updatedCount);
			
			updatedFleet.put(fleet.getRoute(), fleet);
		}
		return updatedFleet;
		
	}
	
	private void saveAllVehicles(Map<String, Fleet> updatedFleet) {
		
		
		
	}

	public void downloadVehicleData(HttpServletResponse response) throws IOException {
		
		VehicleResponseList vehicleResponseList = getAllVehicles(0);
		List<VehicleResponse> vehicleList = vehicleResponseList.getVehicleList();
		
		String sheetName = environment.getProperty(VehicleEnum.VEHICLE_EXCEL_SHEET_NAME, "VehicleList");
		String fileName = environment.getProperty(VehicleEnum.VEHICLE_EXCEL_FILE_NAME, "VehicleData");
		
		excelGenerator.exportToExcel(response, vehicleList, fileName, sheetName,
				VehicleEnum.VEHICLE_EXCEL_COLUMN_DOWNLOAD);
		
		
	}
}
