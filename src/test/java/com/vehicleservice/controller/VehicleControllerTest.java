//package com.vehicleservice.controller;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
//import com.vehicleservice.entity.Fleet;
//import com.vehicleservice.entity.Vehicle;
//import com.vehicleservice.service.VehicleService;
//
//@WebMvcTest(VehicleController.class)
//class VehicleControllerTest {
//
//	@MockBean
//	VehicleService vehicleService;
//
//	@Autowired
//	MockMvc mockMvc;
//	
//	@Test
//	public void addVehicleTest() throws JsonProcessingException, Exception {
//		
//		Vehicle vehicle = new Vehicle(1, new Fleet(1, 0, null), "AB12", "Fiesta", "Sedan");
//		
//		when(vehicleService.addVehicle(vehicle)).thenReturn(vehicle);
//		
//		mockMvc.perform(MockMvcRequestBuilders.post("/vehicle/addVehicle")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(new ObjectMapper().writeValueAsString(vehicle)))
//				.andExpect(status().isCreated());
//		
//	}
//	
//	@Test
//	public void getAllVehiclesTest() throws Exception {
//		
//		List<Vehicle> vehicles = Arrays.asList(new Vehicle(1, new Fleet(1, 0, null), "AB12", "Fiesta", "Sedan"), 
//				new Vehicle(2, new Fleet(1, 0, null), "CD34", "Mustang", "Sports"));
//		
//		when(vehicleService.getAllVehicles()).thenReturn(vehicles);
//		
//		mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/getAllVehicles")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//		
//	}
//	
//	@Test
//	public void getVehicleById() throws Exception {
//		
//		int id = 2;
//		Vehicle vehicle = new Vehicle(2, new Fleet(1, 0, null), "AB12", "Fiesta", "Sedan");
//		
//		when(vehicleService.getVehicleById(id)).thenReturn(vehicle);
//		
//		mockMvc.perform(MockMvcRequestBuilders.get("/vehicle/getVehicleById/" + id)
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk());
//		
//	}
//
//}
