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

-- Parks
INSERT INTO Parks(id, name, location) VALUES
(1, 'Evergreen National Park', 'Washington'),
(2, 'Sunny Valley Park', 'California');

-- Trails
INSERT INTO Trail(id, park_id, name, length_km) VALUES
(1, 1, 'Pine Loop', 5.2),
(2, 1, 'River Trail', 8.7),
(3, 2, 'Sunny Ridge', 4.3);

-- Checkpoints
INSERT INTO Checkpoints(id, trail_id, name, checkpoint_km) VALUES
(1, 1, 'Trailhead', 0.0),
(2, 1, 'Old Oak', 2.5),
(3, 1, 'Summit', 5.2),
(4, 2, 'River Start', 0.0),
(5, 2, 'Waterfall', 4.0),
(6, 3, 'Sunny Start', 0.0);

-- Photos
INSERT INTO Photos(id, trail_id, description, author) VALUES
(1, 1, 'View from Summit', 'Alice'),
(2, 2, 'River Crossing', 'Bob'),
(3, 3, 'Sunrise', 'Charlie');

-- MaintenanceLogs
INSERT INTO MaintenanceLogs(id, trail_id, maintainer, date_serviced) VALUES
(1, 1, 'John', '2025-01-15'),
(2, 2, 'Mary', '2025-02-20');

-- Conditions
INSERT INTO Conditions(id, trail_id, status, notes) VALUES
(1, 1, 'Open', 'Good conditions'),
(2, 2, 'Closed', 'Flooded in sections');

-- ElevationPoints
INSERT INTO ElevationPoints(id, trail_id, max_elevation, min_elevation) VALUES
(1, 1, 1200, 800),
(2, 2, 900, 400);

-- Tags
INSERT INTO Tags(id, name) VALUES
(1, 'Easy'),
(2, 'Moderate'),
(3, 'Scenic');

-- TrailTags
INSERT INTO TrailTags(id, trail_id, tag_id) VALUES
(1, 1, 2),
(2, 1, 3),
(3, 2, 2),
(4, 3, 1);

-- Hazards
INSERT INTO Hazards(id, name) VALUES
(1, 'Steep drop'),
(2, 'Loose rocks');

-- TrailHazards
INSERT INTO TrailHazards(id, trail_id, hazard_id) VALUES
(1, 1, 2),
(2, 2, 1);

-- Facilities
INSERT INTO Facilities(id, name) VALUES
(1, 'Restroom'),
(2, 'Picnic Area');

-- TrailFacilities
INSERT INTO TrailFacilities(id, trail_id, facility_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1);



SELECT * FROM Parks;
SELECT * FROM Trail;
SELECT * FROM Checkpoints;
SELECT * FROM Photos;
SELECT * FROM MaintenanceLogs;
SELECT * FROM Conditions;
SELECT * FROM ElevationPoints;
SELECT * FROM Tags;
SELECT * FROM Hazards;
SELECT * FROM Facilities;

SELECT * FROM TrailTags;
SELECT * FROM TrailHazards;
SELECT * FROM TrailFacilities;
