DROP TABLE IF EXISTS db_version;

CREATE TABLE db_version(
    version SERIAL PRIMARY KEY, -- auto increase
    procedure VARCHAR(50) NOT NULL
);

CREATE OR REPLACE PROCEDURE RevertDB(
    IN v INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    current_version INT;
    proc_name VARCHAR;
BEGIN
    SELECT MAX(version) INTO current_version FROM db_version;

    WHILE current_version > v LOOP
        SELECT procedure INTO proc_name FROM db_version
        WHERE version = current_version;

        EXECUTE format('CALL rev_%s()', proc_name);

        DELETE FROM db_version WHERE version = current_version;

        SELECT MAX(version) INTO current_version FROM db_version;
    END LOOP;
END;
$$;

TRUNCATE TABLE db_version RESTART IDENTITY; --reset db version table

CALL mod_type();
CALL add_const();
CALL add_col();

SELECT * FROM db_version;
SELECT * FROM MaintenanceLogs;
SELECT * FROM Photos;
SELECT * FROM Tags;

CALL RevertDB(1);

SELECT * FROM db_version;
SELECT * FROM MaintenanceLogs;
SELECT * FROM Photos;
SELECT * FROM Tags;