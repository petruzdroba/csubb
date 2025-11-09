CREATE OR REPLACE PROCEDURE InsereazaSectiune(
    IN nume VARCHAR(50),
    IN descriere VARCHAR(50) DEFAULT ''
)
LANGUAGE plpgsql
AS $$
DECLARE
    id_val INT;
BEGIN
    SELECT COUNT(*) + 1 INTO id_val FROM Sectiune;
    INSERT INTO Sectiune(id, nume, descriere)
    VALUES (id_val, nume, descriere);
END;
$$;

CALL InsereazaSectiune('sectiune8', 'sectiune8');

CREATE OR REPLACE PROCEDURE ActualizeazaEmail(
    IN cod_v INT,
    IN new_email VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE Vizitatori SET email=new_email WHERE id=cod_v;
END;
$$;

CALL ActualizeazaEmail(1, 'ana_new_boss@sef.rege'); 

CREATE OR REPLACE PROCEDURE CelPutinONota()
LANGUAGE plpgsql
AS $$
BEGIN
    DROP TABLE IF EXISTS temp_results;

    CREATE TEMP TABLE temp_results AS
    SELECT v.nume, v.email, COUNT(n.nota)
    FROM Vizitatori v
    JOIN Nota n ON n.cod_v = v.id
    GROUP BY v.nume, v.email
    HAVING COUNT(n.nota) >= 1;

END;
$$;

CALL CelPutinONota();
SELECT * FROM temp_results;


CREATE OR REPLACE PROCEDURE CreazaCategorie(
    new_category VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
DECLARE
    id_val INT;
BEGIN
    RAISE NOTICE '%', 
        CASE WHEN EXISTS (SELECT 1 FROM CategoriiVizitatori WHERE nume = new_category) THEN 'Category exists' END;
    SELECT COUNT(*)+1 INTO id_val FROM categoriivizitatori;
    INSERT INTO categoriivizitatori(id, nume) VALUES
    (id_val, new_category);
     
END;
$$;

CALL CreazaCategorie('Bosi');
CALL CreazaCategorie('Copii');


CREATE OR REPLACE PROCEDURE AdaugaAtractie(
    a_name VARCHAR(50),
    a_descriere VARCHAR(50),
    a_varsta INT,
    s_nume VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
DECLARE
    s_id INT;
    id_val INT;
    a_id INT;
BEGIN
    SELECT id INTO s_id FROM sectiune WHERE nume=s_nume;
    IF s_id IS NOT NULL THEN
        SELECT COUNT(*) + 300 INTO  a_id FROM atractie;
        INSERT INTO atractie(id, nume, descriere, varsta, sectiune) VALUES
        (a_id, a_name, a_descriere, a_varsta, s_id);
    ELSE
            SELECT COUNT(*) + 300 INTO id_val FROM Sectiune;
            
            INSERT INTO Sectiune(id, nume, descriere)
            VALUES (id_val, s_nume, '');

            SELECT COUNT(*) + 300 INTO  a_id FROM atractie;
            INSERT INTO atractie(id, nume, descriere, varsta, sectiune) VALUES
            (a_id, a_name, a_descriere, a_varsta, id_val);
    END IF;
END;
$$;
CALL AdaugaAtractie('asdasdasdas', 'asdasdasda', 12, 'sectiune8');
CALL AdaugaAtractie('bsdbfkjasbfkjsbd', 'asdiuasdkjaskda', 10, 'sectiune9');