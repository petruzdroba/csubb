-- A trail has many logs -> many trails have many logs, logs shared inbetween trails in the same park

--creem tabelul de legatura
CREATE TABLE IF NOT EXISTS TrailMaintenanceLogs(
    id SERIAL PRIMARY KEY,
    trail_id INT REFERENCES Trail(id),
    maintenance_log_id INT REFERENCES MaintenanceLogs(id)
);

--date deja existente
INSERT INTO TrailMaintenanceLogs (trail_id, maintenance_log_id)
SELECT m.trail_id, m.id
FROM MaintenanceLogs m;

--nu mai e nevoie
ALTER TABLE MaintenanceLogs
DROP COLUMN trail_id;

--constraints
ALTER TABLE TrailMaintenanceLogs
ADD CONSTRAINT fk_trail FOREIGN KEY (trail_id) REFERENCES Trail(id);

ALTER TABLE TrailMaintenanceLogs
ADD CONSTRAINT fk_maintenance_log FOREIGN KEY (maintenance_log_id) REFERENCES MaintenanceLogs(id);


--test
INSERT INTO MaintenanceLogs (id, maintainer, date_serviced)
VALUES (41,'John', '2025-12-01');

INSERT INTO TrailMaintenanceLogs (trail_id, maintenance_log_id) VALUES
(1, 41),  -- Pine Loop
(2, 41),  -- River Trail
(3, 41);

SELECT m.id AS log_id, m.maintainer, t.name AS trail
FROM TrailMaintenanceLogs tml
JOIN MaintenanceLogs m ON m.id = tml.maintenance_log_id
JOIN Trail t ON t.id = tml.trail_id
ORDER BY m.id, t.id;