DROP TABLE IF EXISTS trip_details;
DROP TABLE IF EXISTS outlet_master;
DROP TABLE IF EXISTS depo_master;
DROP TABLE IF EXISTS slab_master;

DROP TABLE IF EXISTS contractor_vehicle_details;
DROP TABLE IF EXISTS contractor_master;

CREATE TABLE depo_master (
    depo_name VARCHAR(20),
    district VARCHAR(20),
    depo_code INT,
    last_update_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (depo_name)
);
INSERT INTO depo_master (depo_name, district, depo_code) VALUES ('Chagallu', 'West Godavari', 1);
INSERT INTO depo_master (depo_name, district, depo_code) VALUES ('Eluru', 'West Godavari', 2);
INSERT INTO depo_master (depo_name, district, depo_code) VALUES ('Bhimavaram', 'West Godavari', 3);

CREATE TABLE slab_master (
    slab_code VARCHAR(10),
    range_min INT,
    range_max INT,
    last_update_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (slab_code)
);
INSERT INTO slab_master (slab_code, range_min, range_max) VALUES ('Slab - 1', 0, 20);
INSERT INTO slab_master (slab_code, range_min, range_max) VALUES ('Slab - 2', 21, 40);
INSERT INTO slab_master (slab_code, range_min, range_max) VALUES ('Slab - 3', 41, 60);
INSERT INTO slab_master (slab_code, range_min, range_max) VALUES ('Slab - 4', 61, 80);
INSERT INTO slab_master (slab_code, range_min, range_max) VALUES ('Slab - 5', 81, 100);
INSERT INTO slab_master (slab_code, range_min, range_max) VALUES ('Slab - 6', 101, 120);
INSERT INTO slab_master (slab_code, range_min, range_max) VALUES ('Slab - 7', 121, 140);

CREATE TABLE outlet_master (
    outlet_code VARCHAR(10),
    outlet_address VARCHAR(100),
    station VARCHAR(20),
    depo_name VARCHAR(20),
    km_from_depo DOUBLE,
    last_update_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (outlet_code),
    CONSTRAINT fk_depo_outlet FOREIGN KEY (depo_name)
        REFERENCES depo_master (depo_name)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE TABLE trip_details (
	id INT auto_increment,
    date DATE NOT NULL,
    outlet_code VARCHAR(10) NOT NULL,
    vehicle_number VARCHAR(12) NOT NULL,
    num_cases_imfl INT NOT NULL,
    num_cases_beer INT NOT NULL,
    form_3 INT NOT NULL,
    last_update_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    primary key(id),
    CONSTRAINT fk_outlet_trip FOREIGN KEY (outlet_code)
        REFERENCES outlet_master (outlet_code)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE TABLE contractor_master (
    contractor_code VARCHAR(10),
    contractor_name VARCHAR(50) NOT NULL,
    contractor_email VARCHAR(50) NULL,
    last_update_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (contractor_code)
);
INSERT INTO contractor_master (contractor_code, contractor_name, contractor_email) VALUES
('KRSVL', 'K R S V Lakshmi', 'klakshmi75@gmail.com'),
('HK', 'Harshita Kalidindi', 'kharshita96@gmail.com');

CREATE TABLE contractor_vehicle_details (
	id INT auto_increment,
    contractor_code VARCHAR(10) NOT NULL,
    vehicle_number VARCHAR(12) NOT NULL,
    date DATE NOT NULL,
    last_update_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    primary key(id),
    UNIQUE KEY (vehicle_number , contractor_code , date),
    CONSTRAINT fk_contractor_vehicle FOREIGN KEY (contractor_code)
        REFERENCES contractor_master (contractor_code)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);


