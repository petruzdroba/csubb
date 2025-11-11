-- Modify column type
CREATE OR REPLACE PROCEDURE mod_type()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE MaintenanceLogs
    ALTER COLUMN date_serviced TYPE TIMESTAMP;

    INSERT INTO db_version(procedure)
    VALUES ('mod_type');
END;
$$;

CREATE OR REPLACE PROCEDURE rev_mod_type()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE MaintenanceLogs
    ALTER COLUMN date_serviced TYPE DATE;
END;
$$;


-- Add constraint
CREATE OR REPLACE PROCEDURE add_const()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE photos
    ALTER COLUMN description SET DEFAULT 'photo';

    INSERT INTO db_version(procedure)
    VALUES ('add_const');
END;
$$;

CREATE OR REPLACE PROCEDURE rev_add_const()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE photos
    ALTER COLUMN description DROP DEFAULT;
END;
$$;


-- Delete / create Hazards table
CREATE OR REPLACE PROCEDURE del_haz()
LANGUAGE plpgsql
AS $$
BEGIN
    DROP TABLE IF EXISTS hazards CASCADE;

    INSERT INTO db_version(procedure)
    VALUES ('del_haz');
END;
$$;

CREATE OR REPLACE PROCEDURE rev_del_haz()
LANGUAGE plpgsql
AS $$
BEGIN
    CREATE TABLE Hazards(
        id INT PRIMARY KEY,
        name VARCHAR(50) NOT NULL
    );

    CREATE TABLE TrailHazards(
        id INT PRIMARY KEY,
        trail_id INT REFERENCES Trail(id),
        hazard_id INT REFERENCES Hazards(id)
    );
END;
$$;


-- Add / remove column
CREATE OR REPLACE PROCEDURE add_col()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE tags
    ADD COLUMN grade SMALLINT DEFAULT 5;

    INSERT INTO db_version(procedure)
    VALUES ('add_col');
END;
$$;

CREATE OR REPLACE PROCEDURE rev_add_col()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE tags
    DROP COLUMN IF EXISTS grade;
END;
$$;


-- Add / remove foreign key
CREATE OR REPLACE PROCEDURE add_fk()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE trailhazards
    ADD CONSTRAINT trailhazards_hazard_id_fkey
    FOREIGN KEY (hazard_id) 
    REFERENCES hazards(id)
    ON DELETE CASCADE;

    INSERT INTO db_version(procedure)
    VALUES ('add_fk');
END;
$$;

CREATE OR REPLACE PROCEDURE rev_add_fk()
LANGUAGE plpgsql
AS $$
BEGIN
    ALTER TABLE trailhazards
    DROP CONSTRAINT IF EXISTS trailhazards_hazard_id_fkey;
END;
$$;