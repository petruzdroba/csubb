USE lab_tsql;
GO

CREATE OR ALTER PROCEDURE mod_type
AS
BEGIN
    ALTER TABLE MaintananceLogs
    ALTER COLUMN date_serviced DATETIME2;

    INSERT INTO db_version (proce) VALUES('mod_type');
END;
GO

CREATE OR ALTER PROCEDURE rev_mod_type
AS
BEGIN
    ALTER TABLE MaintananceLogs 
    ALTER COLUMN date_serviced DATE;
END;
GO

--Add constraint
CREATE OR ALTER PROCEDURE add_const
AS BEGIN
    ALTER TABLE Photos
    ADD CONSTRAINT DF_photo_description
    DEFAULT 'photo' FOR description;

    INSERT INTO db_version(proce)
    VALUES ('add_const');
END;
GO

CREATE OR ALTER PROCEDURE rev_add_const
AS BEGIN
    ALTER TABLE Photos
    DROP CONSTRAINT DF_photo_description;
END;
GO

CREATE OR ALTER PROCEDURE del_haz
AS BEGIN
    DROP TABLE IF EXISTS Hazards;

    INSERT INTO db_version(proce)
    VALUES ('del_haz');
END;
GO

CREATE OR ALTER PROCEDURE rev_del_haz
AS
BEGIN
    IF OBJECT_ID('Hazards', 'U') IS NULL
    BEGIN
        CREATE TABLE Hazards
        (
            id INT PRIMARY KEY,
            name VARCHAR(50) NOT NULL
        );
    END

    IF OBJECT_ID('TrailHazards', 'U') IS NULL
    BEGIN
        CREATE TABLE TrailHazards
        (
            id INT PRIMARY KEY,
            trail_id INT NOT NULL,
            hazard_id INT NOT NULL,
            CONSTRAINT FK_TrailHazards_Trail
                FOREIGN KEY (trail_id) REFERENCES Trail(id) ON DELETE CASCADE,
            CONSTRAINT FK_TrailHazards_Hazards
                FOREIGN KEY (hazard_id) REFERENCES Hazards(id) ON DELETE CASCADE
        );
    END
END;
GO


CREATE OR ALTER PROCEDURE add_col
AS BEGIN
    ALTER TABLE Tags
    ADD grade SMALLINT DEFAULT 5;

    INSERT INTO db_version(proce)
    VALUES ('add_col');
END;
GO

CREATE OR ALTER PROCEDURE rev_add_col
AS BEGIN
    ALTER TABLE tags
    DROP COLUMN IF EXISTS grade;
END;
GO


CREATE OR ALTER PROCEDURE add_fk
AS BEGIN
    ALTER TABLE TrailHazards
    ADD CONSTRAINT FK_hazard_fkey
    FOREIGN KEY (hazard_id)
    REFERENCES Hazards(id)
    ON DELETE CASCADE;

    INSERT INTO db_version(proce)
    VALUES ('add_FK');
END;
GO

CREATE OR ALTER PROCEDURE rev_add_fk
AS BEGIN
    ALTER TABLE TrailHazards
    DROP CONSTRAINT IF EXISTS FK_hazard_fkey;
END;
GO
--EXEC rev_del_haz;
