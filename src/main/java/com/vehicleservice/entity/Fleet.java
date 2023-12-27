package com.vehicleservice.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fleet")
public class Fleet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int count;
	
	private String route;
	
	@OneToMany(mappedBy = "fleet", cascade = CascadeType.PERSIST)
//	@JsonIgnore
	@JsonIgnoreProperties("fleet")
	private List<Vehicle> vehicles;

	public Fleet(int id, int count, String route) {
		this.id = id;
		this.count = count;
		this.route = route;
	}
	
	
	
}
