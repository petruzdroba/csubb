USE lab_tsql;
GO 

DROP TABLE IF EXISTS db_version;

CREATE TABLE db_version(
    version INT PRIMARY KEY IDENTITY(1,1),
    proce VARCHAR(100) NOT NULL
);
GO

CREATE OR ALTER PROCEDURE RevertDB @v INT
AS BEGIN
    DECLARE @current_version INT;
    DECLARE @proc_name VARCHAR;
    DECLARE @sql NVARCHAR(MAX);

    SET @current_version = (SELECT MAX(VERSION) FROM db_version); 

    WHILE @current_version > @v 
    BEGIN
        SET @proc_name = (SELECT proce FROM db_version WHERE version = @current_version);
        SET @sql = 'EXEC rev_' + @proc_name;
        
        EXEC sp_executesql @sql;

        DELETE FROM db_version WHERE version = @current_version;
        SET @current_version = (SELECT MAX(VERSION) FROM db_version); 
    END
END;
GO

-- TRUNCATE TABLE db_version;
-- DBCC CHECKIDENT ('db_version', RESEED, 0);
-- restart identity counter for dbversion