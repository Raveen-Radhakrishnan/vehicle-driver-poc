package com.vehicleservice.service.employee;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vehicleservice.auditing.employee.EmployeeAudit;
import com.vehicleservice.entity.employee.Employee;
import com.vehicleservice.exception.ErrorCode;
import com.vehicleservice.exception.GeneralizedException;
import com.vehicleservice.repository.employee.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	public Employee addEmployee(Employee employee) {
		
		String createdBy = "admin";
		Date creationDate = new Date();
		
		EmployeeAudit employeeAudit = new EmployeeAudit(createdBy, creationDate, createdBy, creationDate);
		
		employee.setEmployeeAudit(employeeAudit);
		
		return employeeRepository.save(employee);
		
	}


	public List<Employee> findAllEmployees() {
		
		return employeeRepository.findAll();
	}


	public Employee updateEmployeeById(Employee employee) {
		
		Optional<Employee> employeeOptional = employeeRepository.findById(employee.getEmployeeId());
		
		if(!employeeOptional.isPresent())
			throw new GeneralizedException(
					"Employee with ID " + employee.getEmployeeId() + " not found",
					ErrorCode.EMPLOYEE_NOT_FOUND, HttpStatus.BAD_REQUEST);

		Employee existingEmployee = employeeOptional.get();
		
		if(!existingEmployee.getCity().equalsIgnoreCase(employee.getCity()) ||
				!existingEmployee.getName().equalsIgnoreCase(employee.getName())) {
			
			String modifiedBy = "admin";
			Date modifiedDate = new Date();

			if(employee.getCity() != null)
				existingEmployee.setCity(employee.getCity());

			if(employee.getName() != null)
				existingEmployee.setName(employee.getName());
			
			if(existingEmployee.getEmployeeAudit() != null) {
				
				existingEmployee.getEmployeeAudit().setLastModifiedBy(modifiedBy);
				existingEmployee.getEmployeeAudit().setLastModifiedDate(modifiedDate);
			}
			
			return employeeRepository.save(existingEmployee);
		
		}else {
			
			return existingEmployee;
		}
		
		
		
	}
	
}
