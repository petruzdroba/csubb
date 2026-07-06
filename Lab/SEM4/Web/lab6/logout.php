<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

$username = $_SESSION['username'] ?? 'unknown';

audit_log($sqlite, $username, 'logout');

clear_remember_me($pdo);

// Destroy session
$_SESSION = [];
session_destroy();

header('Location: login.php');
exit;
?>