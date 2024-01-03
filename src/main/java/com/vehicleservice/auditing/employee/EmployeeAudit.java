package com.vehicleservice.auditing.employee;

import java.util.Date;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EmployeeAudit {
	
	private String createdBy;
	
	private Date creationDate;
	
	private String lastModifiedBy;
	
	private Date lastModifiedDate;
	
}
