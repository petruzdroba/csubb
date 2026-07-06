<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

try_remember_me($pdo);
require_login();

$userId = (int)$_SESSION['user_id'];
$role   = $_SESSION['role'];
$fullName = $_SESSION['full_name'];

$k      = 3;
$page   = max(1, (int)($_GET['page'] ?? 1)); //get page number from the query
$offset = ($page - 1) * $k;// calc offset based on idx

if ($role === 'staff') {
    $countStmt = $pdo->query('SELECT COUNT(*) FROM trail_photos');
    $total     = (int)$countStmt->fetchColumn();

    $stmt = $pdo->prepare('
        SELECT tp.id, tp.filename, tp.trail_name, tp.description, u.full_name AS uploader
        FROM trail_photos tp
        JOIN users u ON u.id = tp.uploader_id
        ORDER BY tp.id DESC
        LIMIT :limit OFFSET :offset
    ');
} else {
    $countStmt = $pdo->prepare('SELECT COUNT(*) FROM trail_photos WHERE uploader_id = :uid');
    $countStmt->execute([':uid' => $userId]);
    $total = (int)$countStmt->fetchColumn(); //exec count 

    $stmt = $pdo->prepare('
        SELECT tp.id, tp.filename, tp.trail_name, tp.description, NULL AS uploader
        FROM trail_photos tp
        WHERE tp.uploader_id = :uid
        ORDER BY tp.id DESC
        LIMIT :limit OFFSET :offset
    ');
    $stmt->bindValue(':uid', $userId, PDO::PARAM_INT);
}

$stmt->bindValue(':limit',  $k,      PDO::PARAM_INT);
$stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
$stmt->execute(); // based on role we select the data by uid or all
$photos = $stmt->fetchAll();

$pages = (int)ceil($total / $k);
$page  = min($page, max(1, $pages));
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Photos – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
<div>
    <div><a href="dashboard.php">Back to Dashboard</a></div>

        <h2><?= $role === 'staff' ? 'All Trail Photos' : 'My Trail Photos' ?></h2>

        <table border="1" cellpadding="8" cellspacing="0">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Trail</th>
                    <th>Description</th>
                    <?php if ($role === 'staff'): ?>
                        <th>Uploader</th>
                    <?php endif; ?>
                    <th>Preview</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <?php if (empty($photos)): ?>
                    <tr><td colspan="<?= $role === 'staff' ? 6 : 5 ?>">No photos found.</td></tr>
                <?php else: ?>
                    <?php foreach ($photos as $i => $r): ?>
                        <tr>
                            <td><?= $offset + $i + 1 ?></td>
                            <!-- protect against XSS -->
                            <td><?= htmlspecialchars($r['trail_name']) ?></td>
                            <td><?= htmlspecialchars($r['description'] ?? '') ?></td>

                            <?php if ($role === 'staff'): ?>
                                <!-- show uploader column if staff -->
                                <td><?= htmlspecialchars($r['uploader']) ?></td>

                            <?php endif; ?>
                            <td><img src="uploads/<?= htmlspecialchars($r['filename']) ?>"
                                     style="height:60px;object-fit:cover;" alt="photo"></td>
                            <td>
                                <form method="POST" action="upload.php"
                                      onsubmit="return confirm('Delete this photo?')">
                                    <input type="hidden" name="action"     value="delete">
                                    <input type="hidden" name="photo_id"   value="<?= (int)$r['id'] ?>">
                                    <!-- send token to DEL endpoint -->
                                    <input type="hidden" name="csrf_token" value="<?= csrf_token() ?>">
                                    <button type="submit" class="btn">Delete</button>
                                </form>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                <?php endif; ?>
            </tbody>
        </table>

        <div style="margin-top:10px;">
            <?php if ($page > 1): ?>
                <a href="photo-pagination.php?page=<?= $page - 1 ?>">Previous <?= $k ?></a>
            <?php else: ?>
                <span>Previous <?= $k ?></span>
            <?php endif; ?>

            <span style="margin:0 12px;">Page <?= $page ?> of <?= max(1, $pages) ?> (<?= $total ?> total)</span>

            <?php if ($page < $pages): ?>
                <a href="photo-pagination.php?page=<?= $page + 1 ?>">Next <?= $k ?></a>
            <?php else: ?>
                <span>Next <?= $k ?></span>
            <?php endif; ?>
        </div>
</div>
</body>
</html>