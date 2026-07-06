-- Create tables
CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    engine INT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS racers
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    cnp    VARCHAR(50)  NOT NULL,
    engine INT          NOT NULL,
    team   VARCHAR(100) NOT NULL
);

INSERT INTO users (email, password)
VALUES ('admin', '$2a$12$noBNcpyrJhv2j9w9L4vCAO/62mLKg7zicEtKod3.hj6sIIMx.1rnu');

INSERT INTO events (engine)
VALUES (50);
INSERT INTO events (engine)
VALUES (125);
INSERT INTO events (engine)
VALUES (250);
INSERT INTO events (engine)
VALUES (500);
INSERT INTO events (engine)
VALUES (1000);

INSERT INTO racers (name, cnp, engine, team)
VALUES ('Marco Rossi', '1900101123456', 125, 'APRILIA');
INSERT INTO racers (name, cnp, engine, team)
VALUES ('Luca Ferrari', '1851215234567', 250, 'APRILIA');
INSERT INTO racers (name, cnp, engine, team)
VALUES ('Hans Müller', '1780320345678', 500, 'HONDA');
INSERT INTO racers (name, cnp, engine, team)
VALUES ('Pedro Alonso', '1920505456789', 1000, 'HONDA');
INSERT INTO racers (name, cnp, engine, team)
VALUES ('James Walker', '1950710567890', 50, 'YAMAHA');
INSERT INTO racers (name, cnp, engine, team)
VALUES ('Yuki Tanaka', '2001225678901', 125, 'YAMAHA');