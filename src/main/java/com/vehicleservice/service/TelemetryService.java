package com.vehicleservice.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vehicleservice.constants.VehicleEnum;
import com.vehicleservice.entity.Driver;
import com.vehicleservice.entity.Telemetry;
import com.vehicleservice.exception.ErrorCode;
import com.vehicleservice.exception.GeneralizedException;
import com.vehicleservice.helper.BestPerformerHelper;
import com.vehicleservice.repository.DriverRepository;
import com.vehicleservice.repository.TelemetryRepository;
import com.vehicleservice.repository.VehicleRepository;
import com.vehicleservice.response.BestPerformerResponse;
import com.vehicleservice.response.DriverResponse;
import com.vehicleservice.response.DriverScoreResponse;
import com.vehicleservice.response.Pagination;
import com.vehicleservice.response.TelemetryResponse;
import com.vehicleservice.response.TelemetryResponseList;

@Service
public class TelemetryService {

	@Autowired
	private TelemetryRepository telemetryRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private Environment environment;
	
	public Telemetry addTelemetryRecord(Telemetry telemetry) {
		
//		boolean isVehiclePresent = vehicleRepository.existsById(telemetry.getVehicleId());
		boolean isVehiclePresent = vehicleRepository.existsById(telemetry.getVehicleId());

		if(!isVehiclePresent)
			throw new GeneralizedException(ErrorCode.VEHICLE_NOT_FOUND.getReasonPhrase(),
					ErrorCode.VEHICLE_NOT_FOUND, HttpStatus.BAD_REQUEST);

//		boolean isDriverPresent = driverRepository.existsById(telemetry.getDriverId());
		boolean isDriverPresent = driverRepository.existsById(telemetry.getDriverId());
		
		if(!isDriverPresent)
			throw new GeneralizedException(ErrorCode.DRIVER_NOT_FOUND.getReasonPhrase(),
					ErrorCode.DRIVER_NOT_FOUND, HttpStatus.BAD_REQUEST);
		
		telemetry.setTime(LocalDateTime.now());
		
		return telemetryRepository.save(telemetry);
		
	}

	public List<Telemetry> getTelemetryRecordsOld(String startTime, String endTime){
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd_HH:mm:ss").createDateTimeFormatter();

//		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd").createDateTimeFormatter();
//		LocalDate startDate = LocalDate.parse(startTime, dateTimeFormatter);
//		LocalDate endDate = LocalDate.parse(endTime, dateTimeFormatter);
//		LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
//		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
		
		LocalDateTime startTimeLocal = LocalDateTime.parse(startTime, dateTimeFormatter);
		LocalDateTime endTimeLocal = LocalDateTime.parse(endTime, dateTimeFormatter);
		
		return telemetryRepository.findAllByTimeBetween(startTimeLocal, endTimeLocal);
		
	}

	public TelemetryResponseList getTelemetryRecords(String startTime, String endTime, Integer page){
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd_HH:mm:ss").createDateTimeFormatter();
		
//		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd").createDateTimeFormatter();
//		LocalDate startDate = LocalDate.parse(startTime, dateTimeFormatter);
//		LocalDate endDate = LocalDate.parse(endTime, dateTimeFormatter);
//		LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
//		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
		
		LocalDateTime startTimeLocal = LocalDateTime.parse(startTime, dateTimeFormatter);
		LocalDateTime endTimeLocal = LocalDateTime.parse(endTime, dateTimeFormatter);

		Integer pageSize = Integer.parseInt(environment.getProperty(VehicleEnum.PAGE_SIZE, "5"));
		
		Pageable pageable = PageRequest.of(page == null ? 0 : page, pageSize);
		
		Page<Telemetry> telemetries = telemetryRepository.findAllByTimeBetween(startTimeLocal, endTimeLocal, pageable);
		
		TelemetryResponseList telemetryResponseList = new TelemetryResponseList();
		
		Pagination pagination = new Pagination(telemetries.getTotalPages(), telemetries.getTotalElements(),
				telemetries.getNumber());

		List<TelemetryResponse> telemetryList = telemetries.getContent().stream().map(telemetry -> new TelemetryResponse(telemetry))
				.collect(Collectors.toList());
		
		telemetryResponseList.setTelemetryList(telemetryList);
		telemetryResponseList.setPagination(pagination);
		
		return telemetryResponseList;
		
	}
	
	public BestPerformerResponse getBestPerformer(String startTime, String endTime){
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd_HH:mm:ss").createDateTimeFormatter();

		LocalDateTime startTimeLocal = LocalDateTime.parse(startTime, dateTimeFormatter);
		LocalDateTime endTimeLocal = LocalDateTime.parse(endTime, dateTimeFormatter);
		
		List<Telemetry> listOfRecords = telemetryRepository.findAllByTimeBetween(startTimeLocal, endTimeLocal);
		
		if(listOfRecords != null && !listOfRecords.isEmpty()) {
			
			Map<Integer, List<Telemetry>> driverMap = listOfRecords.stream().collect(Collectors.groupingBy(Telemetry::getDriverId));
//			Map<Integer, List<Telemetry>> driverMap = new HashMap<>();
			
			Entry<Integer,Double> bestPerformer = BestPerformerHelper.calculateBestPerformer(driverMap);
			
			if(bestPerformer != null && bestPerformer.getKey() != null) {
				
				Optional<Driver> bestDriverOptional = driverRepository.findById(bestPerformer.getKey());
				
				if(!bestDriverOptional.isPresent())
					throw new GeneralizedException(ErrorCode.DRIVER_NOT_FOUND.getReasonPhrase(),
							ErrorCode.DRIVER_NOT_FOUND, HttpStatus.NOT_FOUND);
				
				Driver bestDriver = bestDriverOptional.get();
				
//				Driver bestDriver = bestDriverOptional.orElseThrow(() -> new GeneralizedException(ErrorCode.DRIVER_NOT_FOUND.getReasonPhrase(),
//						ErrorCode.DRIVER_NOT_FOUND, HttpStatus.NOT_FOUND));
				
				return new BestPerformerResponse(bestPerformer.getValue(), new DriverResponse(bestDriver));
				
			}else {
				throw new GeneralizedException(ErrorCode.BEST_PERFORMER_NOT_AVAILABLE.getReasonPhrase(),
						ErrorCode.BEST_PERFORMER_NOT_AVAILABLE, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}else {
			
			throw new GeneralizedException(ErrorCode.TELEMETRY_RECORDS_NOT_FOUND.getReasonPhrase(),
					ErrorCode.TELEMETRY_RECORDS_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		
	}
	
	public List<DriverScoreResponse> getScoresForLastHour(){
		
		List<DriverScoreResponse> driverScoreResponses = null;
		
		LocalDateTime endTimeLocal = LocalDateTime.now();
		
		LocalDateTime startTimeLocal = endTimeLocal.minusHours(1L);
		
		List<Telemetry> listOfRecords = telemetryRepository.findAllByTimeBetween(startTimeLocal, endTimeLocal);
		
		if(listOfRecords != null && !listOfRecords.isEmpty()) {
			
			driverScoreResponses = new ArrayList<>();
			
			Map<Integer, List<Telemetry>> driverMap = listOfRecords.stream().collect(Collectors.groupingBy(Telemetry::getDriverId));
//			Map<Integer, List<Telemetry>> driverMap = new HashMap<>();
			
			Map<Integer, Double> scoreMap = BestPerformerHelper.calculateIndividualScores(driverMap);
			
			if(scoreMap != null && !scoreMap.isEmpty()) {
				
				List<Driver> drivers = driverRepository.findAllById(scoreMap.keySet());
				
				if(drivers == null || drivers.isEmpty())
					throw new GeneralizedException(ErrorCode.DRIVER_NOT_FOUND.getReasonPhrase(),
							ErrorCode.DRIVER_NOT_FOUND, HttpStatus.NOT_FOUND);
				
				for(Driver driver : drivers) {
					
					DriverScoreResponse driverScoreResponse = new DriverScoreResponse(scoreMap.get(driver.getId()), new DriverResponse(driver));
					
					driverScoreResponses.add(driverScoreResponse);
				}
//				Map<Driver, DriverScoreResponse> driverScoreResponseMap = drivers.stream().collect(Collectors.toMap(Function.identity(), d -> 
//				new DriverScoreResponse(scoreMap.get(d.getId()), new DriverResponse(d))));
//				
//				driverScoreResponses = driverScoreResponseMap.values().stream().collect(Collectors.toList());
				
			}
			
		}else {
			
			throw new GeneralizedException(ErrorCode.TELEMETRY_RECORDS_NOT_FOUND.getReasonPhrase(),
					ErrorCode.TELEMETRY_RECORDS_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		return driverScoreResponses;
		
	}

	public List<Telemetry> getTelemetryRecordsBasedOnParamTypeOld(Integer driverId, String parameterType, String startTime, String endTime){
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd_HH:mm:ss").createDateTimeFormatter();

//		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd").createDateTimeFormatter();
//		LocalDate startDate = LocalDate.parse(startTime, dateTimeFormatter);
//		LocalDate endDate = LocalDate.parse(endTime, dateTimeFormatter);
//		LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
//		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
		
		LocalDateTime startTimeLocal = LocalDateTime.parse(startTime, dateTimeFormatter);
		LocalDateTime endTimeLocal = LocalDateTime.parse(endTime, dateTimeFormatter);
		
		return telemetryRepository.findByDriverAndParameterAndTimeBetween(new Driver(driverId, null, null, null, null, null), parameterType, startTimeLocal, endTimeLocal);
//		return null;
		
	}

	public TelemetryResponseList getTelemetryRecordsBasedOnParamType(String parameterType, String startTime, String endTime, Integer page){
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd_HH:mm:ss").createDateTimeFormatter();
		
//		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd").createDateTimeFormatter();
//		LocalDate startDate = LocalDate.parse(startTime, dateTimeFormatter);
//		LocalDate endDate = LocalDate.parse(endTime, dateTimeFormatter);
//		LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
//		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
		
		LocalDateTime startTimeLocal = LocalDateTime.parse(startTime, dateTimeFormatter);
		LocalDateTime endTimeLocal = LocalDateTime.parse(endTime, dateTimeFormatter);
		
		Integer pageSize = Integer.parseInt(environment.getProperty(VehicleEnum.PAGE_SIZE, "5"));
		
		Pageable pageable = PageRequest.of(page == null ? 0 : page, pageSize);
		
		Page<Telemetry> telemetries = telemetryRepository.findByParameterAndTimeBetween(parameterType, startTimeLocal, endTimeLocal, pageable);
		
		TelemetryResponseList telemetryResponseList = new TelemetryResponseList();
		
		Pagination pagination = new Pagination(telemetries.getTotalPages(), telemetries.getTotalElements(),
				telemetries.getNumber());

		List<TelemetryResponse> telemetryList = telemetries.getContent().stream().map(telemetry -> new TelemetryResponse(telemetry))
				.collect(Collectors.toList());
		
		telemetryResponseList.setTelemetryList(telemetryList);
		telemetryResponseList.setPagination(pagination);
		
		return telemetryResponseList;
		
	}

	public List<Telemetry> getTelemetryRecordsBasedOnDriverId(Integer driverId, String startTime, String endTime) {
		
		DateTimeFormatter dateTimeFormatter = new DateTimeFormatterFactory("yyyy-MM-dd_HH:mm:ss").createDateTimeFormatter();
		
		LocalDateTime startTimeLocal = LocalDateTime.parse(startTime, dateTimeFormatter);
		LocalDateTime endTimeLocal = LocalDateTime.parse(endTime, dateTimeFormatter);
		
		List<Telemetry> telemetries = telemetryRepository
				.findByDriverAndTimeBetween(new Driver(driverId, null, null, null, null, null), startTimeLocal, endTimeLocal);
		
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String formattedDate = simpleDateFormat.format(telemetries.get(0).getTime());

		DateTimeFormatter dateTimeFormatter1 = new DateTimeFormatterFactory("yyyy-MM-dd HH:mm:ss").createDateTimeFormatter();

		telemetries.forEach(tel -> tel.setTimestamp(tel.getTime().format(dateTimeFormatter1)));
		
		return telemetries;
		
		
		
		
	}
}
