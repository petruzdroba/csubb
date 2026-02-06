USE practice;
GO

DROP TABLE IF EXISTS RutaStatie;
DROP TABLE IF EXISTS Statie;
DROP TABLE IF EXISTS Ruta;
DROP TABLE IF EXISTS Tren;
DROP TABLE IF EXISTS Tip;
GO

CREATE TABLE Tip
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    descriere VARCHAR(50)
);

CREATE TABLE Tren
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    tip_id BIGINT FOREIGN KEY REFERENCES Tip(id) ON DELETE CASCADE
);

CREATE TABLE Statie
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50)
);

CREATE TABLE Ruta
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    tren_id BIGINT FOREIGN KEY REFERENCES Tren(id) ON DELETE CASCADE
);

CREATE TABLE RutaStatie(
    ruta_id BIGINT FOREIGN KEY REFERENCES Ruta(id) ON DELETE CASCADE,
    statie_id BIGINT FOREIGN KEY REFERENCES Statie(id) ON DELETE CASCADE,
    CONSTRAINT PK_id PRIMARY KEY (ruta_id, statie_id) ,

    plecare_ora INT,
    plecare_minut INT,
    sosire_ora INT,
    sosire_minut INT
);
GO

INSERT INTO Tip (descriere) VALUES ('passager train'), ('express'),('regional');
INSERT INTO Tren(nume, tip_id) VALUES ('Thomas',1 ), ('Percy', 3), ('Amanda', 2);
INSERT INTO Statie(nume)
VALUES ('Statia De Nord'), ('Statia de SUd'), ('Statia de Est'), ('Statia de Vest');

INSERT INTO Ruta (nume, tren_id) VALUES ('Nord Est', 2), ('Sud Vest',1), ('Sud Nord Vest', 3);
GO

CREATE OR ALTER PROCEDURE statie_ruta
    @ruta BIGINT,
    @statie BIGINT,
    @plecare_ora INT,
    @plecare_minut INT,
    @sosire_ora INT,
    @sosire_minut INT
AS BEGIN
    IF EXISTS (SELECT 1 FROM RutaStatie WHERE ruta_id=@ruta AND statie_id=@statie)
    BEGIN
        UPDATE RutaStatie
        SET plecare_ora=@plecare_ora, plecare_minut=@plecare_minut, sosire_ora=@sosire_ora, sosire_minut=@sosire_minut
        WHERE ruta_id=@ruta AND statie_id=@statie;
    END;
    ELSE
    BEGIN
        INSERT INTO RutaStatie (ruta_id, statie_id, plecare_ora, plecare_minut, sosire_ora, sosire_minut)
        VALUES (@ruta, @statie, @plecare_ora, @plecare_minut, @sosire_ora, @sosire_minut);
    END;
END;
GO

EXEC statie_ruta 1, 1, 10,35,10,25;
EXEC statie_ruta 1, 3, 11,55,11,40;
EXEC statie_ruta 1, 2, 12,55,12,40;
EXEC statie_ruta 1, 4, 14,55,13,40;
GO

CREATE OR ALTER VIEW view_statie_ruta
AS 
SELECT r.nume
FROM Ruta r
JOIN RutaStatie rs ON rs.ruta_id = r.id
JOIN Statie s ON s.id = rs.statie_id
GROUP BY r.nume
HAVING COUNT(*)=(SELECT COUNT(*) FROM Statie);
GO


