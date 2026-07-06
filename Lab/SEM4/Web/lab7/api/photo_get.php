<?php
require_once '../includes/db.php';
require_once '../includes/session.php';
require_login();

header('Content-Type: application/json');

$userId = (int)$_SESSION['user_id'];
$role   = $_SESSION['role'];
$id     = (int)($_GET['id'] ?? 0);

$stmt = $pdo->prepare('
    SELECT id, trail_name, description
    FROM trail_photos
    WHERE id = :id AND (uploader_id = :uid OR :role = \'staff\')
');
$stmt->execute([':id' => $id, ':uid' => $userId, ':role' => $role]);
$photo = $stmt->fetch(PDO::FETCH_ASSOC);

if (!$photo) {
    http_response_code(404);
    echo json_encode(['error' => 'Not found']);
    exit;
}

// encode response as json
echo json_encode($photo);