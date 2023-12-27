package com.vehicleservice.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponseListSlice {
	
	private List<VehicleResponse> vehicleList;
	
	private boolean hasNextPage;
	
}
