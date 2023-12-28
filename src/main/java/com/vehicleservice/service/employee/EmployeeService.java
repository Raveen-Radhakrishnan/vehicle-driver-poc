package com.vehicleservice.service.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vehicleservice.entity.employee.Employee;
import com.vehicleservice.repository.employee.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	public Employee addEmployee(Employee employee) {
		
		return employeeRepository.save(employee);
		
	}


	public List<Employee> findAllEmployees() {
		
		return employeeRepository.findAll();
	}
	
}
