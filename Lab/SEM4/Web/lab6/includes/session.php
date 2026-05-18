<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
define('REMEMBER_COOKIE', 'park_remember');
define('REMEMBER_DAYS',   30);

function csrf_token(): string {
    if (empty($_SESSION['csrf_token'])) {
        $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
    }
    return $_SESSION['csrf_token'];
}

function csrf_check(): void {
    // cross site request forgery
    
    $token = $_POST['csrf_token'] ?? '';
    if (!hash_equals($_SESSION['csrf_token'] ?? '', $token)) {
        http_response_code(403);
        die('CSRF token mismatch.');
    }

    return;
}

function try_remember_me(PDO $pdo): void {
    if (isset($_SESSION['user_id'])) return;
    if (empty($_COOKIE[REMEMBER_COOKIE])) return;

    $token = $_COOKIE[REMEMBER_COOKIE];
    $stmt  = $pdo->prepare('SELECT id, username, full_name, role FROM users WHERE remember_token = ?');
    $stmt->execute([$token]);
    if ($row = $stmt->fetch()) {
        $_SESSION['user_id']   = $row['id'];
        $_SESSION['username']  = $row['username'];
        $_SESSION['full_name'] = $row['full_name'];
        $_SESSION['role']      = $row['role'];
        set_remember_cookie($token);
    }
}

function set_remember_cookie(string $token): void {
    setcookie(REMEMBER_COOKIE, $token, [
        'expires'  => time() + REMEMBER_DAYS * 86400,
        'path'     => '/',
        'httponly' => true,
        'samesite' => 'Lax',
    ]);
}

function clear_remember_me(PDO $pdo): void {
    if (!empty($_COOKIE[REMEMBER_COOKIE])) {
        $token = $_COOKIE[REMEMBER_COOKIE];
        $stmt  = $pdo->prepare('UPDATE users SET remember_token = NULL WHERE remember_token = ?');
        $stmt->execute([$token]);
    }
    setcookie(REMEMBER_COOKIE, '', time() - 3600, '/');
}

function require_login(): void {
    if (empty($_SESSION['user_id'])) {
        header('Location: login.php?next=' . urlencode($_SERVER['REQUEST_URI']));
        exit;
    }
}

function require_staff(): void {
    require_login();
    if ($_SESSION['role'] !== 'staff') {
        header('Location: index.html?error=access_denied');
        exit;
    }
}

function is_logged_in(): bool {
    return !empty($_SESSION['user_id']);
}

function is_staff(): bool {
    return is_logged_in() && $_SESSION['role'] === 'staff';
}