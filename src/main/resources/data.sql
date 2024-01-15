INSERT INTO fleet (count, route)
VALUES (1, 'PUNE'),
(3, 'MUMBAI'),
(3, 'KOCHI'),
(1, 'KOLKATA'),
(1, 'DELHI'),
(1, 'BANGALORE'),
(1, 'KASHMIR');

INSERT INTO vehicle (fleet_id, registration_number, model, style, status)
VALUES (1, 'MH 12 AA 23', 'Mustang', 'Sports', 'N'),
(2, 'MH 01 AA 1', 'Fiesta', 'Car', 'N'),
(3, 'KL 09 AB 1265', 'F150', 'Truck', 'N'),
(3, 'KL 49 CB 344', 'Figo', 'Car', 'N'),
(4, 'WB 01 WE 4512', 'Fiesta', 'Car', 'N'),
(5, 'DL 01 QW 548', 'Fortuner', 'SUV', 'N'),
(6, 'KA 01 TR 32', 'Aspire', 'Car', 'N'),
(2, 'MH 47 HI 5', 'Ecosport', 'MUV', 'N'),
(3, 'DL 20 NN 9421', 'F150', 'Truck', 'N'),
(7, 'JK 01 RR 2475', 'Mustang', 'Sports', 'N');

INSERT INTO driver (name, address, license_number, phone_number, status)
VALUES ('John', 'Pune', 'MH01 12345678901', '+916234567890', 'D'),
('Tom', 'Mumbai', 'MH02 12345678902', '+917987654321', 'D'),
('Peter', 'Kolkata', 'MH03 12345678903', '+918567891230', 'D'),
('Tommy', 'Kochi', 'MH04 12345678904', '+919418529630', 'D'),
('Bruce', 'Chennai', 'MH05 12345678905', '+916749512360', 'D'),
('Jack', 'Tokyo', 'MH06 12345678906', '+917651316465', 'D'),
('Mark', 'London', 'MH07 12345678907', '+918654867532', 'D'),
('Wong', 'Beiging', 'MH08 12345678908', '+919546875231', 'D'),
('Bob', 'Bangalore', 'MH09 12345678909', '+918564554135', 'D'),
('Don', 'Delhi', 'MH10 12345678910', '+919857714572', 'D');

INSERT INTO telemetry (driver_id, vehicle_id, parameter, param_value, time)
VALUES 
(1, 1, 'current_speed', 100, CURRENT_TIMESTAMP-0.012),
(1, 1, 'current_speed', 96, CURRENT_TIMESTAMP-0.006),
(1, 1, 'current_speed', 72, CURRENT_TIMESTAMP),
(1, 1, 'current_speed', 81, CURRENT_TIMESTAMP+0.006),
(1, 1, 'current_speed', 89, CURRENT_TIMESTAMP+0.012),
(2, 2, 'current_speed', 50, CURRENT_TIMESTAMP-0.012),
(2, 2, 'current_speed', 40, CURRENT_TIMESTAMP-0.006),
(2, 2, 'current_speed', 45, CURRENT_TIMESTAMP),
(2, 2, 'current_speed', 78, CURRENT_TIMESTAMP+0.006),
(2, 2, 'current_speed', 63, CURRENT_TIMESTAMP+0.012),
(2, 2, 'distance', 150, CURRENT_TIMESTAMP),
(3, 3, 'current_speed', 20, CURRENT_TIMESTAMP-0.012),
(3, 3, 'current_speed', 29, CURRENT_TIMESTAMP-0.006),
(3, 3, 'current_speed', 42, CURRENT_TIMESTAMP),
(3, 3, 'current_speed', 55, CURRENT_TIMESTAMP+0.006),
(3, 3, 'current_speed', 70, CURRENT_TIMESTAMP+0.012),
(4, 4, 'seatbelt', 0, CURRENT_TIMESTAMP),
(2, 2, 'fast_acceleration', 5, CURRENT_TIMESTAMP),
(4, 4, 'heavy_braking', 8, CURRENT_TIMESTAMP),
(3, 3, 'night_drive', 1, CURRENT_TIMESTAMP),
(1, 1, 'traffic_violation', 0, CURRENT_TIMESTAMP),
(2, 2, 'ac_misusage', 9, CURRENT_TIMESTAMP),
(4, 4, 'current_speed', 120, CURRENT_TIMESTAMP-0.012),
(4, 4, 'current_speed', 111, CURRENT_TIMESTAMP-0.006),
(4, 4, 'current_speed', 101, CURRENT_TIMESTAMP),
(4, 4, 'current_speed', 93, CURRENT_TIMESTAMP+0.006),
(4, 4, 'current_speed', 75, CURRENT_TIMESTAMP+0.012),
(5, 5, 'current_speed', 35, CURRENT_TIMESTAMP-0.012),
(5, 5, 'current_speed', 44, CURRENT_TIMESTAMP-0.006),
(5, 5, 'current_speed', 50, CURRENT_TIMESTAMP),
(5, 5, 'current_speed', 40, CURRENT_TIMESTAMP+0.006),
(5, 5, 'current_speed', 22, CURRENT_TIMESTAMP+0.012),
(5, 5, 'distance', 500, CURRENT_TIMESTAMP),
(5, 5, 'fast_acceleration', 6, CURRENT_TIMESTAMP);



INSERT INTO central_configs (CONFIG_KEY) VALUES ('TELEMETRY_TIMESTAMP');
