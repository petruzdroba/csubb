CREATE DATABASE IF NOT EXISTS park_portal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE park_portal;

CREATE TABLE IF NOT EXISTS users (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    username       VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    full_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(100) NOT NULL UNIQUE,
    role           ENUM('guest','staff') NOT NULL DEFAULT 'guest',
    remember_token VARCHAR(64)  DEFAULT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS trail_photos (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    uploader_id   INT NOT NULL,
    filename      VARCHAR(255) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    trail_name    VARCHAR(100) NOT NULL,
    description   TEXT,
    uploaded_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uploader_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (username, password, full_name, email, role) VALUES
(
    'staff',
    'staff',
    'John Ranger',
    'john@parkportal.com',
    'staff'
),
(
    'guest',
    'guest',
    'Ana Visitor',
    'ana@example.com',
    'guest'
);
