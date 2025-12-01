-- A trail has many checkpoints 1:N -> Many trails have one check point N:1

ALTER TABLE Trail
ADD COLUMN main_checkpoint_id INT NULL;

--pastram cel mai mare id fkey
UPDATE Trail t
SET main_checkpoint_id = sub.max_id
FROM (
    SELECT trail_id, MAX(id) AS max_id
    FROM Checkpoints
    GROUP BY trail_id
) AS sub
WHERE t.id = sub.trail_id;

--eliminam checkpointurile care nu au max id
INSERT INTO legaturi_eliminate (NumeTabelSt, IdSt, NumeTabelDr, IdDr)
SELECT 'Trail', c.trail_id, 'Checkpoints', c.id
FROM Checkpoints c
JOIN (
    SELECT trail_id, MAX(id) AS max_id
    FROM Checkpoints
    GROUP BY trail_id
) sub ON c.trail_id = sub.trail_id
WHERE c.id <> sub.max_id; --inafara de max checkpoint

--eliminam constraint
ALTER TABLE Checkpoints
DROP COLUMN trail_id;

-- adaugam constraint
ALTER TABLE Trail
ADD CONSTRAINT trail_main_checkpoint_fk
FOREIGN KEY (main_checkpoint_id) REFERENCES Checkpoints(id)
ON DELETE SET NULL;
