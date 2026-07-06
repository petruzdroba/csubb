<?php
require_once '../includes/db.php';
require_once '../includes/session.php';
require_login();

header('Content-Type: application/json');

$userId = (int)$_SESSION['user_id'];
$role   = $_SESSION['role'];
$data   = json_decode(file_get_contents('php://input'), true);

$id          = (int)($data['id']          ?? 0);
$trailName   = trim($data['trail_name']   ?? '');
$description = trim($data['description']  ?? '');

if (!$id || empty($trailName)) {
    http_response_code(400);
    echo json_encode(['error' => 'Invalid data']);
    exit;
}

$sel = $pdo->prepare('SELECT id FROM trail_photos WHERE id = :id AND (uploader_id = :uid OR :role = \'staff\')');
$sel->execute([':id' => $id, ':uid' => $userId, ':role' => $role]);

if (!$sel->fetch()) {
    http_response_code(403);
    // if uid or stafgf dont match
    echo json_encode(['error' => 'Forbidden']);
    exit;
}

$stmt = $pdo->prepare('UPDATE trail_photos SET trail_name = :trail, description = :desc WHERE id = :id');
$stmt->execute([':trail' => $trailName, ':desc' => $description, ':id' => $id]);

echo json_encode(['success' => true]);