package com.vehicleservice.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "custom.telemetry-scheduler")
@Data
public class TelemetryProps {
	
	private Boolean enabled;
	private Long initialDelay;
	private Long fixedDelay;
	private String cron;
	private Long timeIntervalInSeconds;
	
}
