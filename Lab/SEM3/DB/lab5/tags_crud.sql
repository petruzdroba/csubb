CREATE OR REPLACE PROCEDURE InsereazaTag(
    IN p_id INT,
    IN p_name VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO Tags (id, name)
    VALUES (p_id, p_name);
END;
$$;


CREATE OR REPLACE PROCEDURE InsereazaTrailTag(
    IN p_id INT,
    IN p_trail_id INT,
    IN p_tag_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Trail WHERE id = p_trail_id) THEN
        RAISE EXCEPTION 'Trail ID % does not exist', p_trail_id;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Tags WHERE id = p_tag_id) THEN
        RAISE EXCEPTION 'Tag ID % does not exist', p_tag_id;
    END IF;

    INSERT INTO TrailTags (id, trail_id, tag_id)
    VALUES (p_id, p_trail_id, p_tag_id);
END;
$$;


CREATE OR REPLACE FUNCTION ReadTag(
    IN p_id INT
)
RETURNS TABLE(tag_id INT, tag_name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM tags tg WHERE tg.id = p_id) THEN
        RAISE EXCEPTION 'Tag ID % does not exist', p_id;
    END IF;

    RETURN QUERY
    SELECT tg.id, tg.name
    FROM tags tg
    WHERE tg.id = p_id;
END;
$$;


CREATE OR REPLACE FUNCTION ReadTrailTags(
    IN p_trail_id INT
)
RETURNS TABLE(trail_tag_id INT, tag_id INT, tag_name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT tt.id, tg.id, tg.name
    FROM trailtags tt
    JOIN tags tg ON tt.tag_id = tg.id
    WHERE tt.trail_id = p_trail_id;
END;
$$;


CREATE OR REPLACE PROCEDURE UpdateTag(
    IN p_id INT,
    IN p_name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM tags WHERE id = p_id) THEN
        RAISE EXCEPTION 'Tag ID % does not exist', p_id;
    END IF;

    UPDATE tags
    SET name = p_name
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE UpdateTrailTag(
    IN p_id INT,
    IN p_trail_id INT,
    IN p_tag_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trailtags WHERE id = p_id) THEN
        RAISE EXCEPTION 'TrailTag ID % does not exist', p_id;
    END IF;

    UPDATE trailtags
    SET trail_id = p_trail_id,
        tag_id = p_tag_id
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE DeleteTag(
    IN p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM tags WHERE id = p_id) THEN
        RAISE EXCEPTION 'Tag ID % does not exist', p_id;
    END IF;

    DELETE FROM TrailTags WHERE tag_id = p_id;
    DELETE FROM Tags WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE DeleteTrailTag(
    IN p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM trailtags WHERE id = p_id) THEN
        RAISE EXCEPTION 'TrailTag ID % does not exist', p_id;
    END IF;

    DELETE FROM TrailTags WHERE id = p_id;
END;
$$;