package com.vehicleservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {

	private String route;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{2}\\s[0-9]{2}\\s[A-Z]{2}\\s[0-9]{1,4}$", message = " Registration Number should be in this format AB 12 GH 1234 ")
	private String registrationNumber;

	private String model;

	private String style;
	
	@Pattern(regexp = "^(N)|(I)|(T)|(R)|(P)$", message = "Allowed status values are: N, I, T, R, P")
	private String status;

}
