<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

try_remember_me($pdo);
require_login();

$userId   = (int)$_SESSION['user_id'];
$role     = $_SESSION['role'];
$fullName = $_SESSION['full_name'];
$username = $_SESSION['username'];

// Pre-filled profile data
$stmt = $pdo->prepare('SELECT full_name, email FROM users WHERE id = :id');
$stmt->execute([':id' => $userId]);
$user = $stmt->fetch();

// Profile update
$updateMsg = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['action']) && $_POST['action'] === 'update_profile') {
    $newName  = trim($_POST['full_name'] ?? '');
    $newEmail = trim($_POST['email']     ?? '');

    if (empty($newName) || !filter_var($newEmail, FILTER_VALIDATE_EMAIL)) {
        $updateMsg = '<span>Invalid name or email.</span>';
    } else {
        $upd = $pdo->prepare('UPDATE users SET full_name = :name, email = :email WHERE id = :id');
        $upd->execute([':name' => $newName, ':email' => $newEmail, ':id' => $userId]);
        $_SESSION['full_name'] = $newName;
        $fullName              = $newName;
        $user['full_name']     = $newName;
        $user['email']         = $newEmail;
        $updateMsg = '<span>Profile updated successfully.</span>';
        audit_log($sqlite, $username, 'profile_update');
    }
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
                <span><?= htmlspecialchars($role) ?></span>
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

    
        <h2><?= $role === 'staff' ? 'All Trail Photos' : 'My Trail Photos' ?></h2>

        <table id="photos-table" border="1" cellpadding="8" cellspacing="0">
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
            <tbody id="photos-tbody">
                <tr><td colspan="<?= $role === 'staff' ? 6 : 5 ?>">Loading...</td></tr> 
                <!-- update colspan based on role, user dosent see uploader name -->
            </tbody>
        </table>

        <div id="photos-nav" style="margin-top:10px;">
            <button id="btn-prev" disabled>Previous 3</button>
            <span id="photos-info" style="margin:0 12px;"></span>
            <button id="btn-next" disabled>Next 3</button>
        </div>

</div>

<script>
    const currentUserId = <?= $userId ?>;
    const currentRole   = <?= json_encode($role) ?>;
</script>
<script src="photo-pagination.js"></script>
<!-- <script src="photo-pagination-xml.js"></script> -->
</body>
</html>