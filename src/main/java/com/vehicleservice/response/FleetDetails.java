package com.vehicleservice.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vehicleservice.entity.Fleet;
import com.vehicleservice.entity.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FleetDetails {

	private int id;

	private int count;

	private String route;

	@JsonIgnoreProperties("fleet")
	private List<Vehicle> vehicles;

	public FleetDetails(int id, int count, String route) {
		this.id = id;
		this.count = count;
		this.route = route;
	}

	public FleetDetails(Fleet fleet) {
		this.id = fleet.getId();
		this.count = fleet.getCount();
		this.route = fleet.getRoute();
	}

}
