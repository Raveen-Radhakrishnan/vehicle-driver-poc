package com.vehicleservice.entity.employee;

import com.vehicleservice.auditing.employee.EmployeeAudit;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "EMPLOYEE_ID")
	private int employeeId;
	
	private String name;

	private String city;
	
	@Embedded
	@AttributeOverrides({ 
	    @AttributeOverride(name = "createdBy", column = @Column(name = "CREATED_BY")),
	    @AttributeOverride(name = "creationDate", column = @Column(name = "CREATION_DATE")),
	    @AttributeOverride(name = "lastModifiedBy", column = @Column(name = "LAST_MODIFIED_BY")),
	    @AttributeOverride(name = "lastModifiedDate", column = @Column(name = "LAST_MODIFIED_DATE"))
	    }) 
	private EmployeeAudit employeeAudit;
	
}
