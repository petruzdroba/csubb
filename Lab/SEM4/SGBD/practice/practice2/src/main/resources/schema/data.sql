INSERT INTO Profesori (nume)
VALUES ('Ion Popescu');
INSERT INTO Profesori (nume)
VALUES ('Maria Ionescu');
INSERT INTO Profesori (nume)
VALUES ('Alexandru Georgescu');
INSERT INTO Profesori (nume)
VALUES ('Elena Vasile');

INSERT INTO Clase (nume_clasa, id_diriginte)
VALUES ('10A', 1);
INSERT INTO Clase (nume_clasa, id_diriginte)
VALUES ('10B', 2);
INSERT INTO Clase (nume_clasa, id_diriginte)
VALUES ('11A', 3);

INSERT INTO Elevi (nume, id_clasa)
VALUES ('Andrei Micu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Bianca Ilie', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Cristian Dumitrescu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Diana Marinescu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Emy Stoian', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Florin Georgiou', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Gabriela Popa', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Horia Bratu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Ioana Mihai', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Jovan Markovic', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Klara Szabo', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Larisa Cojocaru', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Mircea Voiculescu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Natalia Stancu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Octavian Luca', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Petronela Dobrescu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Quentin Lacroix', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Roxana Badea', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Silviu Georgescu', 1);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Teodora Enache', 1);

INSERT INTO Elevi (nume, id_clasa)
VALUES ('Adrian Stefan', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Bogdan Nicu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Camelia Radu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Doru Vasaru', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Elvira Popescu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Fedor Ionescu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Giorgiana Marius', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Horatiu Ciocan', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Isabela Marcu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Josif Tanase', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Kristof Nemeth', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Livia Szocs', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Matei Rusu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Nicoleta Popescu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Ovidiu Neagu', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Pamela Dragos', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Quincy Durand', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Ruxandra Voinea', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Sergei Petrov', 2);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Tatiana Bostan', 2);

INSERT INTO Elevi (nume, id_clasa)
VALUES ('Anton Vlad', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Bianca Stoica', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Claudiu Ionita', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Delia Chiriac', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Eugen Florea', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Felicia Nastase', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Gelu Sandu', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Hermina Zarnescu', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Iris Toma', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Julius Kovacs', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Karolina Olah', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Leonard Mincu', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Magdalena Barbu', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Nicu Badulescu', 3);
INSERT INTO Elevi (nume, id_clasa)
VALUES ('Octavia Moraru', 3);

INSERT INTO Materii (denumire)
VALUES ('Numerologie');
INSERT INTO Materii (denumire)
VALUES ('Navigatie');
INSERT INTO Materii (denumire)
VALUES ('Neurobiologie');
INSERT INTO Materii (denumire)
VALUES ('Nutritie');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (1, 1, 8, '2024-09-15');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (1, 1, 7, '2024-10-05');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (1, 1, 9, '2024-10-20');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (1, 1, 8, '2024-11-10');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (1, 1, 7, '2024-12-01');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (1, 1, 9, '2025-01-15');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (2, 1, 9, '2024-09-16');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (2, 1, 8, '2024-10-06');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (2, 1, 10, '2024-10-21');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (2, 1, 9, '2024-11-11');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (2, 1, 8, '2024-12-02');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (2, 1, 9, '2025-01-16');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (3, 1, 7, '2024-09-17');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (3, 1, 8, '2024-10-07');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (3, 1, 6, '2024-10-22');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (3, 1, 7, '2024-11-12');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (3, 1, 6, '2024-12-03');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (3, 1, 8, '2025-01-17');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (4, 1, 6, '2024-09-18');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (4, 1, 5, '2024-10-08');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (4, 1, 6, '2024-10-23');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (4, 1, 5, '2024-11-13');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (4, 1, 7, '2024-12-04');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (4, 1, 6, '2025-01-18');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (5, 1, 5, '2024-09-19');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (5, 1, 6, '2024-10-09');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (5, 1, 5, '2024-10-24');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (5, 1, 6, '2024-11-14');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (5, 1, 5, '2024-12-05');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (5, 1, 6, '2025-01-19');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (6, 2, 8, '2024-09-20');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (6, 2, 9, '2024-10-10');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (6, 2, 8, '2024-10-25');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (6, 2, 9, '2024-11-15');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (6, 2, 7, '2024-12-06');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (6, 2, 9, '2025-01-20');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (7, 2, 9, '2024-09-21');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (7, 2, 10, '2024-10-11');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (7, 2, 9, '2024-10-26');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (7, 2, 10, '2024-11-16');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (7, 2, 9, '2024-12-07');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (7, 2, 10, '2025-01-21');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (8, 3, 7, '2024-09-22');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (8, 3, 8, '2024-10-12');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (8, 3, 7, '2024-10-27');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (8, 3, 8, '2024-11-17');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (8, 3, 6, '2024-12-08');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (8, 3, 8, '2025-01-22');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (9, 3, 8, '2024-09-23');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (9, 3, 9, '2024-10-13');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (9, 3, 8, '2024-10-28');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (9, 3, 9, '2024-11-18');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (9, 3, 8, '2024-12-09');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (9, 3, 9, '2025-01-23');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (10, 4, 6, '2024-09-24');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (10, 4, 7, '2024-10-14');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (10, 4, 6, '2024-10-29');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (10, 4, 7, '2024-11-19');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (10, 4, 5, '2024-12-10');
INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (10, 4, 7, '2025-01-24');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (11, 1, 8, '2025-02-01'),
       (11, 2, 9, '2025-02-02'),
       (11, 3, 8, '2025-02-03'),
       (11, 4, 9, '2025-02-04'),
       (12, 1, 7, '2025-02-01'),
       (12, 2, 8, '2025-02-02'),
       (12, 3, 7, '2025-02-03'),
       (12, 4, 8, '2025-02-04'),
       (13, 1, 9, '2025-02-01'),
       (13, 2, 9, '2025-02-02'),
       (13, 3, 10, '2025-02-03'),
       (13, 4, 9, '2025-02-04'),
       (14, 1, 6, '2025-02-01'),
       (14, 2, 7, '2025-02-02'),
       (14, 3, 6, '2025-02-03'),
       (14, 4, 7, '2025-02-04'),
       (15, 1, 8, '2025-02-01'),
       (15, 2, 8, '2025-02-02'),
       (15, 3, 9, '2025-02-03'),
       (15, 4, 8, '2025-02-04'),
       (16, 1, 7, '2025-02-01'),
       (16, 2, 7, '2025-02-02'),
       (16, 3, 8, '2025-02-03'),
       (16, 4, 7, '2025-02-04'),
       (17, 1, 9, '2025-02-01'),
       (17, 2, 8, '2025-02-02'),
       (17, 3, 9, '2025-02-03'),
       (17, 4, 8, '2025-02-04'),
       (18, 1, 6, '2025-02-01'),
       (18, 2, 7, '2025-02-02'),
       (18, 3, 7, '2025-02-03'),
       (18, 4, 6, '2025-02-04'),
       (19, 1, 8, '2025-02-01'),
       (19, 2, 9, '2025-02-02'),
       (19, 3, 8, '2025-02-03'),
       (19, 4, 9, '2025-02-04'),
       (20, 1, 7, '2025-02-01'),
       (20, 2, 8, '2025-02-02'),
       (20, 3, 7, '2025-02-03'),
       (20, 4, 8, '2025-02-04');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (21, 1, 7, '2025-02-05'),
       (21, 2, 6, '2025-02-06'),
       (21, 3, 7, '2025-02-07'),
       (21, 4, 6, '2025-02-08'),
       (22, 1, 8, '2025-02-05'),
       (22, 2, 7, '2025-02-06'),
       (22, 3, 8, '2025-02-07'),
       (22, 4, 7, '2025-02-08'),
       (23, 1, 6, '2025-02-05'),
       (23, 2, 6, '2025-02-06'),
       (23, 3, 7, '2025-02-07'),
       (23, 4, 6, '2025-02-08'),
       (24, 1, 7, '2025-02-05'),
       (24, 2, 8, '2025-02-06'),
       (24, 3, 7, '2025-02-07'),
       (24, 4, 8, '2025-02-08'),
       (25, 1, 5, '2025-02-05'),
       (25, 2, 6, '2025-02-06'),
       (25, 3, 5, '2025-02-07'),
       (25, 4, 6, '2025-02-08'),
       (26, 1, 8, '2025-02-05'),
       (26, 2, 8, '2025-02-06'),
       (26, 3, 7, '2025-02-07'),
       (26, 4, 8, '2025-02-08'),
       (27, 1, 7, '2025-02-05'),
       (27, 2, 7, '2025-02-06'),
       (27, 3, 8, '2025-02-07'),
       (27, 4, 7, '2025-02-08'),
       (28, 1, 6, '2025-02-05'),
       (28, 2, 7, '2025-02-06'),
       (28, 3, 6, '2025-02-07'),
       (28, 4, 7, '2025-02-08'),
       (29, 1, 8, '2025-02-05'),
       (29, 2, 7, '2025-02-06'),
       (29, 3, 8, '2025-02-07'),
       (29, 4, 7, '2025-02-08'),
       (30, 1, 7, '2025-02-05'),
       (30, 2, 6, '2025-02-06'),
       (30, 3, 7, '2025-02-07'),
       (30, 4, 6, '2025-02-08'),

       (31, 1, 8, '2025-02-05'),
       (31, 2, 8, '2025-02-06'),
       (31, 3, 9, '2025-02-07'),
       (31, 4, 8, '2025-02-08'),
       (32, 1, 6, '2025-02-05'),
       (32, 2, 7, '2025-02-06'),
       (32, 3, 6, '2025-02-07'),
       (32, 4, 7, '2025-02-08'),
       (33, 1, 7, '2025-02-05'),
       (33, 2, 8, '2025-02-06'),
       (33, 3, 7, '2025-02-07'),
       (33, 4, 8, '2025-02-08'),
       (34, 1, 9, '2025-02-05'),
       (34, 2, 8, '2025-02-06'),
       (34, 3, 9, '2025-02-07'),
       (34, 4, 8, '2025-02-08'),
       (35, 1, 5, '2025-02-05'),
       (35, 2, 6, '2025-02-06'),
       (35, 3, 5, '2025-02-07'),
       (35, 4, 6, '2025-02-08'),
       (36, 1, 7, '2025-02-05'),
       (36, 2, 7, '2025-02-06'),
       (36, 3, 8, '2025-02-07'),
       (36, 4, 7, '2025-02-08'),
       (37, 1, 8, '2025-02-05'),
       (37, 2, 9, '2025-02-06'),
       (37, 3, 8, '2025-02-07'),
       (37, 4, 9, '2025-02-08'),
       (38, 1, 6, '2025-02-05'),
       (38, 2, 6, '2025-02-06'),
       (38, 3, 7, '2025-02-07'),
       (38, 4, 6, '2025-02-08'),
       (39, 1, 8, '2025-02-05'),
       (39, 2, 7, '2025-02-06'),
       (39, 3, 8, '2025-02-07'),
       (39, 4, 7, '2025-02-08'),
       (40, 1, 7, '2025-02-05'),
       (40, 2, 8, '2025-02-06'),
       (40, 3, 7, '2025-02-07'),
       (40, 4, 8, '2025-02-08');

INSERT INTO Note (id_elev, id_materie, nota, data_notei)
VALUES (41, 1, 9, '2025-02-10'),
       (41, 2, 9, '2025-02-11'),
       (41, 3, 8, '2025-02-12'),
       (41, 4, 9, '2025-02-13'),
       (42, 1, 8, '2025-02-10'),
       (42, 2, 8, '2025-02-11'),
       (42, 3, 9, '2025-02-12'),
       (42, 4, 8, '2025-02-13'),
       (43, 1, 7, '2025-02-10'),
       (43, 2, 8, '2025-02-11'),
       (43, 3, 7, '2025-02-12'),
       (43, 4, 8, '2025-02-13'),
       (44, 1, 10, '2025-02-10'),
       (44, 2, 9, '2025-02-11'),
       (44, 3, 10, '2025-02-12'),
       (44, 4, 9, '2025-02-13'),
       (45, 1, 8, '2025-02-10'),
       (45, 2, 7, '2025-02-11'),
       (45, 3, 8, '2025-02-12'),
       (45, 4, 7, '2025-02-13'),
       (46, 1, 9, '2025-02-10'),
       (46, 2, 8, '2025-02-11'),
       (46, 3, 9, '2025-02-12'),
       (46, 4, 8, '2025-02-13'),
       (47, 1, 7, '2025-02-10'),
       (47, 2, 7, '2025-02-11'),
       (47, 3, 8, '2025-02-12'),
       (47, 4, 7, '2025-02-13'),
       (48, 1, 8, '2025-02-10'),
       (48, 2, 9, '2025-02-11'),
       (48, 3, 8, '2025-02-12'),
       (48, 4, 9, '2025-02-13'),
       (49, 1, 6, '2025-02-10'),
       (49, 2, 7, '2025-02-11'),
       (49, 3, 6, '2025-02-12'),
       (49, 4, 7, '2025-02-13'),
       (50, 1, 8, '2025-02-10'),
       (50, 2, 8, '2025-02-11'),
       (50, 3, 9, '2025-02-12'),
       (50, 4, 8, '2025-02-13'),
       (51, 1, 9, '2025-02-10'),
       (51, 2, 9, '2025-02-11'),
       (51, 3, 8, '2025-02-12'),
       (51, 4, 9, '2025-02-13'),
       (52, 1, 7, '2025-02-10'),
       (52, 2, 8, '2025-02-11'),
       (52, 3, 7, '2025-02-12'),
       (52, 4, 8, '2025-02-13'),
       (53, 1, 8, '2025-02-10'),
       (53, 2, 7, '2025-02-11'),
       (53, 3, 8, '2025-02-12'),
       (53, 4, 7, '2025-02-13'),
       (54, 1, 9, '2025-02-10'),
       (54, 2, 8, '2025-02-11'),
       (54, 3, 9, '2025-02-12'),
       (54, 4, 8, '2025-02-13'),
       (55, 1, 8, '2025-02-10'),
       (55, 2, 9, '2025-02-11'),
       (55, 3, 8, '2025-02-12'),
       (55, 4, 9, '2025-02-13');