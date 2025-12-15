--toate facility-urile and ale carui trail sunt
CREATE OR REPLACE VIEW view_facilities_trails AS
SELECT 
    f.id AS facility_id,
    f.name AS facility_name,
    t.id AS trail_id,
    t.name AS trail_name
FROM facilities f
JOIN trailfacilities tf ON f.id = tf.facility_id
JOIN trail t ON tf.trail_id = t.id;

SELECT facility_id, facility_name
FROM view_facilities_trails
WHERE trail_id = 1;


--fiecare tag carui trail ii apartine
CREATE OR REPLACE VIEW view_trails_tags AS
SELECT 
    t.id AS trail_id,
    t.name AS trail_name,
    tg.id AS tag_id,
    tg.name AS tag_name
FROM trail t
JOIN trailtags tt ON t.id = tt.trail_id
JOIN tags tg ON tt.tag_id = tg.id;

SELECT trail_id, trail_name, tag_name
FROM view_trails_tags
WHERE tag_name = 'Loop';
