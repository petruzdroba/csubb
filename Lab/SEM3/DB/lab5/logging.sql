CREATE TABLE logging (
    log_id SERIAL PRIMARY KEY,
    table_name TEXT NOT NULL,
    record_id INT NOT NULL, --id ul la row-ul sters
    old_row JSONB NOT NULL,
    operation TEXT NOT NULL, -- update sau delete
    op_time TIMESTAMP NOT NULL DEFAULT now(),
    sql_user TEXT NOT NULL DEFAULT current_user
);

CREATE OR REPLACE FUNCTION logging_trigger()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO logging(table_name, record_id, old_row, operation)
    VALUES (
        TG_TABLE_NAME,
        OLD.id,
        to_jsonb(OLD),
        TG_OP
    );

    IF TG_OP = 'UPDATE' THEN
        RETURN NEW;
    END IF;

    RETURN OLD;
END;
$$;

CREATE TRIGGER log_trail_facilities_ud
BEFORE UPDATE OR DELETE ON TrailFacilities
FOR EACH ROW
EXECUTE FUNCTION logging_trigger();

CREATE TRIGGER log_trail_tags_ud
BEFORE UPDATE OR DELETE ON TrailTags
FOR EACH ROW
EXECUTE FUNCTION logging_trigger();

CREATE TRIGGER log_trail_hazards_ud
BEFORE UPDATE OR DELETE ON TrailHazards
FOR EACH ROW
EXECUTE FUNCTION logging_trigger();

SELECT * FROM logging;
TRUNCATE TABLE logging;
