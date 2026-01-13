USE lab_tsql;
GO

--run lab1.sql for data and tables

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