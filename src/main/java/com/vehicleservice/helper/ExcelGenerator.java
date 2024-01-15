package com.vehicleservice.helper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.vehicleservice.constants.VehicleEnum;
import com.vehicleservice.constants.VehicleStatus;
import com.vehicleservice.response.VehicleResponse;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExcelGenerator {
	
	@Autowired
	private Environment environment;
	
	public void exportToExcel(HttpServletResponse httpServletResponse, List<VehicleResponse> vehicleResponses,
			String fileName, String sheetname, String columnList) throws IOException {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		httpServletResponse = setResponseForExcel(httpServletResponse, fileName);
		
		List<String> headerColumns = getHeaderColumns(columnList);
		
		writeTableHeader(workbook, sheetname, headerColumns);

		writeTableData(workbook, sheetname, vehicleResponses);
		
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		
		workbook.write(outputStream);
		
		workbook.close();
		
		outputStream.close();
		
	}

	private void writeTableData(XSSFWorkbook workbook, String sheetname, List<VehicleResponse> vehicleResponses) {
		
		XSSFSheet sheet = workbook.getSheet(sheetname);
		
		int rowCounter = 1;
		
		for(VehicleResponse vehicleResponse : vehicleResponses) {
			
			XSSFRow row = sheet.createRow(rowCounter);
			
			int columnCount = 1;
			
			createCell(row, columnCount++, vehicleResponse.getId(), null);
			createCell(row, columnCount++,
					vehicleResponse.getModel() != null ? vehicleResponse.getModel() : CellType.BLANK, null);
			createCell(row, columnCount++, vehicleResponse.getRegistrationNumber(), null);
			createCell(row, columnCount++, vehicleResponse.getStatus().getStatusCode(), null);
			createCell(row, columnCount++,
					vehicleResponse.getStyle() != null ? vehicleResponse.getStyle() : CellType.BLANK, null);
			createCell(row, columnCount++,
					vehicleResponse.getFleet() != null ? vehicleResponse.getFleet().getRoute() : CellType.BLANK, null);

			rowCounter++;
		}
		
	}

	private void writeTableHeader(XSSFWorkbook workbook, String sheetname, List<String> headerColumns) {
		
		XSSFSheet sheet = workbook.createSheet(sheetname);
		
		XSSFRow row = sheet.createRow(0);
		
		for(int i = 0; i < headerColumns.size(); i++) {
			
			createCell(row, i, headerColumns.get(i), null);
			
		}
		
	}
	
	private void createCell(Row row, int columnCount, Object cellValue, CellStyle style) {
		
//        sheet.autoSizeColumn(columnCount);
        
        Cell cell = row.createCell(columnCount);
        
        if (cellValue instanceof Integer value) {
            cell.setCellValue(value);
            
        } else if (cellValue instanceof Double value) {
            cell.setCellValue(value);
            
        } else if (cellValue instanceof Boolean value) {
            cell.setCellValue(value);
            
        } else if (cellValue instanceof Long value) {
            cell.setCellValue(value);

		} else {
			cell.setCellValue((String) cellValue);
		}
//        cell.setCellStyle(style);
    }

	private List<String> getHeaderColumns(String columnList) {

		String[] columnsArray = environment.getProperty(columnList).trim().split(",");

		List<String> columns = Arrays.asList(columnsArray);

		columns.forEach(column -> column.trim().toUpperCase());

		return columns;
	}

	private HttpServletResponse setResponseForExcel(HttpServletResponse response, String fileName) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        return response;
    }
			
	
	
}
