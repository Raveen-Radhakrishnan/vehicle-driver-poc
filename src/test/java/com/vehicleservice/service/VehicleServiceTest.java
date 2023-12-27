//package com.vehicleservice.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import com.vehicleservice.entity.Fleet;
//import com.vehicleservice.entity.Vehicle;
//import com.vehicleservice.repository.VehicleRepository;
//
//@SpringBootTest
//class VehicleServiceTest {
//	
//	@Autowired
//	VehicleService vehicleService;
//	
//	@MockBean
//	VehicleRepository vehicleRepository;
//
//	@Test
//	public void addVehicle() {
//		
//		Vehicle vehicle = new Vehicle(1, new Fleet(1, 0, null), "AB12", "Fiesta", "Sedan");
//		
//		when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
//		
//		assertEquals(vehicle, vehicleService.addVehicle(vehicle));
//		
//		
//	}
//	
//	@Test
//	public void getAllVehicles() {
//		
//		List<Vehicle> vehicles = Arrays.asList(new Vehicle(1, new Fleet(1, 0, null), "AB12", "Fiesta", "Sedan"), 
//				new Vehicle(2, new Fleet(1, 0, null), "CD34", "Mustang", "Sports"));
//		
//		when(vehicleRepository.findAll()).thenReturn(vehicles);
//		
//		assertEquals(2, vehicleService.getAllVehicles().size());
//		
//	}
//	
//	@Test
//	public void getVehicleById() {
//		
//		int id =  1;
//		Vehicle vehicle = new Vehicle(id, new Fleet(1, 0, null), "AB12", "Fiesta", "Sedan");
//		
//		when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
//		
//		assertEquals(vehicle, vehicleService.getVehicleById(id));
//		
//	}
//
//}
