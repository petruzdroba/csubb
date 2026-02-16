USE practic_db;
GO 

DROP TABLE IF EXISTS Creatori;
DROP TABLE IF EXISTS Categorie;
DROP TABLE IF EXISTS PLATFORME 
DROP TABLE IF EXISTS SeriePlatforme;
DROP TABLE IF EXISTS Serie;
DROP TABLE IF EXISTS Platforme;
GO

CREATE TABLE Categorie(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    descriere VARCHAR(50)
);

CREATE TABLE Creatori(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    categorie_id BIGINT FOREIGN KEY REFERENCES Categorie(id) ON DELETE CASCADE
);

CREATE TABLE Platforme(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50)
);

CREATE TABLE Serie(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    creator_id BIGINT FOREIGN KEY REFERENCES Creatori(id) ON DELETE CASCADE,
);

CREATE TABLE SeriePlatforme(
    serie_id BIGINT FOREIGN KEY REFERENCES Serie(id) ON DELETE CASCADE,
    platforma_id BIGINT FOREIGN KEY REFERENCES PLatforme(id) ON DELETE CASCADE,
    CONSTRAINT PK_SP PRIMARY KEY (serie_id, platforma_id),
    ora_start TIME,
    ora_stop TIME
);

INSERT INTO Categorie (descriere) VALUES ('Categorie 1'),('Categorie 2'),('Categorie 3'),('Categorie 4');
INSERT INTO Creatori (nume, categorie_id) VALUES ('Nume 1', 1),('Nume 2', 2),('Nume 3', 3),('Nume 4', 4);
INSERT INTO Platforme (nume) VALUES ('Platforma 1'),('Platforma 2'),('Platforma 3'),('Platforma 4');
INSERT INTO Serie (nume, creator_id) VALUES ('Serie 1', 1),('Serie 2', 2),('Serie 3', 3),('Serie 4', 4);
GO

CREATE OR ALTER PROCEDURE link_sp
    @serie BIGINT,
    @platforma BIGINT,
    @ora_start TIME,
    @ora_stop TIME
AS BEGIN
    IF EXISTS(SELECT 1 FROM SeriePlatforme WHERE serie_id=@serie AND platforma_id=@platforma)
    BEGIN
        UPDATE SeriePlatforme
        SET ora_start=@ora_start, ora_stop=@ora_stop
        WHERE serie_id=@serie AND platforma_id=@platforma;
    END;
    ELSE
    BEGIN
        INSERT INTO SeriePlatforme (serie_id, platforma_id, ora_start, ora_stop)
        VALUES (@serie, @platforma, @ora_start, @ora_stop);
    END;
END;
GO

EXEC link_sp 1,1,'13:00', '13:10';
EXEC link_sp 1,2,'13:15', '13:30';

EXEC link_sp 2,2,'13:20', '13:30';
SELECT * FROM SeriePlatforme;
GO

EXEC link_sp 3,1,'13:00', '13:10';
EXEC link_sp 3,2,'13:15', '13:30';
EXEC link_sp 3,3,'13:00', '13:10';
GO

EXEC link_sp 4,1,'13:00', '13:10';
EXEC link_sp 4,2,'13:15', '13:30';
EXEC link_sp 4,3,'13:00', '13:10';
EXEC link_sp 4,4,'13:15', '13:30';
GO


CREATE OR ALTER VIEW all_platforms
AS
SELECT s.nume
FROM Serie s
JOIN SeriePlatforme sp ON sp.serie_id=s.id
GROUP BY s.nume
HAVING COUNT(sp.platforma_id)=(SELECT COUNT(*) FROM Platforme);
GO

SELECT * FROM all_platforms;
GO