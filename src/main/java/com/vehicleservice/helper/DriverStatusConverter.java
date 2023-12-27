package com.vehicleservice.helper;

import com.vehicleservice.constants.DriverStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DriverStatusConverter implements AttributeConverter<DriverStatus, String> {
	
	@Override
    public String convertToDatabaseColumn(DriverStatus driverStatus) {
		
        if (driverStatus == null) {
            return null;
        }
        return driverStatus.getStatusCode();
    }

    @Override
    public DriverStatus convertToEntityAttribute(String driverStatusCode) {

    	if (driverStatusCode == null) {
            return null;
        }

        return DriverStatus.of(driverStatusCode);
    }
	
}
