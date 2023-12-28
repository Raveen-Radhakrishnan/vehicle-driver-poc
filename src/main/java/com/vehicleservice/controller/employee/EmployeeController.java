package com.vehicleservice.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vehicleservice.entity.employee.Employee;
import com.vehicleservice.service.employee.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@PostMapping("/employees")
	public ResponseEntity<Employee>	addEmployee(@RequestBody Employee employee){
		
		return new ResponseEntity<Employee>(employeeService.addEmployee(employee), HttpStatus.CREATED);
		
		
	}

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> findAllEmployees(){
		
		return new ResponseEntity<List<Employee>>(employeeService.findAllEmployees(), HttpStatus.OK);
		
		
	}
	
	
}
