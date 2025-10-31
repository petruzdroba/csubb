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
(2, 'Sunny Valley Park', 'California'),
(3, 'Granite Peak Reserve', 'Colorado'),
(4, 'Yosemite', 'California');

-- Trails (different numbers per park)
INSERT INTO Trail(id, park_id, name, length_km) VALUES
(1, 1, 'Pine Loop', 5.2),
(2, 1, 'River Trail', 8.7),
(3, 2, 'Sunny Ridge', 4.3),
(4, 3, 'Granite Climb', 10.5),
(5, 3, 'Eagle Pass', 6.8),
(6,4, 'Big Capitan', 20.0);

-- Checkpoints (variable per trail)
INSERT INTO Checkpoints(id, trail_id, name, checkpoint_km) VALUES
(1, 1, 'Trailhead', 0.0),
(2, 1, 'Old Oak', 2.5),
(3, 1, 'Summit', 5.2),
(4, 2, 'River Start', 0.0),
(5, 3, 'Sunny Start', 0.0),
(6, 3, 'Ridge View', 2.2),
(7, 4, 'Base Camp', 0.0),
(8, 4, 'Rocky Point', 5.5),
(9, 4, 'Summit Peak', 10.5),
(10, 5, 'Trailhead', 0.0),
(11, 5, 'Eagle Cliff', 6.8),
(12,6, 'The Nose', 13.0),
(13,6, 'Changing Corners', 15.0);

-- Photos (authors sometimes repeat)
INSERT INTO Photos(id, trail_id, description, author) VALUES
(1, 1, 'View from Summit', 'Alice'),
(2, 2, 'River Crossing', 'Bob'),
(3, 3, 'Sunrise', 'Alice'),
(4, 4, 'Rocky cliffs view', 'Diana'),
(5, 5, 'Eagle soaring', 'Bob'),
(6, 1, 'Forest Trail', 'John'),
(7, 2, 'Waterfall close-up', 'Mary'),
(8, 3, 'Ridge Panorama', 'Sam'),
(9, 4, 'Snowy Peak', 'Nina'),
(10, 5, 'Eagle Nest', 'Paul'),
(11, 3, 'Lake Reflection', 'Tina'),
(12, 1, 'Sunset over Pines', 'Alice'),
(13, 2, 'River Bend', 'Bob'),
(14, 3, 'Trail Crossing', 'Paul'),
(15, 4, 'Rocky Path', 'John'),
(16, 6, 'The Boulder Problem', 'Alex');

-- MaintenanceLogs (maintainers sometimes repeat)
INSERT INTO MaintenanceLogs(id, trail_id, maintainer, date_serviced) VALUES
(1, 1, 'John', '2025-01-15'),
(2, 2, 'Mary', '2025-02-20'),
(3, 3, 'John', '2025-03-10'),
(4, 4, 'Nina', '2025-05-05'),
(5, 5, 'Paul', '2025-06-18'),
(6,6, 'Tommy', '2025-09-19');

-- Conditions
INSERT INTO Conditions(id, trail_id, status, notes) VALUES
(1, 1, 'Open', 'Good conditions'),
(2, 2, 'Closed', 'Flooded'),
(3, 3, 'Open', 'Sunny'),
(4, 4, 'Closed', 'Rescue operation'),
(5, 5, 'Open', 'Windy but clear'),
(6,6, 'Open', 'Windy');

-- ElevationPoints
INSERT INTO ElevationPoints(id, trail_id, max_elevation, min_elevation) VALUES
(1, 1, 1200, 800),
(2, 2, 900, 400),
(3, 3, 700, 500),
(4, 4, 3000, 1500),
(5, 5, 2500, 1800),
(6,6, 4500, 0);

-- Tags
INSERT INTO Tags(id, name) VALUES
(1, 'Easy'), (2, 'Moderate'), (3, 'Scenic'), (4, 'Challenging'), (5, 'Family-friendly');

-- TrailTags (variable per trail)
INSERT INTO TrailTags(id, trail_id, tag_id) VALUES
(1, 1, 2), (2, 1, 3),
(3, 2, 2),
(4, 3, 1), (5, 3, 5),
(6, 4, 4), (7, 4, 3), (8, 4, 5),
(9, 5, 3), (10, 6, 4);

-- Hazards
INSERT INTO Hazards(id, name) VALUES
(1, 'Steep drop'), (2, 'Loose rocks'), (3, 'Falling branches'), (4, 'Death');

-- TrailHazards (variable per trail)
INSERT INTO TrailHazards(id, trail_id, hazard_id) VALUES
(1, 1, 2),
(2, 2, 1), (3, 2, 2),
(4, 3, 3),
(5, 4, 1), (6, 4, 3),
(7, 5, 2), (8, 6, 4);

-- Facilities
INSERT INTO Facilities(id, name) VALUES
(1, 'Restroom'), (2, 'Picnic Area'), (3, 'Parking');

-- TrailFacilities (variable per trail)
INSERT INTO TrailFacilities(id, trail_id, facility_id) VALUES
(1, 1, 1), (2, 1, 2),
(3, 2, 1),
(4, 3, 2), (5, 3, 3),
(6, 4, 1), (7, 4, 3),
(8, 5, 2);

SELECT * FROM PHOTOS WHERE author='Diana' OR author='Bob';

--trails in cali
SELECT t.name AS trail_name, p.name AS cali_parks FROM Trail t
JOIN Parks p ON p.id = t.park_id
WHERE p.location = 'California';

--trails that have parking
SELECT t.name AS trail_name, f.name FROM Trail t
JOIN TrailFacilities tf ON tf.trail_id=t.id
JOIN Facilities f ON f.id=tf.facility_id WHERE f.name = 'Parking';

-- trails that are closed
SELECT t.name AS trail_name, c.notes AS reason_closed FROM Trail t
JOIN Conditions c ON c.trail_id=t.id
WHERE c.status = 'Closed';

--trails with checkpoints further than 3km from start
SELECT DISTINCT t.name AS trail_name, c.name AS checkpoint_name, c.checkpoint_km
 FROM Trail t
JOIN Checkpoints c ON c.trail_id = t.id 
WHERE c.checkpoint_km >= 3.0;

--trails that have elevation gain over 1000
SELECT t.name AS trail_name, (e.max_elevation-e.min_elevation) AS elevation_gain
FROM Trail t
JOIN ElevationPoints e ON e.trail_id=t.id
WHERE e.max_elevation-e.min_elevation >= 1000;


--all trails with more than 1 hazard
SELECT DISTINCT t.name AS trail_name,p.name AS park_name, COUNT(DISTINCT th.hazard_id) 
FROM trail t 
JOIN parks p ON p.id = t.park_id
JOIN trailhazards th on th.trail_id = t.id
GROUP BY t.id, t.name, p.name
HAVING COUNT(DISTINCT th.hazard_id) > 1;

--maintainers who also posted photos
SELECT m.maintainer , COUNT(p.id) AS photo_count FROM maintenancelogs m
JOIN photos p ON m.maintainer=p.author
GROUP BY  m.maintainer;

--distinct facilities
SELECT t.name AS trail_name, COUNT(trail_id) AS facility_nr FROM Trail t
JOIN trailfacilities n ON n.trail_id = t.id
GROUP BY t.id, t.name
HAVING COUNT(trail_id) > 0;

--List the parks that have more than two distinct tags across all their trails.
SELECT p.name AS park_name, COUNT(DISTINCT tt.tag_id) AS distinct_tags
FROM Parks p
JOIN Trail T on t.park_id = p.id
JOIN trailtags tt on tt.trail_id = t.id
GROUP BY p.id, p.name
HAVING COUNT(DISTINCT tt.tag_id) >2;