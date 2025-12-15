--Create
CREATE OR REPLACE PROCEDURE InsereazaFacility(
    IN p_id INT,
    IN p_name VARCHAR(50) DEFAULT 'facility'
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO Facilities (id, name)
    VALUES (p_id, p_name);
END;
$$;


CREATE OR REPLACE PROCEDURE InsereazaTrailFacility(
    IN p_id INT,
    IN p_trail_id INT,
    IN p_facility_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Facilities WHERE id = p_facility_id) THEN
        RAISE EXCEPTION 'Facility ID % does not exist', p_facility_id;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Trail WHERE id = p_trail_id) THEN
        RAISE EXCEPTION 'Trail ID % does not exist', p_trail_id;
    END IF;

    INSERT INTO TrailFacilities (id, trail_id, facility_id)
    VALUES (p_id, p_trail_id, p_facility_id);
END;
$$;


--Read
CREATE OR REPLACE FUNCTION ReadFacility(
    IN p_id INT
)
RETURNS TABLE(id INT, name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
     IF NOT EXISTS (SELECT 1 FROM Facilities f WHERE f.id = p_id) THEN
        RAISE EXCEPTION 'Facility ID % does not exist', p_id;
    END IF;

    RETURN QUERY SELECT f.id, f.name FROM facilities f WHERE f.id=p_id;
END;
$$;


CREATE OR REPLACE FUNCTION ReadTrailFacilities(
    IN p_trail_id INT
)
RETURNS TABLE(trail_facility_id INT, facility_id INT, facility_name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT tf.id, f.id, f.name
    FROM trailfacilities tf
    JOIN facilities f ON tf.facility_id = f.id
    WHERE tf.trail_id = p_trail_id;
END;
$$;


CREATE OR REPLACE PROCEDURE UpdateFacility(
    IN p_id INT,
    IN p_name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM facilities WHERE id = p_id) THEN
        RAISE EXCEPTION 'Facility ID % does not exist', p_id;
    END IF;

    UPDATE facilities
    SET name = p_name
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE UpdateTrailFacility(
    IN p_id INT,
    IN p_trail_id INT,
    IN p_facility_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trailfacilities WHERE id = p_id) THEN
        RAISE EXCEPTION 'TrailFacility ID % does not exist', p_id;
    END IF;

    UPDATE trailfacilities
    SET trail_id = p_trail_id,
        facility_id = p_facility_id
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE DeleteFacility(
    IN p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM facilities WHERE id = p_id) THEN
        RAISE EXCEPTION 'Facility ID % does not exist', p_id;
    END IF;

    DELETE FROM TrailFacilities WHERE facility_id = p_id;
    DELETE FROM Facilities WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE DeleteTrailFacility(
    IN p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trailfacilities WHERE id = p_id) THEN
        RAISE EXCEPTION 'TrailFacility ID % does not exist', p_id;
    END IF;

    DELETE FROM TrailFacilities WHERE id = p_id;
END;
$$;
