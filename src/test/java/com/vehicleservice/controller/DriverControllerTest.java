//package com.vehicleservice.controller;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.vehicleservice.entity.Driver;
//import com.vehicleservice.service.DriverService;
//
//@WebMvcTest(DriverController.class)
//class DriverControllerTest {
//
//	@MockBean
//	DriverService driverService;
//
//	@Autowired
//	MockMvc mockMvc;
//	
//	@Test
//	public void addDriverTest() throws JsonProcessingException, Exception {
//		
//		Driver driver = new Driver(1, "John", "Airoli", "LIN1234", "1234567890");
//		
//		when(driverService.addDriver(driver)).thenReturn(driver);
//		
//		mockMvc.perform(MockMvcRequestBuilders.post("/driver/addDriver")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(new ObjectMapper().writeValueAsString(driver)))
//				.andExpect(status().isCreated());
//		
//	}
//	
//	@Test
//	public void getAllDriversTest() throws Exception {
//		
//		List<Driver> drivers = Arrays.asList(new Driver(1, "John", "Airoli", "LIN1234", "1234567890"), 
//				new Driver(2, "Tom", "Kurla", "LIN4321", "9876543210"));
//		
//		when(driverService.getAllDrivers()).thenReturn(drivers);
//		
//		mockMvc.perform(MockMvcRequestBuilders.get("/driver/getAllDrivers")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$[0].name", is("John")))
//				.andExpect(jsonPath("$", hasSize(2)));
//		
//		
//	}
//	
//	@Test
//	public void getDriverById() throws Exception {
//		
//		int id = 2;
//		Driver driver = new Driver(2, "Tom", "Kurla", "LIN4321", "9876543210");
//		
//		when(driverService.getDriverById(id)).thenReturn(driver);
//		
//		mockMvc.perform(MockMvcRequestBuilders.get("/driver/getDriverById/" + id)
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//		
//	}
//
//}
