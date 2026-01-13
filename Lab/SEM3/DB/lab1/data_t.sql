use lab_tsql;
GO

-- Parks
INSERT INTO Parks(id, name, location) VALUES
(1, 'Evergreen National Park', 'Washington'),
(2, 'Sunny Valley Park', 'California'),
(3, 'Red Canyon Reserve', 'Utah'),
(4, 'Blue Mist Forest', 'Oregon'),
(5, 'Granite Peaks Park', 'Colorado'),
(6, 'Silver River Park', 'Montana'),
(7, 'Crescent Hills', 'Nevada'),
(8, 'Maple Grove Park', 'Vermont'),
(9, 'Highland Meadows', 'Wyoming'),
(10, 'Desert Bloom Park', 'Arizona');

-- Trails
INSERT INTO Trail(id, park_id, name, length_km) VALUES
(1, 1, 'Pine Loop', 5.2),
(2, 1, 'River Trail', 8.7),
(3, 2, 'Sunny Ridge', 4.3),
(4, 3, 'Canyon Walk', 6.1),
(5, 3, 'Ridge Climb', 10.4),
(6, 4, 'Mist Path', 3.8),
(7, 5, 'Peak Challenge', 12.7),
(8, 6, 'Silver Loop', 4.9),
(9, 7, 'Hilltop Route', 7.2),
(10, 8, 'Maple Run', 2.6);

-- Checkpoints
INSERT INTO Checkpoints(id, trail_id, name, checkpoint_km) VALUES
(1, 1, 'Trailhead', 0.0),
(2, 1, 'Old Oak', 2.5),
(3, 1, 'Summit', 5.2),
(4, 2, 'River Start', 0.0),
(5, 2, 'Waterfall', 4.0),
(6, 3, 'Sunny Start', 0.0),
(7, 4, 'Canyon Gate', 0.0),
(8, 5, 'Upper Ridge', 6.0),
(9, 7, 'Base Camp', 0.0),
(10, 8, 'Silver Junction', 2.3);

-- Photos
INSERT INTO Photos(id, trail_id, description, author) VALUES
(1, 1, 'View from Summit', 'Alice'),
(2, 2, 'River Crossing', 'Bob'),
(3, 3, 'Sunrise', 'Charlie'),
(4, 4, 'Canyon Walls', 'Dana'),
(5, 5, 'High Ridge', 'Evan'),
(6, 6, 'Morning Fog', 'Alice'),
(7, 7, 'Rocky Climb', 'Frank'),
(8, 8, 'Silver River Bend', 'George'),
(9, 9, 'Hilltop Lookout', 'Helen'),
(10, 10, 'Autumn Leaves', 'Ivy');

-- MaintenanceLogs
INSERT INTO MaintenanceLogs(id, trail_id, maintainer, date_serviced) VALUES
(1, 1, 'John', '2025-01-15'),
(2, 2, 'Mary', '2025-02-20'),
(3, 3, 'Victor', '2025-03-12'),
(4, 4, 'Lena', '2025-02-01'),
(5, 5, 'John', '2025-04-08'),
(6, 6, 'Kara', '2025-01-29'),
(7, 7, 'Mary', '2025-03-22'),
(8, 8, 'Victor', '2025-01-10'),
(9, 9, 'Lena', '2025-02-18'),
(10, 10, 'Kara', '2025-04-01');

-- Conditions
INSERT INTO Conditions(id, trail_id, status, notes) VALUES
(1, 1, 'Open', 'Good conditions'),
(2, 2, 'Closed', 'Flooded in sections'),
(3, 3, 'Open', 'Dry and clear'),
(4, 4, 'Open', 'Windy at canyon rim'),
(5, 5, 'Restricted', 'Snow near peak'),
(6, 6, 'Open', 'Foggy mornings'),
(7, 7, 'Closed', 'Rockfall cleanup'),
(8, 8, 'Open', 'Smooth terrain'),
(9, 9, 'Open', 'High visibility'),
(10, 10, 'Open', 'Leaf cover on trail');

-- ElevationPoints
INSERT INTO ElevationPoints(id, trail_id, max_elevation, min_elevation) VALUES
(1, 1, 1200, 800),
(2, 2, 900, 400),
(3, 3, 600, 300),
(4, 4, 1400, 900),
(5, 5, 2100, 1300),
(6, 6, 700, 500),
(7, 7, 2600, 1400),
(8, 8, 550, 420),
(9, 9, 1100, 700),
(10, 10, 450, 300);

-- Tags
INSERT INTO Tags(id, name) VALUES
(1, 'Easy'),
(2, 'Moderate'),
(3, 'Scenic'),
(4, 'Family Friendly'),
(5, 'Challenging'),
(6, 'Waterfall'),
(7, 'Forest'),
(8, 'Viewpoint'),
(9, 'Wildlife'),
(10, 'Loop');

-- TrailTags
INSERT INTO TrailTags(id, trail_id, tag_id) VALUES
(1, 1, 2),
(2, 1, 3),
(3, 2, 2),
(4, 3, 1),
(5, 4, 3),
(6, 5, 5),
(7, 6, 7),
(8, 7, 8),
(9, 8, 10),
(10, 9, 9);

-- Hazards
INSERT INTO Hazards(id, name) VALUES
(1, 'Steep drop'),
(2, 'Loose rocks'),
(3, 'Bear activity'),
(4, 'Slippery path'),
(5, 'Falling branches'),
(6, 'Narrow ledge'),
(7, 'Fast-flowing water'),
(8, 'Avalanche zone'),
(9, 'Heat exposure'),
(10, 'Cactus patches');

-- TrailHazards
INSERT INTO TrailHazards(id, trail_id, hazard_id) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 3, 4),
(4, 4, 6),
(5, 5, 8),
(6, 6, 5),
(7, 7, 3),
(8, 8, 7),
(9, 9, 9),
(10, 10, 10);

-- Facilities
INSERT INTO Facilities(id, name) VALUES
(1, 'Restroom'),
(2, 'Picnic Area'),
(3, 'Parking Lot'),
(4, 'Visitor Center'),
(5, 'Water Station'),
(6, 'Campground'),
(7, 'Shelter'),
(8, 'Bike Rack'),
(9, 'Trash Bin'),
(10, 'Lookout Tower');

-- TrailFacilities
INSERT INTO TrailFacilities(id, trail_id, facility_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 1),
(4, 3, 3),
(5, 4, 4),
(6, 5, 5),
(7, 6, 7),
(8, 7, 6),
(9, 8, 9),
(10, 10, 10);

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
