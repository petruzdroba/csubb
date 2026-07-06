<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

try_remember_me($pdo);
require_login();

$userId = (int)$_SESSION['user_id'];
$role   = $_SESSION['role'];

if ($role === 'staff') {
    $stmt = $pdo->query('SELECT id, trail_name FROM trail_photos ORDER BY id DESC');
} else {
    $stmt = $pdo->prepare('SELECT id, trail_name FROM trail_photos WHERE uploader_id = :uid ORDER BY id DESC');
    $stmt->execute([':uid' => $userId]);
}
$photos = $stmt->fetchAll(PDO::FETCH_ASSOC);
// dynamixally exec the query based on role
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Photo – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
<div>
    <div><a href="dashboard.php">← Back to Dashboard</a></div>
    <div class="card card-centered">
        <h2>Edit Trail Photo (jQuery)</h2>

        <div>
            <label for="photo-select">Select Photo</label>
            <select id="photo-select">
                <option value=""> Choose a photo </option>
                <?php foreach ($photos as $p): ?>
                    <!-- for each photo display id + trail name -->
                    <option value="<?= (int)$p['id'] ?>">
                        #<?= (int)$p['id'] ?> - <?= htmlspecialchars($p['trail_name']) ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </div>

        <div id="edit-form" style="display:none; margin-top:16px;">
            <div>
                <label for="trail-name">Trail Name</label>
                <input type="text" id="trail-name">
            </div>
            <div>
                <label for="description">Description</label>
                <textarea id="description"></textarea>
            </div>
            <div style="margin-top:10px;">
                <button id="btn-save" class="btn" disabled>Save</button>
                <span id="save-msg" style="margin-left:10px;"></span>
            </div>
        </div>
    </div>
</div>

<script src="edit-photo-jquery.js"></script>
</body>
</html>