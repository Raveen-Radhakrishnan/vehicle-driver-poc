package com.vehicleservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRequest {
	
	@NotBlank(message = "name cannot be null or empty")
	private String name;
	
	private String address;
	
	@NotBlank
	@Pattern(regexp = "^([A-Z]{2}[0-9]{2})( )[0-9]{11}$", message = " Licence Number should be in this format UP14 20160034761 ")
	private String licenseNumber;
	
//	@Pattern(regexp = "^[6-9]{1}[0-9]{9}$", message = " Mobile number should be start from 6,7,8,9 and it should have 10 digits ")
//	@Pattern(regexp = "^((\\+91?)|\\+)?[6-9]{1}[0-9]{9}$", message = " Mobile number should be start from 6,7,8,9 and it should have 10 digits ")
	@Pattern(regexp = "^(\\+91)[6-9]{1}[0-9]{9}$", message = " Mobile number should be start from 6,7,8,9 and it should have 10 digits alongwith +91 prefix")
	private String phoneNumber;
	
	@Pattern(regexp = "^(D)|(R)|(N)|(B)$", message = "Allowed status values are: D, R, N, B")
	private String status;
	
}
