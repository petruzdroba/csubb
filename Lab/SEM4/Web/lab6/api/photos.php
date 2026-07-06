<?php
require_once '../includes/db.php';
require_once '../includes/session.php';
require_login();

header('Content-Type: application/json');

$k      = 3;
$offset = isset($_GET['offset']) ? (int)$_GET['offset'] : 0;
$userId = (int)$_SESSION['user_id'];
$role   = $_SESSION['role'];

if ($role === 'staff') {
    // staff = everything
    $countStmt = $pdo->prepare('SELECT COUNT(*) FROM trail_photos');
    $countStmt->execute();
    $total     = (int)$countStmt->fetchColumn();

    $stmt = $pdo->prepare('
        SELECT tp.id, tp.uploader_id, tp.filename, tp.trail_name, tp.description,
               u.full_name AS uploader
        FROM trail_photos tp
        JOIN users u ON u.id = tp.uploader_id 
        ORDER BY tp.id DESC
        LIMIT :limit OFFSET :offset
    '); # get all photos 
} else {
    // guest = own photos
    $countStmt = $pdo->prepare('SELECT COUNT(*) FROM trail_photos WHERE uploader_id = :uid');
    $countStmt->execute([':uid' => $userId]);
    $total = (int)$countStmt->fetchColumn();

    $stmt = $pdo->prepare('
        SELECT tp.id, tp.uploader_id, tp.filename, tp.trail_name, tp.description,
               NULL AS uploader
        FROM trail_photos tp
        WHERE tp.uploader_id = :uid
        ORDER BY tp.id DESC
        LIMIT :limit OFFSET :offset
    ');# get photos by logged in user id
    $stmt->bindValue(':uid', $userId, PDO::PARAM_INT);
}

$stmt->bindValue(':limit',  $k,      PDO::PARAM_INT);
$stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$stmt->execute(); # execute whichever query was prpd based on role
$photos = $stmt->fetchAll(PDO::FETCH_ASSOC);

echo json_encode([
    'total'   => $total,
    'offset'  => $offset,
    'k'       => $k,
    'role'    => $role,
    'records' => $photos,
]); # output json to http respose body