CREATE TABLE IF NOT EXISTS users (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    username       TEXT NOT NULL UNIQUE,
    password       TEXT NOT NULL,
    full_name      TEXT NOT NULL,
    email          TEXT NOT NULL,
    role           TEXT NOT NULL DEFAULT 'guest',
    remember_token TEXT
);

CREATE TABLE IF NOT EXISTS trail_photos (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    uploader_id   INTEGER NOT NULL,
    filename      TEXT NOT NULL,
    original_name TEXT NOT NULL,
    trail_name    TEXT NOT NULL,
    description   TEXT,
    uploaded_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (uploader_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS audit_log (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    username   TEXT NOT NULL,
    action     TEXT NOT NULL,
    ip         TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);