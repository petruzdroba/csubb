DROP TABLE IF EXISTS TrailFacilities;
DROP TABLE IF EXISTS TrailHazards;
DROP TABLE IF EXISTS TrailTags;
DROP TABLE IF EXISTS ElevationPoints;
DROP TABLE IF EXISTS Conditions;
DROP TABLE IF EXISTS MaintenanceLogs;
DROP TABLE IF EXISTS Photos;
DROP TABLE IF EXISTS Checkpoints;
DROP TABLE IF EXISTS Trail;
DROP TABLE IF EXISTS Tags;
DROP TABLE IF EXISTS Parks;
DROP TABLE IF EXISTS Hazards;
DROP TABLE IF EXISTS Facilities;

CREATE TABLE Parks(
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE Trail(
    id INT PRIMARY KEY,
    park_id INT REFERENCES Parks(id),
    name VARCHAR(100) NOT NULL,
    length_km FLOAT
);

CREATE TABLE Checkpoints(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    name VARCHAR(100) NOT NULL,
    checkpoint_km FLOAT
);

CREATE TABLE Photos(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    description VARCHAR(100),
    author VARCHAR(50)
);

CREATE TABLE MaintenanceLogs(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    maintainer VARCHAR(50),
    date_serviced DATE
);

CREATE TABLE Conditions(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    status VARCHAR(50),
    notes VARCHAR(120)
);

CREATE TABLE ElevationPoints(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    max_elevation FLOAT,
    min_elevation FLOAT
);

CREATE TABLE Tags(
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE TrailTags(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    tag_id INT REFERENCES Tags(id)
);

CREATE TABLE Hazards(
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE TrailHazards(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    hazard_id INT REFERENCES Hazards(id)
);

CREATE TABLE Facilities(
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE TrailFacilities(
    id INT PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    facility_id INT REFERENCES Facilities(id)
);