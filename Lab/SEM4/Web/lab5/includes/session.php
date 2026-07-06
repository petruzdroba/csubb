<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

define('REMEMBER_COOKIE', 'park_remember');
define('REMEMBER_DAYS',   30);


function try_remember_me(mysqli $mysqli): void {
    if (isset($_SESSION['user_id'])) {
        return; // already logged in
    }
    if (empty($_COOKIE[REMEMBER_COOKIE])) {
        return;
    }

    $token = $_COOKIE[REMEMBER_COOKIE];
    $stmt  = $mysqli->prepare(
        'SELECT id, username, full_name, role FROM users WHERE remember_token = ?'
    );
    $stmt->bind_param('s', $token);
    $stmt->execute();
    $stmt->bind_result($id, $username, $full_name, $role);
    if ($stmt->fetch()) {
        $_SESSION['user_id']   = $id;
        $_SESSION['username']  = $username;
        $_SESSION['full_name'] = $full_name;
        $_SESSION['role']      = $role;

        set_remember_cookie($token);
    }
    $stmt->close();
}

function set_remember_cookie(string $token): void {
    setcookie(
        REMEMBER_COOKIE,
        $token,
        [
            'expires'  => time() + REMEMBER_DAYS * 86400,
            'path'     => '/',
            'httponly' => true,
            'samesite' => 'Lax',
        ]
    );
}

function clear_remember_me(mysqli $mysqli): void {
    if (!empty($_COOKIE[REMEMBER_COOKIE])) {
        $token = $_COOKIE[REMEMBER_COOKIE];
        $stmt  = $mysqli->prepare(
            'UPDATE users SET remember_token = NULL WHERE remember_token = ?'
        );// remove from database

        $stmt->bind_param('s', $token);
        $stmt->execute();
        $stmt->close();
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
