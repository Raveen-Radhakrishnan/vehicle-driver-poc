package com.vehicleservice.scheduler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vehicleservice.constants.VehicleEnum;
import com.vehicleservice.entity.CentralConfigs;
import com.vehicleservice.entity.Driver;
import com.vehicleservice.entity.Telemetry;
import com.vehicleservice.entity.Vehicle;
import com.vehicleservice.props.TelemetryProps;
import com.vehicleservice.repository.CentralConfigRepository;
import com.vehicleservice.repository.DriverRepository;
import com.vehicleservice.repository.TelemetryRepository;
import com.vehicleservice.repository.VehicleRepository;

import jakarta.annotation.PostConstruct;

@Component
public class TelemetryScheduler {
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	TelemetryProps telemetryProps;
	
	@Autowired
	private TelemetryRepository telemetryRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private CentralConfigRepository centralConfigRepository;
	
	static List<String> telemetryFieldsList = new ArrayList<>();
	static List<String> booleanFieldsList = new ArrayList<>();
	static List<String> tenPointFieldsList = new ArrayList<>();
	
	static {
		telemetryFieldsList = Arrays.asList(VehicleEnum.TELEMETRY_FIELDS);
		booleanFieldsList = Arrays.asList(VehicleEnum.BOOLEAN_FIELDS);
		tenPointFieldsList = Arrays.asList(VehicleEnum.TEN_POINT_FIELDS);
	}
	
//	@PostConstruct
//	@EventListener(ApplicationReadyEvent.class)
	public void checkForMissedTask() {
		
		Optional<CentralConfigs> configOptional = centralConfigRepository.findById(VehicleEnum.TELEMETRY_TIMESTAMP);
		
		if(configOptional.isPresent()) {
			
			String lastRunTimestamp = configOptional.get().getConfigValue();
			
			if(lastRunTimestamp != null) {
				
				try {
					Date lastRunDate = format.parse(lastRunTimestamp);
					
					long currentMillis = new Date().toInstant().getEpochSecond();
					long lastRunMillis = lastRunDate.toInstant().getEpochSecond();
					
					System.out.println("running missed task");
					
					if(currentMillis - lastRunMillis > telemetryProps.getTimeIntervalInSeconds())
						insertTelemetryRecords();
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}else {
				insertTelemetryRecords();
				
			}
			
		}
		
		
	}
	
	
	@Transactional
//	@Scheduled(initialDelay = 5000, fixedDelay = 2000)
	@Scheduled(initialDelayString = "#{telemetryProps.getInitialDelay()}", fixedDelayString = "#{telemetryProps.getFixedDelay()}")
//	@Scheduled(cron = "0 30 15 * * WED")
//	@Scheduled(cron = "#{telemetryProps.getCron()}")
	public void insertTelemetryRecords() {
		
//		System.out.println(new Date());
		
		if(!telemetryProps.getEnabled())
			return;
		
		Integer randomIndex = new Random().nextInt(telemetryFieldsList.size());
		
		String parameter = telemetryFieldsList.get(randomIndex);
		
		Double paramValue = generateRandomParamValue(parameter);
		DecimalFormat df = new DecimalFormat("#.##");      
		paramValue = Double.valueOf(df.format(paramValue));
		
		LocalDateTime currentTimeStamp = LocalDateTime.now();

		int count = (int) driverRepository.count();
		
		Integer randomId = new Random().nextInt(1, count+1);

		Driver driver = findRandomDriver(randomId);
		
		Vehicle vehicle = findRandomVehicle(randomId);
		
		if(vehicle == null) {
			
			System.out.println("Vehicle not found : " + randomId);
			return;
		}
		
		Telemetry telemetry = new Telemetry(driver, vehicle, parameter, paramValue, currentTimeStamp);
		
		telemetryRepository.save(telemetry);
		
		System.out.println("Save successful");
		
//		String currentTime = format.format(new Date());
//		
//		centralConfigRepository.save(new CentralConfigs(VehicleEnum.TELEMETRY_TIMESTAMP, currentTime));
		
		
	}


	private Double generateRandomParamValue(String parameter) {
		
		if(parameter.equalsIgnoreCase(VehicleEnum.MILEAGE)) {
			
			return new Random().nextDouble(5.0, 50.0);
			
		}else if(parameter.equalsIgnoreCase(VehicleEnum.CURRENT_SPEED) ||
				parameter.equalsIgnoreCase(VehicleEnum.DISTANCE)) {
			
			return new Random().nextDouble(5.0, 200.0);
			
		}else if(booleanFieldsList.contains(parameter)) {
			
			boolean value = new Random().nextBoolean();
			
			return value ? 1.0 : 0.0;
			
		}else {
			
			Integer value = new Random().nextInt(1, 11);
			
			return value.doubleValue();
			
		}
		
	}
	
	private Driver findRandomDriver(Integer randomId) {
		
		Driver driver = null;
		
		Optional<Driver> driverOptional = driverRepository.findById(randomId);
		
		if(driverOptional.isPresent()) {
			
			driver = driverOptional.get();
		}
		
		return driver;
	}
	
	private Vehicle findRandomVehicle(Integer randomId) {
		
		Vehicle vehicle = null;
		
		Optional<Vehicle> vehicleOptional = vehicleRepository.findById(randomId);
		
		if(vehicleOptional.isPresent()) {
			
			vehicle = vehicleOptional.get();
		}
		return vehicle;
	}
	
}
