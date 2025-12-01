--test
INSERT INTO Conditions(id, trail_id, status, notes) VALUES
(11, 1, 'Closed', 'Maintenance ongoing'),       -- Trail 1 extra
(12, 1, 'Restricted', 'Weather issues'),        -- Trail 1 extra
(13, 2, 'Open', 'Water levels normal'),         -- Trail 2 extra
(14, 3, 'Closed', 'Foggy morning'),             -- Trail 3 extra
(15, 3, 'Open', 'Clear afternoon');            -- Trail 3 extra
-- a trail can have many conditions -> a trail only has its current condition

ALTER TABLE Trail
ADD COLUMN current_condition_id INT NULL;

--conditia curents -> max id
UPDATE Trail t
SET current_condition_id = sub.max_id
FROM (
    SELECT trail_id, MAX(id) AS max_id
    FROM Conditions
    GROUP BY trail_id
) AS sub
WHERE t.id = sub.trail_id;

--eliminam restul conditiilor din trecut
INSERT INTO Legaturi_Eliminate (NumeTabelSt, IdSt, NumeTabelDr, IdDr)
SELECT 'Trail', c.trail_id, 'Conditions', c.id
FROM Conditions c
JOIN (
    SELECT trail_id, MAX(id) AS max_id
    FROM Conditions
    GROUP BY trail_id
) sub ON c.trail_id = sub.trail_id
WHERE c.id <> sub.max_id;

--avem in Trail.current_condition_id
ALTER TABLE Conditions
DROP COLUMN trail_id;

--constraint
ALTER TABLE Trail
ADD CONSTRAINT trail_current_condition_fk
FOREIGN KEY (current_condition_id) REFERENCES Conditions(id)
ON DELETE SET NULL;
