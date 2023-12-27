package com.vehicleservice.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryResponseList {
	
	private List<TelemetryResponse> telemetryList;
	
	private Pagination pagination;
	
}
