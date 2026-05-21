<?php
define('DB_PATH', __DIR__ . '/../data/park_portal.sqlite');

try {
    $pdo = new PDO('sqlite:' . DB_PATH);
    $pdo->setAttribute(PDO::ATTR_ERRMODE,            PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
    $pdo->exec('PRAGMA foreign_keys = ON;');
} catch (PDOException $e) {
    die('Database connection failed: ' . $e->getMessage());
}

$sqlite = $pdo;

function audit_log(PDO $db, string $username, string $action): void {
    $ip   = $_SERVER['REMOTE_ADDR'] ?? 'unknown';
    $stmt = $db->prepare(
        'INSERT INTO audit_log (username, action, ip) VALUES (:u, :a, :ip)'
    );
    $stmt->execute([':u' => $username, ':a' => $action, ':ip' => $ip]);
}