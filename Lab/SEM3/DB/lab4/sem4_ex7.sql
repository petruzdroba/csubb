CREATE OR REPLACE FUNCTION fill_desc()
RETURNS TRIGGER
AS $$
BEGIN
    IF NEW.description IS NULL OR NEW.description='' THEN
        NEW.description := NEW.author || ' on trail ' || NEW.trail_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trigger_photo
BEFORE INSERT
ON Photos
FOR EACH ROW
EXECUTE FUNCTION fill_desc();

INSERT INTO Photos(id, trail_id, author) VALUES (101, 1, 'Alice');
INSERT INTO Photos(id, trail_id, description,author) VALUES (102, 1, 'Alice', 'Poza cu bananier');

SELECT * FROM Photos;
