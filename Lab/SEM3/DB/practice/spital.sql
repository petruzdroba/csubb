USE practice;
GO

DROP TABLE IF EXISTS DoctorPacient;
DROP TABLE IF EXISTS BoliTratemente;
DROP TABLE IF EXISTS BoliPacienti;
DROP TABLE IF EXISTS Tratamente;
DROP TABLE IF EXISTS Boli;
DROP TABLE IF EXISTS Pacient;
DROP TABLE IF EXISTS Doctori;
DROP TABLE IF EXISTS Departament;
GO


CREATE TABLE Departament(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    non_stop BIT
);

CREATE TABLE Doctori(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50),
    data_nastere DATE,
    dpt_id BIGINT FOREIGN KEY REFERENCES Departament(id) ON DELETE CASCADE
);

CREATE TABLE Pacient(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50)
);

CREATE TABLE Boli(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    nume VARCHAR(50)
);

CREATE TABLE Tratamente(
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    descriere VARCHAR(50)
);

CREATE TABLE BoliPacienti(
    boli_id BIGINT FOREIGN KEY REFERENCES Boli(id) ON DELETE CASCADE,
    pacient_id BIGINT FOREIGN KEY REFERENCES Pacient(id) ON DELETE CASCADE,
    CONSTRAINT PK_boli_pacient PRIMARY KEY (boli_id, pacient_id)
);

CREATE TABLE BoliTratemente(
    boli_id BIGINT FOREIGN KEY REFERENCES Boli(id) ON DELETE CASCADE,
    tratament_id BIGINT FOREIGN KEY REFERENCES Tratamente(id) ON DELETE CASCADE,
    CONSTRAINT PK_boli_tratament PRIMARY KEY (boli_id, tratament_id)
);

CREATE TABLE DoctorPacient(
    doctor_id BIGINT FOREIGN KEY REFERENCES Doctori(id) ON DELETE CASCADE,
    pacient_id BIGINT FOREIGN KEY REFERENCES Pacient(id) ON DELETE CASCADE,
    CONSTRAINT PK_doctor_pacient PRIMARY KEY (doctor_id, pacient_id)
);

INSERT INTO Departament (nume, non_stop) 
VALUES ('pediatrie', 0), ('cardiologie', 0), ('garda', 1);

INSERT INTO Departament (nume, non_stop) 
VALUES ('pediatrie67', 0);

SELECT d.nume FROM Departament d WHERE d.nume LIKE '%pediatrie%';
GO

INSERT INTO Pacient (nume) VALUES ('Andrei'), ('Bobica'), ('Georgica');
INSERT INTO Boli (nume) VALUES ('RAcit'), ('Racit Rau'), ('Racit super Rau');
INSERT INTO BoliPacienti (pacient_id, boli_id) VALUES (1,1), (2,1), (3,1);

INSERT INTO Tratamente (descriere) VALUES ('Aerius'), ('BossTop');
INSERT INTO BoliTratemente (boli_id, tratament_id) VALUES (1,1), (1,2), (2,1), (2,2);

GO

CREATE OR ALTER FUNCTION boli_rele()
RETURNS INT
AS
BEGIN
    RETURN(
        SELECT COUNT(*)
        FROM 
        (SELECT b.nume
        FROM Boli b
        JOIN BoliPacienti bp ON bp.boli_id=b.id
        GROUP BY b.nume
        HAVING COUNT(bp.pacient_id)>=3) AS sub
    )
END;
GO

SELECT dbo.boli_rele() AS Boli_RELE;
GO

CREATE OR ALTER VIEW view_tratamente
AS 
SELECT t.descriere
FROM Tratamente t
JOIN BoliTratemente bt ON bt.tratament_id=t.id
WHERE t.descriere LIKE 'A%'
GROUP BY t.descriere
HAVING COUNT(bt.boli_id) >=2;
GO

SELECT * FROM view_tratamente;
GO

CREATE OR ALTER VIEW view_dpt_nonstop
AS
SELECT d.nume
FROM Departament d 
LEFT JOIN Doctori dd ON dd.dpt_id=d.id
WHERE d.non_stop=1
GROUP BY d.nume
HAVING COUNT(dd.id)<=3;
GO

SELECT * FROM view_dpt_nonstop;