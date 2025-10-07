-- test.sql: Basic SQL statements for PostgreSQL

-- Drop table if exists
DROP TABLE IF EXISTS Users;

-- Create table
CREATE TABLE Users (
    UserID SERIAL PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    Age INT
);

-- Insert sample data
INSERT INTO Users (Username, Age) VALUES
('Alice', 25),
('Bob', 30),
('Charlie', 22);

-- Select all data
SELECT * FROM Users;

-- Update a row
UPDATE Users SET Age = 26 WHERE Username = 'Alice';

-- Delete a row
DELETE FROM Users WHERE Username = 'Charlie';

-- Final select to see changes
SELECT * FROM Users;
