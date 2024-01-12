package com.vehicleservice.constants;

public class VehicleEnum {
	
	public static final String SPEED_LIMIT = "alert-configuration.speed-limit";

	public static final String PAGE_SIZE = "custom.page-size";
	
	public static final String[] BOOLEAN_FIELDS = {"seatbelt", "night_drive", "traffic_violation"};

	public static final String CURRENT_SPEED = "current_speed";

	public static final String DISTANCE = "distance";

	public static final String MILEAGE = "mileage";
	
	public static final String[] POSITIVE_FIELDS = {"current_speed", "distance", "seatbelt", "mileage"};

	public static final String[] NEGATIVE_FIELDS = {"fast_acceleration", "ac_misusage", "heavy_braking", "night_drive", "traffic_violation"};
	
	public static final String[] TELEMETRY_FIELDS = { "current_speed", "distance", "seatbelt", "mileage",
			"fast_acceleration", "ac_misusage", "heavy_braking", "night_drive", "traffic_violation" };	
	
	
	/*
	current_speed	5-200
	distance		5-200
	mileage			5-50
	fast_acceleration	1-10
	ac_misusage			1-10
	heavy_braking		1-10
	seatbelt			0 or 1
	night_drive			0 or 1
	traffic_violation	0 or 1
	
	
	*/
	
	public static final String[] TEN_POINT_FIELDS = {"fast_acceleration", "ac_misusage", "heavy_braking"};
	
	public static final String TELEMETRY_TIMESTAMP = "TELEMETRY_TIMESTAMP";

	public static final String CREATE = "CREATE";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";

	public static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static final String EXCEL_SHEET_NAME_UPLOAD = "custom.excel-upload-config.vehicle-sheet-name";
	
	//Excel Export
	public static final String VEHICLE_EXCEL_COLUMN_DOWNLOAD = "custom.excel-download-config.vehicle-column-list";

	public static final String VEHICLE_EXCEL_SHEET_NAME = "custom.excel-download-config.vehicle-sheet-name";
	
	public static final String VEHICLE_EXCEL_FILE_NAME = "custom.excel-download-config.file-name";
	
}
