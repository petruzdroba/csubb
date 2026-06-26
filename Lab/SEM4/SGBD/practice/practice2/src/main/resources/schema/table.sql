DROP TABLE IF EXISTS Note;
DROP TABLE IF EXISTS Elevi;
DROP TABLE IF EXISTS Clase;
DROP TABLE IF EXISTS Materii;
DROP TABLE IF EXISTS Profesori;

CREATE TABLE Profesori
(
    id_profesor BIGINT AUTO_INCREMENT PRIMARY KEY,
    nume        VARCHAR(255)
);

CREATE TABLE Clase
(
    id_clasa     BIGINT AUTO_INCREMENT PRIMARY KEY,
    nume_clasa   VARCHAR(255),
    id_diriginte BIGINT,

    CONSTRAINT fk_clase_diriginte FOREIGN KEY (id_diriginte) REFERENCES Profesori (id_profesor)
);

CREATE TABLE Elevi
(
    id_elev  BIGINT AUTO_INCREMENT PRIMARY KEY,
    nume     VARCHAR(255),
    id_clasa BIGINT,

    CONSTRAINT fk_elevi_clasa FOREIGN KEY (id_clasa) REFERENCES Clase (id_clasa)
);

CREATE TABLE Materii
(
    id_materie BIGINT AUTO_INCREMENT PRIMARY KEY,
    denumire   VARCHAR(255)
);

CREATE TABLE Note
(
    id_nota    BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_elev    BIGINT,
    id_materie BIGINT,
    nota       INT,
    data_notei DATE,

    CONSTRAINT fk_note_elev FOREIGN KEY (id_elev) REFERENCES Elevi (id_elev),
    CONSTRAINT fk_note_materie FOREIGN KEY (id_materie) REFERENCES Materii (id_materie)
);

