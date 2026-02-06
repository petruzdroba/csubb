USE practice;
GO

DROP TABLE IF EXISTS Turnee;
DROP TABLE IF EXISTS Jucatori;
DROP TABLE IF EXISTS Arene;
DROP TABLE IF EXISTS PartideInfo;
DROP TABLE IF EXISTS Partide;

CREATE TABLE Turnee
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    locatie VARCHAR(50),
    perioada VARCHAR(50)
);

CREATE TABLE Jucatori
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    puncte INT,
    premii INT
);

CREATE TABLE Arene
(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50)
);

CREATE TABLE Partide
(
    jucator1 BIGINT FOREIGN KEY REFERENCES Jucatori(id),
    jucator2 BIGINT FOREIGN KEY REFERENCES Jucatori(id),
    CONSTRAINT id PRIMARY KEY (jucator1, jucator2),
    arena BIGINT FOREIGN KEY REFERENCES Arene(id) ON DELETE CASCADE,
    data DATETIME2,
    turneu BIGINT FOREIGN KEY REFERENCES Turnee(id) ON DELETE CASCADE,
    puncte INT,
    premiu INT,
    castigator BIGINT FOREIGN KEY REFERENCES Jucatori(id)
);
GO

INSERT INTO Turnee
    (locatie, perioada)
VALUES
    ('Wimbeldon', 'Toamna'),
    ('Texas', 'Iarna'),
    ('Cluj Napoca', 'Primavara');

INSERT INTO Jucatori
    (nume, puncte, premii)
VALUES
    ('Mircea', 10, 3500),
    ('George', 2, 10000),
    ('Regina', 3, 4000);

INSERT INTO Arene
    (nume)
VALUES
    ('Arena Nationala'),
    ('La Mircea in curte'),
    ('Rosa Baciu');
GO


CREATE OR ALTER PROCEDURE add_partida
    @turneu BIGINT,
    @jucator1 BIGINT,
    @jucator2 BIGINT,
    @puncte INT,
    @premiu INT,
    @castigator BIGINT,
    @arena BIGINT,
    @data DATETIME2
AS
BEGIN

    IF NOT EXISTS (SELECT 1
    FROM Turnee
    WHERE id=@turneu)
    BEGIN
        RAISERROR('Turneul nu a fos gasit',16,1);
    END;

    IF NOT EXISTS ( SELECT 1
    FROM Jucatori
    WHERE id=@jucator1)
    BEGIN
        RAISERROR('Jucatorul nu a fos gasit',16,1);
    END;

    IF NOT EXISTS ( SELECT 1
    FROM Jucatori
    WHERE id=@jucator2)
    BEGIN
        RAISERROR('Jucatorul nu a fos gasit',16,1);
    END;

    IF NOT EXISTS ( SELECT 1
    FROM Arene
    WHERE id=@arena)
    BEGIN
        RAISERROR('Arena nu a fos gasita',16,1);
    END;

    IF @castigator NOT IN (@jucator1, @jucator2)
    BEGIN
        RAISERROR('Castigatorul trebuie sa fie unul dintre jucatori', 16, 1);
        RETURN;
    END


    INSERT INTO Partide
        (jucator1, jucator2, arena, data, turneu,puncte, premiu, castigator)
    VALUES
        (@jucator1, @jucator2, @arena, @data, @turneu, @puncte, @premiu, @castigator);
END;
GO

CREATE OR ALTER VIEW view_jucatori AS
SELECT TOP 100 PERCENT 
j.nume, COUNT(p.castigator) AS meciuri_castigate
FROM Jucatori j
LEFT JOIN Partide p ON j.id=p.castigator
GROUP BY j.nume
ORDER BY COUNT(j.nume) DESC;
GO

CREATE OR ALTER FUNCTION info_jucator(@jucator BIGINT)
RETURNS TABLE
AS
RETURN(
    SELECT
        j.nume,
        j.puncte + ISNULL(SUM(p.puncte),0) AS puncte,
        j.premii+ISNULL(SUM(p.premiu),0) AS premii
    FROM Jucatori j
    LEFT JOIN Partide p ON j.id = p.castigator
    WHERE j.id = @jucator
    GROUP BY j.nume, j.puncte, j.premii
);
GO

SELECT * FROM info_jucator(1);
SELECT * FROM info_jucator(2);
SELECT * FROM info_jucator(3);