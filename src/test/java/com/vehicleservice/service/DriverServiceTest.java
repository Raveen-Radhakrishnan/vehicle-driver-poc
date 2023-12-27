//package com.vehicleservice.service;
//
//import static org.junit.jupiter.api.Assertions.*;
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
//import com.vehicleservice.entity.Driver;
//import com.vehicleservice.repository.DriverRepository;
//
//@SpringBootTest
//class DriverServiceTest {
//	
//	@Autowired
//	DriverService driverService;
//	
//	@MockBean
//	DriverRepository driverRepository;
//
//	@Test
//	public void addDriver() {
//		
//		Driver driver = new Driver(1, "John", "Airoli", "LIN1234", "1234567890");
//		
//		when(driverRepository.save(driver)).thenReturn(driver);
//		
//		assertEquals(driver, driverService.addDriver(driver));
//		
//		
//	}
//	
//	@Test
//	public void getAllDrivers() {
//		
//		List<Driver> drivers = Arrays.asList(new Driver(1, "John", "Airoli", "LIN1234", "1234567890"), 
//				new Driver(2, "Tom", "Kurla", "LIN4321", "9876543210"));
//		
//		when(driverRepository.findAll()).thenReturn(drivers);
//		
//		assertEquals(2, driverService.getAllDrivers().size());
//		
//	}
//	
//	@Test
//	public void getDriverById() {
//		
//		int id =  1;
//		Driver driver = new Driver(id, "John", "Airoli", "LIN1234", "1234567890");
//		
//		when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
//		
//		assertEquals(driver, driverService.getDriverById(id));
//		
//	}
//
//}
