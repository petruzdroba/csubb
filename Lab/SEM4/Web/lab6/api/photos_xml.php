<?php
require_once '../includes/db.php';
require_once '../includes/session.php';
require_login();

header('Content-Type: application/xml');

$k      = 3;
$offset = isset($_GET['offset']) ? (int)$_GET['offset'] : 0;
$userId = (int)$_SESSION['user_id'];
$role   = $_SESSION['role'];

if ($role === 'staff') {
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
    ');
} else {
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
    ');
    $stmt->bindValue(':uid', $userId, PDO::PARAM_INT);
}

$stmt->bindValue(':limit',  $k,      PDO::PARAM_INT);
$stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$stmt->execute();
$photos = $stmt->fetchAll(PDO::FETCH_ASSOC);

// Build XML manually
$xml = '<?xml version="1.0" encoding="UTF-8"?>';
$xml .= '<response>';
$xml .= '<total>'  . $total  . '</total>';
$xml .= '<offset>' . $offset . '</offset>';
$xml .= '<k>'      . $k      . '</k>';
$xml .= '<role>'   . htmlspecialchars($role) . '</role>';
$xml .= '<records>';

foreach ($photos as $p) {
    $xml .= '<photo>';
    $xml .= '<id>'          . (int)$p['id']                              . '</id>';
    $xml .= '<uploader_id>' . (int)$p['uploader_id']                     . '</uploader_id>';
    $xml .= '<filename>'    . htmlspecialchars($p['filename'])            . '</filename>';
    $xml .= '<trail_name>'  . htmlspecialchars($p['trail_name'])          . '</trail_name>';
    $xml .= '<description>' . htmlspecialchars($p['description'] ?? '')   . '</description>';
    $xml .= '<uploader>'    . htmlspecialchars($p['uploader'] ?? '')      . '</uploader>';
    $xml .= '</photo>';
}

$xml .= '</records>';
$xml .= '</response>';

echo $xml;