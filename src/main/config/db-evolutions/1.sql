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
    PRIMARY KEY (depo_name)
);
INSERT INTO depo_master (depo_name, district, depo_code) VALUES ('Chagallu', 'West Godavari', 1);
INSERT INTO depo_master (depo_name, district, depo_code) VALUES ('Eluru', 'West Godavari', 2);
INSERT INTO depo_master (depo_name, district, depo_code) VALUES ('Bhimavaram', 'West Godavari', 3);

CREATE TABLE slab_master (
    slab_code VARCHAR(10),
    range_min INT,
    range_max INT,
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
    PRIMARY KEY (outlet_code),
    CONSTRAINT fk_depo_outlet FOREIGN KEY (depo_name)
        REFERENCES depo_master (depo_name)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);
INSERT INTO outlet_master (outlet_code, outlet_address, station, depo_name, km_from_depo) VALUES
('5014', 'D.NO.11-4/1, Chagallu Village, Chagallu Mandal','Kovvur','Chagallu',2),
('5024', 'D.NO.1-25-6, ward no 14, Veerabhadra puram, Tanuku','Tanuku','Bhimavaram',35),
('5026', 'D.No.12-29-40, ward no 2, Ganesh Chowk, Tanuku','Tanuku','Bhimavaram',36),
('5030', 'D.No.10-175/1, vendra vari veedhi, Iraga varam main road, Tanuku','Tanuku','Bhimavaram',45),
('5043', 'D.No.11-35, Highway red bridge road, Mukkamala vari veedhi, Penugonda','Penugonda','Bhimavaram',40),
('5120', 'D.No.5-35, Polamuru village, Penumantra mandal','Penugonda','Bhimavaram',50),
('5143', 'D.No.129,  Bypass Road, Janga reddyGudem, W.G.Dt','J.R.Gudem','Eluru',45),
('5166', 'D.No.4-2-7/1, ward no.22, Kovvuru Municipality','Kovvur','Chagallu',12),
('5168', 'D.No.6-28, Vadapalli Village, Kovvuru','Kovvur','Chagallu',15),
('5172', 'D.No.1-212/5, Thimmarajupalem (V), Nidadavole Rural mandal','Kovvur','Chagallu',16);

CREATE TABLE trip_details (
	id INT auto_increment,
    date DATE NOT NULL,
    outlet_code VARCHAR(10) NOT NULL,
    vehicle_number VARCHAR(12) NOT NULL,
    num_cases_imfl INT NOT NULL,
    num_cases_beer INT NOT NULL,
    form_3 INT NOT NULL,
    primary key(id),
    CONSTRAINT fk_outlet_trip FOREIGN KEY (outlet_code)
        REFERENCES outlet_master (outlet_code)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);
INSERT INTO trip_details (date, outlet_code, vehicle_number,num_cases_imfl,num_cases_beer,form_3) VALUES
('2020-11-02', '5014', 'AP37Y9689', 0, 80, 0),
('2020-11-02', '5024', 'AP37TF0669', 108, 40, 0),
('2020-11-02', '5026', 'AP37TF0669', 105, 80, 100),
('2020-11-02', '5030', 'AP37TF0669', 110, 40, 0),
('2020-11-02', '5043', 'AP37TF0669', 106, 50, 230),
('2020-11-02', '5120', 'AP37TF0669', 0, 80, 0),
('2020-11-02', '5143', 'AP07TD5314', 0, 110, 850),
('2020-11-02', '5166', 'AP37TC1077', 105, 95, 0),
('2020-11-02', '5168', 'AP37Y9689', 108, 35, 0),
('2020-11-02', '5172', 'AP12V3042', 0, 100, 0),
('2020-11-03', '5014', 'AP37Y9689', 0, 60, 0),
('2020-11-03', '5024', 'AP37TF0669', 108, 50, 0),
('2020-11-03', '5026', 'AP37TF0669', 105, 85, 100),
('2020-11-03', '5030', 'AP37TF0669', 110, 43, 0),
('2020-11-03', '5043', 'AP37TF0669', 106, 52, 230),
('2020-11-03', '5120', 'AP37TF0669', 0, 85, 0),
('2020-11-03', '5143', 'AP07TD5314', 0, 115, 850),
('2020-11-03', '5166', 'AP37TC1077', 105, 98, 0),
('2020-11-03', '5168', 'AP37Y9689', 108, 36, 20),
('2020-11-03', '5172', 'AP12V3042', 0, 101, 0),
('2020-11-04', '5014', 'AP37Y9689', 0, 65, 0),
('2020-11-04', '5024', 'AP37TF0669', 108, 54, 10),
('2020-11-04', '5026', 'AP37TF0669', 105, 86, 100),
('2020-11-04', '5030', 'AP37TF0669', 110, 42, 0),
('2020-11-04', '5043', 'AP37TF0669', 106, 51, 220),
('2020-11-04', '5120', 'AP37TF0669', 0, 84, 0),
('2020-11-04', '5143', 'AP07TD5314', 0, 116, 820),
('2020-11-04', '5166', 'AP37TC1077', 105, 96, 0),
('2020-11-04', '5168', 'AP37Y9689', 108, 35, 0),
('2020-11-04', '5172', 'AP12V3042', 0, 100, 0);

CREATE TABLE contractor_master (
    contractor_code VARCHAR(10),
    contractor_name VARCHAR(50) NOT NULL,
    contractor_email VARCHAR(50) NULL,
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
    primary key(id),
    UNIQUE KEY (vehicle_number , contractor_code , date),
    CONSTRAINT fk_contractor_vehicle FOREIGN KEY (contractor_code)
        REFERENCES contractor_master (contractor_code)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);
INSERT INTO contractor_vehicle_details (contractor_code, vehicle_number, date) VALUES
('KRSVL', 'AP37Y9689', '2020-11-02'),
('KRSVL', 'AP37TF0669', '2020-11-02'),
('KRSVL', 'AP07TD5314', '2020-11-02'),
('HK', 'AP37TC1077', '2020-11-02'),
('HK', 'AP12V3042', '2020-11-02'),
('KRSVL', 'AP37Y9689', '2020-11-03'),
('HK', 'AP37TF0669', '2020-11-03'),
('KRSVL', 'AP07TD5314', '2020-11-03'),
('HK', 'AP37TC1077', '2020-11-03'),
('HK', 'AP12V3042', '2020-11-03'),
('KRSVL', 'AP37Y9689', '2020-11-04'),
('KRSVL', 'AP37TF0669', '2020-11-04'),
('HK', 'AP07TD5314', '2020-11-04'),
('HK', 'AP37TC1077', '2020-11-04'),
('HK', 'AP12V3042', '2020-11-04');

