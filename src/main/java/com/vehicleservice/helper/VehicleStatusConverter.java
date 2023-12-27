package com.vehicleservice.helper;

import com.vehicleservice.constants.VehicleStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VehicleStatusConverter implements AttributeConverter<VehicleStatus, String> {
	
	@Override
    public String convertToDatabaseColumn(VehicleStatus vehicleStatus) {
		
        if (vehicleStatus == null) {
            return null;
        }
        return vehicleStatus.getStatusCode();
    }

    @Override
    public VehicleStatus convertToEntityAttribute(String vehicleStatusCode) {

    	if (vehicleStatusCode == null) {
            return null;
        }

        return VehicleStatus.of(vehicleStatusCode);
    }
	
}
