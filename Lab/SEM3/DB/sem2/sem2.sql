CREATE TABLE CategoriiVizitatori(
    id INT PRIMARY KEY,
    nume VARCHAR(50)
);

CREATE TABLE Vizitatori(
    id INT PRIMARY KEY,
    nume VARCHAR(50),
    email VARCHAR(50),
    categorie_id INT REFERENCES CategoriiVizitatori(id)
);

CREATE TABLE Sectiune(
    id INT PRIMARY KEY,
    nume VARCHAR(50),
    descriere VARCHAR(50)
);

CREATE TABLE Atractie(
    id INT PRIMARY KEY,
    nume VARCHAR(50),
    descriere VARCHAR(50),
    varsta INT,
    sectiune INT REFERENCES Sectiune(id)
);

CREATE TABLE Nota(
    nota REAL CHECK(nota between 1 and 10),
    cod_a INT REFERENCES Atractie(id),
    cod_v INT REFERENCES Vizitatori(id),
    PRIMARY KEY(cod_v, cod_a)
);

INSERT INTO Sectiune (id ,nume, descriere) VALUES
 (1, 'sectiunea1', 'cea mai mare sectiune'),
 (2, 'sectiunea2', 'cea mai mica sectiune'),
 (3, 'sectiunea3', 'sectiune de copii'),
 (4, 'sectiunea4', 'sectiunea cu clovni'),
 (5, 'sectiunea5', 'sectiunea de masinute'),
 (6, 'sectiunea6', 'sectiunea de joculete'),
 (7, 'sectiunea7', 'sectiune petting zoo');

 INSERT INTO Atractie (id, nume, descriere, varsta, sectiune) VALUES
 (1, 'roller coaster', 'muntele mare', 12, 1),
 (2, 'carusel', 'carusel cu caluti', 3, 2),
 (3, 'lazy river', 'splash town', 8, 3),
 (4, 'clown extravaganza', 'meet and greet cu clovni', 4, 4),
 (5, 'F1 simulator', 'cursa mica de f1', 12, 5),
 (6, 'pacanele', 'pacanele superbet', 18, 6),
 (7, 'goat', 'lebronnnnnn', 1, 7);

INSERT INTO CategoriiVizitatori (id, nume) VALUES
(1, 'Copii'),
(2, 'Adulti'),
(3, 'Seniori'),
(4, 'Elevi'),
(5, 'Studenti'),
(6, 'Familii'),
(7, 'Turisti');

INSERT INTO Vizitatori (id, nume, email, categorie_id) VALUES
(1, 'Ana Popescu', 'ana@example.com', 1),
(2, 'Ion Ionescu', 'ion@example.com', 2),
(3, 'Maria Vasilescu', 'maria@example.com', 4),
(4, 'George Petrescu', 'george@example.com', 3),
(5, 'Elena Stan', 'elena@example.com', 5),
(6, 'Darius Enache', 'darius@example.com', 6),
(7, 'Radu Marinescu', 'radu@example.com', 7);

INSERT INTO Nota (nota, cod_a, cod_v) VALUES
(8.5, 1, 1),
(7.0, 2, 2),
(9.2, 3, 3),
(6.8, 4, 4),
(10.0, 5, 5),
(5.5, 6, 6),
(7.7, 7, 7);

UPDATE Sectiune SET descriere='cea mai veche sectiune'
WHERE nume='sectiune1';

UPDATE Atractie SET descriere='bumper cars'
WHERE nume='F1 simulator';

UPDATE CategoriiVizitatori SET nume='Minori'
WHERE id=1;

UPDATE Vizitatori SET email='ana.boss@yahoo.com'
WHERE id=1;

UPDATE Nota SET nota=7.1
WHERE cod_v=7;

DELETE FROM Nota WHERE nota='5.5';
DELETE FROM Atractie WHERE id=6;

SELECT * FROM CategoriiVizitatori WHERE nume='Seniori' OR nume='Minori';
SELECT * FROM Sectiune WHERE nume LIKE 'C%';
SELECT * FROM Sectiune WHERE nume LIKE '%_%n'; 

INSERT INTO Vizitatori (id, nume, email, categorie_id) VALUES
(8, 'Johnny Cage', 'johnny.cage.smecherie@gmail.com', 1);
SELECT V.id, V.nume,V.email 
FROM Vizitatori V
LEFT JOIN Nota n ON V.id = n.cod_v 
WHERE n.cod_v IS NULL;

SELECT v.nume, n.nota, a.nume
FROM Vizitatori v
JOIN Nota n ON v.id = n.cod_v
JOIN Atractie a ON n.cod_a = a.id;

SELECT v.nume, COUNT(cod_v) FROM Vizitatori v
LEFT JOIN Nota n ON n.cod_v = v.id
GROUP BY v.id, v.nume;

INSERT INTO Nota (nota, cod_a, cod_v) VALUES
(8.5, 1, 8);
SELECT DISTINCT n.nota FROM Nota n WHERE nota IN (SELECT nota from Nota);

SELECT * FROM Sectiune;
SELECT * FROM Atractie;
SELECT * FROM CategoriiVizitatori;
SELECT * FROM Vizitatori;
SELECT * FROM Nota;