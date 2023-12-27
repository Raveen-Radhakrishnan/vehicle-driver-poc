package com.vehicleservice.request;

import java.util.List;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverMassUpdateRequest {

//	@Pattern(regexp = "^[A-Z]{2}\\s[0-9]{2}\\s[A-Z]{2}\\s[0-9]{1,4}$", message = " Registration Number should be in this format AB 12 GH 1234 ")
	@Pattern(regexp = "^(D)|(R)|(N)|(B)$", message = "Allowed status values are: D, R, N, B")
	private String status;

	private List<Integer> driverIdList;

}
