package com.vehicleservice.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.vehicleservice.constants.VehicleEnum;
import com.vehicleservice.constants.VehicleStatus;
import com.vehicleservice.entity.vehicleDriver.Fleet;
import com.vehicleservice.entity.vehicleDriver.Vehicle;

@Component
public class ExcelHelper {

	@Autowired
	private Environment environment;

	public boolean hasExcelFormat(MultipartFile file) {

		if (!VehicleEnum.EXCEL_CONTENT_TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}
	
	public Map<String, List<Vehicle>> excelToVehicles(MultipartFile multipartFile) {
		
		Map<String, List<Vehicle>> actionVehicleMap = new HashMap<>();
		actionVehicleMap.put(VehicleEnum.CREATE, new ArrayList<>());
		actionVehicleMap.put(VehicleEnum.UPDATE, new ArrayList<>());
		actionVehicleMap.put(VehicleEnum.DELETE, new ArrayList<>());
		
		try {
			Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());

			Sheet sheet = workbook.getSheet(environment.getProperty(VehicleEnum.EXCEL_SHEET_NAME));
			Iterator<Row> rows = sheet.iterator();

			List<Vehicle> vehicles = new ArrayList<Vehicle>();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				int lastCellNum = currentRow.getLastCellNum();
				
				Vehicle vehicle = new Vehicle();

				String action = null;
				boolean skipRow = false;
				
				
				for (int currentCellIndex = 0; currentCellIndex < lastCellNum; currentCellIndex++) {
					
					if(skipRow)
						break;
					
					Cell currentCell = currentRow.getCell(currentCellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					
					if(currentCell.getCellType() == CellType.BLANK && 
							currentCellIndex != 0) {
						continue;
					}
					
					switch (currentCellIndex) {
					case 0:
						System.out.println("Action: " + currentCell.getStringCellValue());
						action = currentCell.getStringCellValue();
						
						if (action == null || (!action.equalsIgnoreCase("D") && !action.equalsIgnoreCase("U")
								&& !action.equalsIgnoreCase("C")))
							skipRow = true;
						
						break;

					case 1:
//						if (currentCell.getStringCellValue() != null || !currentCell.getStringCellValue().isBlank())
							vehicle.setId((int) currentCell.getNumericCellValue());
							
						break;

					case 2:
						vehicle.setModel(currentCell.getStringCellValue());
						break;

					case 3:
						vehicle.setRegistrationNumber(currentCell.getStringCellValue());
						break;

					case 4:
						vehicle.setStatus(currentCell.getStringCellValue() != null && !currentCell.getStringCellValue().isBlank() 
								? VehicleStatus.of(currentCell.getStringCellValue()) : VehicleStatus.NEW);
						break;

					case 5:
						vehicle.setStyle(currentCell.getStringCellValue());
						break;
						
					case 6:
						System.out.println("Route: " + currentCell.getStringCellValue());
						vehicle.setFleet(new Fleet(0, 0, currentCell.getStringCellValue()));
						break;

					default:
						break;
					}
					
				}

				if(!skipRow) {
					
					if (action.equalsIgnoreCase("D"))
						actionVehicleMap.get(VehicleEnum.DELETE).add(vehicle);
					else if (action.equalsIgnoreCase("U"))
						actionVehicleMap.get(VehicleEnum.UPDATE).add(vehicle);
					else if (action.equalsIgnoreCase("C"))
						actionVehicleMap.get(VehicleEnum.CREATE).add(vehicle);
					
				}
			}

			workbook.close();

			return actionVehicleMap;
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
		}
	}
	
}
