USE practice;
GO

DROP TABLE IF EXISTS Librarie;
DROP TABLE IF EXISTS CartiAutori;
DROP TABLE IF EXISTS Carti;
DROP TABLE IF EXISTS Domenii;
DROP TABLE IF EXISTS Autori;


CREATE TABLE Librarie
(
    id INT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    adresa VARCHAR(50)
);

CREATE TABLE Domenii
(
    id INT PRIMARY KEY IDENTITY(1,1),
    descriere VARCHAR(70)
);

CREATE TABLE Autori
(
    id INT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    prenume VARCHAR(50)
);

CREATE TABLE Carti
(
    id INT PRIMARY KEY IDENTITY(1,1),
    titlu VARCHAR(50),
    domeniu_id INT REFERENCES Domenii(id) ON DELETE CASCADE,
    librarie_id INT REFERENCES Librarie(id) ON DELETE CASCADE,
    data_ach DATE
);

CREATE TABLE CartiAutori
(
    carte INT REFERENCES Carti(id) ON DELETE CASCADE,
    autor INT REFERENCES Autori(id) ON DELETE CASCADE,
    CONSTRAINT PK_CartiAutori PRIMARY KEY (carte, autor)
);
GO

INSERT INTO Librarie
    (nume, adresa)
VALUES
    ('Libris', 'Str. Kogalniceanu nr.6'),
    ('Libraria Universitatii', 'Bvd. 1 Decembrie 1990'),
    ('Eu LIBRARIE', 'Livezeni 13');
INSERT INTO Domenii
    (descriere)
VALUES
    ('Literatura'),
    ('Stintiific');
INSERT INTO Autori
    (nume, prenume)
VALUES
    ('Lucian', 'Blaga'),
    ('Mihai', 'Eminescu');
INSERT INTO Carti
    (titlu, domeniu_id, librarie_id, data_ach)
VALUES
    ('Poezii de Lucian', 1, 1, '2026-01-01'),
    ('Floarea Albastra', 2, 2, '2025-12-12'),
    ('Floarea Albastra2', 2, 2, '2025-12-12'),
    ('Floarea Albastra3', 2, 2, '2025-12-12'),
    ('Floarea Albastra4', 2, 2, '2025-12-12'),
    ('Floarea Albastra5', 2, 2, '2025-12-12'),
    ('BAI EU', 1, 3, '2014-11-12'),
    ('BAI EU', 1, 3, '2014-11-12'),
    ('BAI EU', 1, 3, '2014-11-12'),
    ('BAI EU', 1, 3, '2014-11-12'),
    ('BAI EU', 1, 3, '2014-11-12')
GO

CREATE OR ALTER PROCEDURE add_autor
    @nume VARCHAR(50),
    @prenume VARCHAR(50),
    @carte INT
AS
BEGIN
    DECLARE @autor_id INT;

    IF NOT EXISTS (SELECT 1
    FROM Autori
    WHERE nume=@nume AND prenume=@prenume)
    BEGIN
        INSERT INTO Autori
            (nume, prenume)
        VALUES
            (@nume, @prenume);
    END

    SET @autor_id = (SELECT id
    FROM Autori
    WHERE nume=@nume AND prenume=@prenume);

    IF EXISTS (SELECT 1
    FROM CartiAutori
    WHERE carte=@carte AND autor=@autor_id)
    BEGIN
        RAISERROR ('Autorul si Cartea sunt deja asociati',16,1);
    END
    ELSE
    BEGIN
        INSERT INTO CartiAutori
            (carte, autor)
        VALUES
            (@carte, @autor_id);
    END
END;
GO

EXEC add_autor 'Lucian', 'Blaga3', 3;
EXEC add_autor 'Lucian', 'Blaga2', 3;
EXEC add_autor 'Lucian', 'Blaga4', 3;
GO

CREATE VIEW view_2010 AS
SELECT TOP 100 PERCENT 
COUNT(*) AS counted_book, l.nume
FROM Carti c
JOIN Librarie l ON c.librarie_id=l.id
GROUP BY l.nume
HAVING COUNT(c.id) >= 5
ORDER BY l.nume DESC;
GO

CREATE OR ALTER FUNCTION nr_autori(@nr INT)
RETURNS TABLE
AS
RETURN
(
    SELECT c.titlu AS Titlu, l.nume AS Libraria, l.adresa AS Adresa,  COUNT(*) AS NrAutori
    FROM Carti c
    JOIN Librarie l ON l.id=c.id
    JOIN CartiAutori ca ON ca.carte=c.id
    GROUP BY c.titlu, l.nume, l.adresa
    HAVING COUNT(c.id)=@nr
);
GO

SELECT * FROM nr_autori(3);