CREATE OR REPLACE PROCEDURE InsereazaTrail(
    IN p_id INT,
    IN p_park_id INT, 
    IN p_length_km FLOAT,
    IN p_name VARCHAR(50) DEFAULT 'park'
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Parks WHERE id = p_park_id) THEN
        RAISE EXCEPTION 'Park ID % does not exist', p_park_id;
    END IF;

    INSERT INTO Trail(id, park_id, name, length_km) VALUES
    (p_id, p_park_id, p_name, p_length_km);
END;
$$;


CREATE OR REPLACE FUNCTION ReadTrail(
    IN p_id INT
)
RETURNS TABLE(trail_id INT, park_id INT, trail_name VARCHAR, length_km FLOAT)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trail t WHERE t.id = p_id) THEN
        RAISE EXCEPTION 'Trail ID % does not exist', p_id;
    END IF;

    RETURN QUERY
    SELECT t.id, t.park_id, t.name, t.length_km
    FROM trail t
    WHERE t.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE UpdateTrail(
    IN p_id INT,
    IN p_name VARCHAR,
    IN p_park_id INT,
    IN p_length_km FLOAT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trail WHERE id = p_id) THEN
        RAISE EXCEPTION 'Trail ID % does not exist', p_id;
    END IF;

    UPDATE trail
    SET name = p_name,
        park_id = p_park_id,
        length_km = p_length_km
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE DeleteTrail(
    IN p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trail WHERE id = p_id) THEN
        RAISE EXCEPTION 'Trail ID % does not exist', p_id;
    END IF;

    DELETE FROM TrailFacilities WHERE trail_id = p_id;
    DELETE FROM TrailTags WHERE trail_id = p_id;
    DELETE FROM TrailHazards WHERE trail_id = p_id;
    
    DELETE FROM Trail WHERE id = p_id;
END;
$$;