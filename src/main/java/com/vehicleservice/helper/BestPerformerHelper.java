package com.vehicleservice.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.vehicleservice.constants.VehicleEnum;
import com.vehicleservice.entity.vehicleDriver.Telemetry;

public class BestPerformerHelper {
	
	static List<String> booleanFieldsList = new ArrayList<>();
	static List<String> positiveFieldsList = new ArrayList<>();
	static List<String> negativeFieldsList = new ArrayList<>();
	
//	@PostConstruct
	static {
		booleanFieldsList = Arrays.asList(VehicleEnum.BOOLEAN_FIELDS);
		positiveFieldsList = Arrays.asList(VehicleEnum.POSITIVE_FIELDS);
		negativeFieldsList = Arrays.asList(VehicleEnum.NEGATIVE_FIELDS);
	}
	
	public static Map<Integer, Double> calculateIndividualScores(Map<Integer, List<Telemetry>> driverMap) {
		
		Map<Integer, Double> scoreMap = driverMap.keySet().stream()
				.collect(Collectors.toMap(key -> key, key -> BestPerformerHelper.calculateScore(driverMap.get(key))));
//				.collect(Collectors.toMap(Function.identity(), key -> BestPerformerHelper.calculateScore(driverMap.get(key))));
		
		return scoreMap;
		
	}
	
	public static Entry<Integer, Double> calculateBestPerformer(Map<Integer, List<Telemetry>> driverMap) {
		
		Map<Integer, Double> scoreMap = driverMap.keySet().stream()
				.collect(Collectors.toMap(key -> key, key -> BestPerformerHelper.calculateScore(driverMap.get(key))));
//				.collect(Collectors.toMap(Function.identity(), key -> BestPerformerHelper.calculateScore(driverMap.get(key))));
		
		Optional<Entry<Integer,Double>> maxScore = scoreMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue));
		
		if(maxScore.isPresent())
			return maxScore.get();
		else
			return null;
		
	}
	
	private static Double calculateScore(List<Telemetry> listOfTelemetries) {
		
//		current_speed
//		<30		10
//		31-60	8
//		61-100	6
//		101-120	4
//		>121	2
		
//		 distance
//		  0 - 25 -> 2
//		  26 - 50 -> 5
//		  51 - 75 -> 7
//		  76 - 100 -> 9
//		  > 100 -> 10

		Double positiveValue = 0.0;
		Double negativeValue = 0.0;
		
		for(Telemetry telemetry : listOfTelemetries) {
			Double normalizedValue = 0.0;
			String parameterType = telemetry.getParameter();
			
			if(booleanFieldsList.contains(parameterType)) {
				
				if(parameterType.equalsIgnoreCase("seatbelt")) {
					normalizedValue = telemetry.getParamValue() == 1.0 ? 10.0 : 0.0;
					
				}else if(parameterType.equalsIgnoreCase("night_drive")) {
					normalizedValue = telemetry.getParamValue() == 1.0 ? 10.0 : 0.0;
					
				}else if(parameterType.equalsIgnoreCase("traffic_violation")) {
					normalizedValue = telemetry.getParamValue() == 1.0 ? 10.0 : 0.0;
					
				}
				
			}else if(VehicleEnum.CURRENT_SPEED.equalsIgnoreCase(parameterType)) {
				
				Double speed = telemetry.getParamValue();
				
				normalizedValue = (speed <= 30) ? 10.0 :
									(speed > 30 && speed <= 60) ? 8.0 :
										(speed > 60 && speed <= 100) ? 6.0 :
											(speed > 100 && speed <= 120) ? 4.0 : 2.0;
				
			}else if(VehicleEnum.DISTANCE.equalsIgnoreCase(parameterType)) {
				
				Double distance = telemetry.getParamValue();
				
				normalizedValue = (distance <= 0) ? 0 :
									(distance > 0 && distance <= 25) ? 2.0 :
										(distance > 25 && distance <= 50) ? 5.0 :
											(distance > 50 && distance <= 75) ? 7.0 :
												(distance > 75 && distance <= 100) ? 9.0 : 10.0;
				
			}else {
				normalizedValue = telemetry.getParamValue();
				
			}
			
			if(positiveFieldsList.contains(parameterType)) {
				positiveValue = positiveValue + normalizedValue;
				
			}else if(negativeFieldsList.contains(parameterType)) {
				negativeValue = negativeValue + normalizedValue;
				
			}
			
		}
		return positiveValue - negativeValue;
		
	}
	
}
