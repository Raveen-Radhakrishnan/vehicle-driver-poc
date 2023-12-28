package com.vehicleservice.service.vehicleDriver;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vehicleservice.constants.DriverStatus;
import com.vehicleservice.constants.VehicleEnum;
import com.vehicleservice.constants.VehicleStatus;
import com.vehicleservice.entity.vehicleDriver.Driver;
import com.vehicleservice.entity.vehicleDriver.Vehicle;
import com.vehicleservice.exception.DriverNotFoundException;
import com.vehicleservice.exception.ErrorCode;
import com.vehicleservice.exception.GeneralizedException;
import com.vehicleservice.repository.vehicleDriver.DriverRepository;
import com.vehicleservice.request.DriverMassUpdateRequest;
import com.vehicleservice.request.DriverRequest;
import com.vehicleservice.response.DriverResponse;
import com.vehicleservice.response.DriverResponseList;
import com.vehicleservice.response.Pagination;
import com.vehicleservice.response.VehicleResponse;
import com.vehicleservice.response.VehicleResponseList;

import jakarta.validation.Valid;

@Service
public class DriverService {

	@Autowired
	DriverRepository driverRepository;
	
	@Autowired
	private Environment environment;

	public DriverResponse addDriver(DriverRequest driverRequest) {

//		try {
			Optional<Driver> savedDriver = driverRepository.findByLicenseNumber(driverRequest.getLicenseNumber());
			
			if(savedDriver.isPresent())
				throw new GeneralizedException(ErrorCode.DUPLICATE_LICENCE_NUMBER.getReasonPhrase(),
						ErrorCode.DUPLICATE_LICENCE_NUMBER, HttpStatus.BAD_REQUEST);
			
			
			Driver driver = driverRepository.save(new Driver(driverRequest));
			
			return new DriverResponse(driver);
			
//		} catch (GeneralizedException e) {
//
//			throw new GeneralizedException("Error while saving driver", ErrorCode.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//		}

	}

	public List<DriverResponse> getAllDriversOld() {

		List<Driver> drivers = driverRepository.findAll();
		
		List<DriverResponse> driverList = drivers.stream().map(driver -> new DriverResponse(driver)).collect(Collectors.toList());
		
		return driverList;

	}

	public DriverResponseList getAllDrivers(Integer page) {
		
		Integer pageSize = Integer.parseInt(environment.getProperty(VehicleEnum.PAGE_SIZE, "5"));
		
		Pageable pageable = PageRequest.of(page == null ? 0 : page, pageSize);
		
		Page<Driver> drivers = driverRepository.findAll(pageable);

		DriverResponseList driverResponseList = new DriverResponseList();
		
		Pagination pagination = new Pagination(drivers.getTotalPages(), drivers.getTotalElements(),
				drivers.getNumber());

		List<DriverResponse> driverList = drivers.getContent().stream().map(driver -> new DriverResponse(driver))
				.collect(Collectors.toList());
		
		driverResponseList.setDriverList(driverList);
		driverResponseList.setPagination(pagination);
		
		return driverResponseList;
		
	}

	public DriverResponse getDriverById(int id) {
		
		Optional<Driver> driverOptional = driverRepository.findById(id);
		
		if(driverOptional.isPresent()) {
			
			Driver driver = driverOptional.get();
			
			return new DriverResponse(driver);
		
		}else {
			
			throw new DriverNotFoundException("Driver with id " + id + " not found");
		}
		
//		return driver.orElse(new Driver());
		
	}
	
	public boolean deleteDriverById(Integer driverId) {
		
		Optional<Driver> driverOptional = driverRepository.findById(driverId);
		
		if(driverOptional.isPresent()) {
			
			driverRepository.deleteById(driverId);
			return true;
		}
		return false;
		
	}

	public DriverResponse updateDriver(@Valid DriverRequest driverRequest) {
		
		Optional<Driver> driverOptional = driverRepository.findByLicenseNumber(driverRequest.getLicenseNumber());
		
		if(!driverOptional.isPresent())
			throw new GeneralizedException(ErrorCode.DRIVER_NOT_FOUND.getReasonPhrase(), ErrorCode.DRIVER_NOT_FOUND, HttpStatus.BAD_REQUEST);
		
		boolean updateDriver = false;
		
		Driver driver = driverOptional.get();
		
		if(driverRequest.getName() != null && !driverRequest.getName().equals(driver.getName())) {
			
			driver.setName(driverRequest.getName());
			updateDriver = true;
		}
		if(driverRequest.getAddress() != null && !driverRequest.getAddress().equals(driver.getAddress())) {
			
			driver.setAddress(driverRequest.getAddress());
			updateDriver = true;
		}
		if(driverRequest.getPhoneNumber() != null && !driverRequest.getPhoneNumber().equals(driver.getPhoneNumber())) {
			
			driver.setPhoneNumber(driverRequest.getPhoneNumber());
			updateDriver = true;
		}
		if (driverRequest.getStatus() != null
				&& DriverStatus.of(driverRequest.getStatus()).compareTo(driver.getStatus()) != 0) {
			
			driver.setStatus(DriverStatus.of(driverRequest.getStatus()));
			updateDriver = true;
		}
		
		if(updateDriver)
			return new DriverResponse(driverRepository.save(driver));
		else
			return new DriverResponse(driver);
		
	}

	public List<DriverResponse> massUpdateDriver(@Valid DriverMassUpdateRequest driverMassUpdateRequest) {
		
		List<Driver> listOfDrivers = driverRepository.findAllById(driverMassUpdateRequest.getDriverIdList());

		if (listOfDrivers == null || listOfDrivers.isEmpty())
			throw new GeneralizedException(ErrorCode.DRIVER_NOT_FOUND.getReasonPhrase(), ErrorCode.DRIVER_NOT_FOUND,
					HttpStatus.BAD_REQUEST);

		listOfDrivers.forEach(driver -> driver.setStatus(DriverStatus.of(driverMassUpdateRequest.getStatus())));
		
		List<Driver> savedDrivers = driverRepository.saveAll(listOfDrivers);
		
		List<DriverResponse> driverList = savedDrivers.stream().map(driver -> new DriverResponse(driver))
				.collect(Collectors.toList());
		
		return driverList;
		
	}

	

}
