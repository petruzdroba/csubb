--Many trails have many tags -> many trails have 1 tag

--adaugam un tag care defineste trailul
ALTER TABLE Trail
ADD COLUMN main_tag_id INT NULL;

--fiecare isi pastreaza tagul cu id-ul cel mai mare
UPDATE Trail t
SET main_tag_id = sub.max_tag_id
FROM (
    SELECT trail_id, MAX(tag_id) AS max_tag_id
    FROM TrailTags
    GROUP BY trail_id
) AS sub
WHERE t.id = sub.trail_id;

--eliminam restul tagurilor care nu au trail id max
INSERT INTO Legaturi_Eliminate (NumeTabelSt, IdSt, NumeTabelDr, IdDr)
SELECT 'Trail', c.trail_id, 'Tags', c.tag_id
FROM TrailTags c
JOIN (
    SELECT trail_id, MAX(tag_id) AS max_tag_id
    FROM TrailTags
    GROUP BY trail_id
) sub ON c.trail_id = sub.trail_id
WHERE c.tag_id <> sub.max_tag_id;

-- eliminam tbaelul de legatura
DROP TABLE TrailTags;

-- adaugam contraint
ALTER TABLE Trail
ADD CONSTRAINT trail_main_tag_fk
FOREIGN KEY (main_tag_id) REFERENCES Tags(id)
ON DELETE SET NULL;

--test
SELECT t.name AS trail, t.main_tag_id, tg.name AS main_tag
FROM Trail t
LEFT JOIN Tags tg ON tg.id = t.main_tag_id
ORDER BY t.id;