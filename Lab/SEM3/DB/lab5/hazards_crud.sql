CREATE OR REPLACE PROCEDURE InsereazaHazard(
    IN p_id INT,
    IN p_name VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO Hazards (id, name)
    VALUES (p_id, p_name);
END;
$$;


CREATE OR REPLACE PROCEDURE InsereazaTrailHazard(
    IN p_id INT,
    IN p_trail_id INT,
    IN p_hazard_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Trail WHERE id = p_trail_id) THEN
        RAISE EXCEPTION 'Trail ID % does not exist', p_trail_id;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Hazards WHERE id = p_hazard_id) THEN
        RAISE EXCEPTION 'Hazard ID % does not exist', p_hazard_id;
    END IF;

    INSERT INTO TrailHazards (id, trail_id, hazard_id)
    VALUES (p_id, p_trail_id, p_hazard_id);
END;
$$;


CREATE OR REPLACE FUNCTION ReadHazard(
    IN p_id INT
)
RETURNS TABLE(hazard_id INT, hazard_name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM hazards h WHERE h.id = p_id) THEN
        RAISE EXCEPTION 'Hazard ID % does not exist', p_id;
    END IF;

    RETURN QUERY
    SELECT h.id, h.name
    FROM hazards h
    WHERE h.id = p_id;
END;
$$;


CREATE OR REPLACE FUNCTION ReadTrailHazards(
    IN p_trail_id INT
)
RETURNS TABLE(trail_hazard_id INT, hazard_id INT, hazard_name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT th.id, h.id, h.name
    FROM trailhazards th
    JOIN hazards h ON th.hazard_id = h.id
    WHERE th.trail_id = p_trail_id;
END;
$$;


CREATE OR REPLACE PROCEDURE UpdateHazard(
    IN p_id INT,
    IN p_name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM hazards WHERE id = p_id) THEN
        RAISE EXCEPTION 'Hazard ID % does not exist', p_id;
    END IF;

    UPDATE hazards
    SET name = p_name
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE UpdateTrailHazard(
    IN p_id INT,
    IN p_trail_id INT,
    IN p_hazard_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trailhazards WHERE id = p_id) THEN
        RAISE EXCEPTION 'TrailHazard ID % does not exist', p_id;
    END IF;

    UPDATE trailhazards
    SET trail_id = p_trail_id,
        hazard_id = p_hazard_id
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE DeleteHazard(
    IN p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM hazards WHERE id = p_id) THEN
        RAISE EXCEPTION 'Hazard ID % does not exist', p_id;
    END IF;

    DELETE FROM TrailHazards WHERE hazard_id = p_id;
    DELETE FROM Hazards WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE DeleteTrailHazard(
    IN p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trailhazards WHERE id = p_id) THEN
        RAISE EXCEPTION 'TrailHazard ID % does not exist', p_id;
    END IF;

    DELETE FROM TrailHazards WHERE id = p_id;
END;
$$;