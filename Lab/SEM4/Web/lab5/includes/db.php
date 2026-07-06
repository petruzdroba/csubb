<?php
//   $mysqli  - MYSQL auth only
//   $pdo     – PDO/MySQL connection for uploads
//   $sqlite  – PDO/SQLite connection for logs

define('DB_HOST', 'mysql');
define('DB_USER', 'student');
define('DB_PASS', 'student'); 
define('DB_NAME', 'park_portal');

$mysqli = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
if ($mysqli->connect_error) {
    die('MySQLi connection failed: ' . $mysqli->connect_error);
}
$mysqli->set_charset('utf8mb4');

try {
    $pdo = new PDO(
        'mysql:host=' . DB_HOST . ';dbname=' . DB_NAME . ';charset=utf8mb4',
        DB_USER,
        DB_PASS,
        [
            PDO::ATTR_ERRMODE  => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        ]
    );
} catch (PDOException $e) {
    die('PDO/MySQL connection failed: ' . $e->getMessage());
}

// locally stored log -> server 
$sqliteFile = __DIR__ . '/../data/park_audit.sqlite';
if (!is_dir(dirname($sqliteFile))) {
    mkdir(dirname($sqliteFile), 0755, true);
}
try {
    $sqlite = new PDO('sqlite:' . $sqliteFile, null, null, [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
    ]);
    $sqlite->exec("
        CREATE TABLE IF NOT EXISTS audit_log (
            id         INTEGER PRIMARY KEY AUTOINCREMENT,
            username   TEXT NOT NULL,
            action     TEXT NOT NULL,
            ip_address TEXT,
            logged_at  DATETIME DEFAULT (datetime('now'))
        )
    ");
} catch (PDOException $e) {
    die('PDO/SQLite connection failed: ' . $e->getMessage());
}

// logging the actions
function audit_log(PDO $sqlite, string $username, string $action): void {
    $ip = $_SERVER['REMOTE_ADDR'] ?? 'unknown';
    $stmt = $sqlite->prepare(
        'INSERT INTO audit_log (username, action, ip_address) VALUES (?, ?, ?)'
    );
    $stmt->execute([$username, $action, $ip]);
}
