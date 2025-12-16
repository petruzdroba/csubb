DROP TABLE IF EXISTS RouteStations CASCADE;
DROP TABLE IF EXISTS Routes CASCADE;
DROP TABLE IF EXISTS Stations CASCADE;
DROP TABLE IF EXISTS Trains CASCADE;
DROP TABLE IF EXISTS TypeTrains CASCADE;

CREATE TABLE IF NOT EXISTS TypeTrains(
    id INT PRIMARY KEY,
    description VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Trains(
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    typetrain_id INT REFERENCES TypeTrains(id)
);


CREATE TABLE IF NOT EXISTS Routes(
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    train_id INT REFERENCES Trains(id)
);

CREATE TABLE IF NOT EXISTS Stations(
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS RouteStations(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    route_id INT REFERENCES Routes(id),
    station_id INT REFERENCES Stations(id),
    hour_arrive INT CHECK (hour_arrive BETWEEN 0 and 23),
    minute_arrive INT CHECK (minute_arrive BETWEEN 0 and 59),
    hour_depart INT CHECK (hour_depart BETWEEN 0 and 23),
    minute_depart INT CHECK (minute_depart BETWEEN 0 and 59)
);

CREATE OR REPLACE PROCEDURE InsertRoute(
    IN p_route_id INT,
    IN p_station_id INT,
    IN p_station_name VARCHAR(50),
    IN p_hour_arrive INT ,
    IN p_minute_arrive INT ,
    IN p_hour_depart INT ,
    IN p_minute_depart INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS(SELECT 1 FROM Routes WHERE id=p_route_id) THEN
        RAISE EXCEPTION 'Routes ID % does not exist', p_route_id;
    END IF;

    IF p_hour_arrive NOT BETWEEN 0 AND 23 OR p_hour_depart NOT BETWEEN 0 AND 23 THEN
        RAISE EXCEPTION 'Hour must be between 0 and 23';
    END IF;

    IF p_minute_arrive NOT BETWEEN 0 AND 59 OR p_minute_depart NOT BETWEEN 0 AND 59 THEN
        RAISE EXCEPTION 'Minute must be between 0 and 59';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Stations WHERE id=p_station_id) THEN
        INSERT INTO Stations (id, name) VALUES (p_station_id, p_station_name) RETURNING id INTO p_station_id;
    END IF;

    IF EXISTS (SELECT 1 FROM RouteStations WHERE route_id=p_route_id AND station_id=p_station_id) THEN
        UPDATE RouteStations
        SET hour_arrive=p_hour_arrive,
            minute_arrive=p_minute_arrive,
            hour_depart=p_hour_depart,
            minute_depart=p_minute_depart
        WHERE route_id=p_route_id AND station_id=p_station_id;
    ELSE
        INSERT INTO RouteStations (route_id, station_id, hour_arrive, minute_arrive, hour_depart, minute_depart)
        VALUES (p_route_id, p_station_id, p_hour_arrive, p_minute_arrive, p_hour_depart, p_minute_depart);
    END IF;
END;
$$;

INSERT INTO TypeTrains (id, description) VALUES
(1, 'Express'),
(2, 'Regional');

INSERT INTO Trains (id, name, typetrain_id) VALUES
(1, 'FastLine', 1),
(2, 'LocalTrack', 2);

INSERT INTO Routes (id, name, train_id) VALUES
(1, 'City A - City B', 1),
(2, 'Town X - Town Y', 2);

INSERT INTO Stations (id, name) VALUES
(1, 'Central Station'),
(2, 'North Station'),
(3, 'South Station');

SELECT * FROM trains;
SELECT * FROM routes;
SELECT * FROM stations;


CALL InsertRoute(1, 1, 'Central Station', 9, 0, 9, 15);
CALL InsertRoute(1, 2, 'North Station', 9, 30, 9, 45);
CALL InsertRoute(1, 3, 'South Station', 10, 0, 10, 15);
CALL InsertRoute(2, 2, 'North Station', 11, 0, 11, 20);
CALL InsertRoute(2, 3, 'South Station', 11, 30, 11, 50);

DROP VIEW viewroutestations;

CREATE OR REPLACE VIEW ViewRouteStations AS
SELECT 
    r.name AS route_name,
    s.name AS station_name,
    rs.hour_arrive,
    rs.minute_arrive,
    rs.hour_depart,
    rs.minute_depart
FROM Routes r
JOIN RouteStations rs ON r.id = rs.route_id
JOIN Stations s ON rs.station_id = s.id
ORDER BY r.id, rs.hour_arrive, rs.minute_arrive;

SELECT * FROM viewroutestations;

CREATE OR REPLACE VIEW ViewFullRoutes AS
SELECT
    r.name AS route_name
    FROM Routes r
    JOIN (SELECT route_id FROM RouteStations GROUP BY route_id
    HAVING COUNT (station_id) = (SELECT COUNT(*) FROM stations)
    ) cr ON cr.route_id = r.id;

SELECT * FROM viewfullroutes;