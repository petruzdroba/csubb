CREATE PROCEDURE InsereazaSectiune(
    IN nume VARCHAR(50),
    IN descriere VARCHAR(50)
)
BEGIN
    DECLARE id_val INT;
    SELECT COUNT(*) + 1 INTO id_val FROM Sectiune;
    INSERT INTO Sectiune(id, nume, descriere)
    VALUES (id_val, nume, descriere);
END;

CREATE PROCEDURE ActualizeazaEmail(
    IN cod_v INT,
    IN new_email VARCHAR(50)
)
BEGIN
    UPDATE Vizitatori SET email = new_email WHERE id = cod_v;
END;

CREATE PROCEDURE CelPutinONota()
BEGIN
    DROP TEMPORARY TABLE IF EXISTS temp_results;
    CREATE TEMPORARY TABLE temp_results AS
    SELECT v.nume, v.email, COUNT(n.nota) AS note_count
    FROM Vizitatori v
    JOIN Nota n ON n.cod_v = v.id
    GROUP BY v.nume, v.email
    HAVING COUNT(n.nota) >= 1;
END;

CREATE PROCEDURE CreazaCategorie(
    IN new_category VARCHAR(50)
)
BEGIN
    DECLARE id_val INT;
    IF EXISTS (SELECT 1 FROM CategoriiVizitatori WHERE nume = new_category) THEN
        SELECT 'Category exists' AS notice;
    END IF;
    SELECT COUNT(*) + 1 INTO id_val FROM CategoriiVizitatori;
    INSERT INTO CategoriiVizitatori(id, nume)
    VALUES (id_val, new_category);
END;

CREATE PROCEDURE AdaugaAtractie(
    IN a_name VARCHAR(50),
    IN a_descriere VARCHAR(50),
    IN a_varsta INT,
    IN s_nume VARCHAR(50)
)
BEGIN
    DECLARE s_id INT;
    DECLARE id_val INT;
    DECLARE a_id INT;
    SELECT id INTO s_id FROM Sectiune WHERE nume = s_nume LIMIT 1;
    IF s_id IS NOT NULL THEN
        SELECT COUNT(*) + 300 INTO a_id FROM Atractie;
        INSERT INTO Atractie(id, nume, descriere, varsta, sectiune)
        VALUES (a_id, a_name, a_descriere, a_varsta, s_id);
    ELSE
        SELECT COUNT(*) + 1 INTO id_val FROM Sectiune;
        INSERT INTO Sectiune(id, nume, descriere)
        VALUES (id_val, s_nume, '');
        SELECT COUNT(*) + 300 INTO a_id FROM Atractie;
        INSERT INTO Atractie(id, nume, descriere, varsta, sectiune)
        VALUES (a_id, a_name, a_descriere, a_varsta, id_val);
    END IF;
END;

CALL InsereazaSectiune('sectiune8', 'sectiune8');
CALL ActualizeazaEmail(1, 'ana_new_boss@sef.rege');
CALL CelPutinONota();
SELECT * FROM temp_results;
CALL CreazaCategorie('Bosi');
CALL CreazaCategorie('Copii');
CALL AdaugaAtractie('asdasdasdas', 'asdasdasda', 12, 'sectiune8');
CALL AdaugaAtractie('bsdbfkjasbfkjsbd', 'asdiuasdkjaskda', 10, 'sectiune9');
