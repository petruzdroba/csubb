<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

try_remember_me($mysqli);
require_login();

$userId   = (int)$_SESSION['user_id'];
$role     = $_SESSION['role'];
$fullName = $_SESSION['full_name'];
$username = $_SESSION['username'];

// get pre filled data 
$stmt = $pdo->prepare('SELECT full_name, email FROM users WHERE id = :id');
$stmt->execute([':id' => $userId]);
$user = $stmt->fetch(); // pre fills the edit form below

// update
$updateMsg = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'update_profile') {
    $newName  = trim($_POST['full_name'] ?? '');
    $newEmail = trim($_POST['email']     ?? '');

    if (empty($newName) || !filter_var($newEmail, FILTER_VALIDATE_EMAIL)) {
        $updateMsg = '<span>Invalid name or email.</span>';
    } else {
        $upd = $pdo->prepare(
            'UPDATE users SET full_name = :name, email = :email WHERE id = :id'
        );
        $upd->execute([':name' => $newName, ':email' => $newEmail, ':id' => $userId]);
        $_SESSION['full_name'] = $newName;
        $fullName  = $newName;
        $user['full_name'] = $newName;
        $user['email']     = $newEmail;
        $updateMsg = '<span>Profile updated successfully.</span>';
        audit_log($sqlite, $username, 'profile_update');
    }
}

// phoyos by user
$photoStmt = $pdo->prepare(
    'SELECT id, filename, original_name, trail_name, description, uploaded_at
     FROM trail_photos WHERE uploader_id = :uid ORDER BY uploaded_at DESC'
);
$photoStmt->execute([':uid' => $userId]);
$photos = $photoStmt->fetchAll();

// id staff: all photos
$allPhotos = [];
if ($role === 'staff') {
    $allStmt = $pdo->query(
        'SELECT tp.id, tp.filename, tp.original_name, tp.trail_name,
                tp.description, tp.uploaded_at, u.full_name, u.username
         FROM trail_photos tp
         JOIN users u ON u.id = tp.uploader_id
         ORDER BY tp.uploaded_at DESC'
    );
    $allPhotos = $allStmt->fetchAll();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
<div>

    <div>
        <div>
            <h1>Welcome, <?= htmlspecialchars($fullName) ?>
                <span><?= $role ?></span>
            </h1>
        </div>
        <div>
            <a href="index.html">Home</a>
            <?php if ($role === 'staff'): ?>
                <a href="hire5.php">Hiring</a>
            <?php endif; ?>
            <a href="upload.php">Upload Photo</a>
            <a href="logout.php">Log Out</a>
        </div>
    </div>

    <div class="card">
        <h2>Edit Profile</h2>
        <?php if ($updateMsg): ?>
            <p><?= $updateMsg ?></p>
        <?php endif; ?>
        <form method="POST" action="dashboard.php">
            <input type="hidden" name="action" value="update_profile">
            <div>
                <div>
                    <label for="full_name">Full Name</label>

                    <!-- take prefilled data -->
                    <input type="text" id="full_name" name="full_name"
                           value="<?= htmlspecialchars($user['full_name']) ?>" required>
                </div>
                <div>
                    <label for="email">Email</label>

                    <input type="email" id="email" name="email"
                           value="<?= htmlspecialchars($user['email']) ?>" required>
                </div>
            </div>
            <button type="submit" class="btn">Save Changes</button>
        </form>
    </div>

    <div class="card">
        <h2>My Trail Photos</h2>
        <?php if (empty($photos)): ?>
            <p>You haven't uploaded any photos yet. <a href="upload.php">Upload one now →</a></p>
        <?php else: ?>
            <div>
                <?php foreach ($photos as $p): ?>
                    <div>
                        <img src="uploads/<?= htmlspecialchars($p['filename']) ?>"
                             alt="<?= htmlspecialchars($p['original_name']) ?>">
                        <div>
                            <div><?= htmlspecialchars($p['trail_name']) ?></div>
                            <div><?= htmlspecialchars($p['description'] ?? '') ?></div>
                            <div><?= htmlspecialchars($p['uploaded_at']) ?></div>
                        </div>
                        <form method="POST" action="upload.php">
                            <input type="hidden" name="action"   value="delete">
                            <input type="hidden" name="photo_id" value="<?= (int)$p['id'] ?>">
                            <button type="submit" class="btn"
                                    onclick="return confirm('Delete this photo?')">
                                Delete
                            </button>
                        </form>
                    </div>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>
    </div>

    <?php if ($role === 'staff' && !empty($allPhotos)): ?>
    <div class="card">
        <h2>Staff View – All Uploads</h2>
        <div>
            <?php foreach ($allPhotos as $p): ?>
                <div>
                    <img src="uploads/<?= htmlspecialchars($p['filename']) ?>"
                         alt="<?= htmlspecialchars($p['original_name']) ?>">
                    <div>
                        <div><?= htmlspecialchars($p['trail_name']) ?></div>
                        <div>by <?= htmlspecialchars($p['full_name']) ?></div>
                        <div><?= htmlspecialchars($p['description'] ?? '') ?></div>
                        <div><?= htmlspecialchars($p['uploaded_at']) ?></div>
                    </div>
                    <form method="POST" action="upload.php">
                        <input type="hidden" name="action"   value="delete">
                        <input type="hidden" name="photo_id" value="<?= (int)$p['id'] ?>">
                        <button type="submit" class="btn"
                                onclick="return confirm('Delete this photo?')">
                            Delete
                        </button>
                    </form>
                </div>
            <?php endforeach; ?>
        </div>
    </div>
    <?php endif; ?>

</div>
</body>
</html>
