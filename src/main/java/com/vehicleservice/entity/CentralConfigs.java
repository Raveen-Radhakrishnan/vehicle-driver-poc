package com.vehicleservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "central_configs")
public class CentralConfigs {
	
	@Id
	@Column(name = "CONFIG_KEY")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String configKey;
	
	@Column(name = "CONFIG_VALUE")
	private String configValue;

	
}
