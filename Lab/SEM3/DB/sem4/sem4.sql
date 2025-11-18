CREATE OR REPLACE FUNCTION Ex1(nume_categorie VARCHAR)
RETURNS INT AS $$
DECLARE
    categorie_id INT;
BEGIN
    SELECT id INTO categorie_id
    FROM CategoriiVizitatori
    WHERE nume = nume_categorie
    LIMIT 1;
    RETURN categorie_id;
END;
$$ LANGUAGE plpgsql;

SELECT Ex1('Adulti'::VARCHAR);

CREATE OR REPLACE FUNCTION Ex2(ch CHAR)
RETURNS TABLE(id INT, nume VARCHAR, descriere VARCHAR) AS $$
BEGIN
    RETURN QUERY
    SELECT s.id, s.nume, s.descriere
    FROM Sectiune s
    WHERE CHAR_LENGTH(s.nume) >= 2
      AND RIGHT(s.nume, 1) = ch;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM Ex2('2'::CHAR);


CREATE OR REPLACE VIEW Ex3
AS
 SELECT * FROM CategoriiVizitatori
 WHERE nume='Copii' OR nume='Seniori';

SELECT * FROM EX3;


CREATE OR REPLACE VIEW Ex4
AS
    SELECT * FROM Sectiune s
    WHERE LEFT(s.nume, 1)='s';
SELECT * FROM Ex4;


CREATE OR REPLACE VIEW Ex5
AS
    SELECT v.nume,n.nota, a.nume AS nume_atractie
    FROM Vizitatori v
    JOIN Nota n ON n.cod_v = v.id
    JOIN Atractie a ON n.cod_a = a.id;

SELECT * FROM Ex5;


CREATE OR REPLACE FUNCTION show_message()
RETURNS TRIGGER
AS $$
BEGIN
    RAISE EXCEPTION 'Cannot delete';
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER BlockDelete
BEFORE DELETE
ON CategoriiVizitatori
FOR EACH ROW
EXECUTE FUNCTION show_message();

DELETE FROM CategoriiVizitatori WHERE nume='Sefi';