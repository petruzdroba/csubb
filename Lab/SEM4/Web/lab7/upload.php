<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

try_remember_me($pdo);
require_login();

$userId   = (int)$_SESSION['user_id'];
$role     = $_SESSION['role'];
$username = $_SESSION['username'];

define('UPLOAD_DIR',     __DIR__ . '/uploads/');
define('MAX_FILE_SIZE',  5 * 1024 * 1024); // 5 mb
define('ALLOWED_TYPES',  ['image/jpeg', 'image/png', 'image/webp', 'image/gif']);


if (isset($_GET['id'])) {
    $stmt = $pdo->prepare('SELECT filename FROM trail_photos WHERE id = ? AND uploader_id = ?');
    $stmt->execute([$_GET['id'], $userId]);
    $row = $stmt->fetch();
    if ($row) {
    $filepath = realpath(UPLOAD_DIR . $row['filename']);
    if ($filepath && str_starts_with($filepath, realpath(UPLOAD_DIR))) {
        $mime = mime_content_type($filepath);
        header('Content-Type: ' . $mime);
        readfile($filepath);
    }
} else {
        http_response_code(404);
        echo 'Not found.';
    }
    exit;
}

$error   = '';
$success = '';

// remove photo pdo 
if ($_SERVER['REQUEST_METHOD'] === 'POST' && ($_POST['action'] ?? '') === 'delete') {
    csrf_check();

    $photoId = (int)($_POST['photo_id'] ?? 0);

    $sel = $pdo->prepare('SELECT uploader_id, filename FROM trail_photos WHERE id = :id');
    $sel->execute([':id' => $photoId]);
    $photo = $sel->fetch();

    if (!$photo) {
        $error = 'Photo not found.';
    } elseif ($photo['uploader_id'] !== $userId && $role !== 'staff') {// not staff or user
        $error = 'You do not have permission to delete this photo.';
    } else {
        $filepath = UPLOAD_DIR . $photo['filename'];
        if (file_exists($filepath)) {
            unlink($filepath);
        }
        $del = $pdo->prepare('DELETE FROM trail_photos WHERE id = :id');
        $del->execute([':id' => $photoId]);

        audit_log($sqlite, $username, 'delete_photo:' . $photo['filename']);
        header('Location: dashboard.php?deleted=1');
        exit;
    }
}

// upload pdo
if ($_SERVER['REQUEST_METHOD'] === 'POST' && ($_POST['action'] ?? '') === 'upload') {
    $trailName   = trim($_POST['trail_name']  ?? '');
    $description = trim($_POST['description'] ?? '');

    // iother trail
    if ($trailName === '__other__') {
        $trailName = trim($_POST['trail_name_other'] ?? '');
    }

    if (empty($trailName)) {
        $error = 'Please select or enter a trail name.';
    } elseif (empty($_FILES['photo']) || $_FILES['photo']['error'] === UPLOAD_ERR_NO_FILE) {
        $error = 'Please choose a file to upload.';
    } elseif ($_FILES['photo']['error'] !== UPLOAD_ERR_OK) {
        $error = 'Upload error (code ' . $_FILES['photo']['error'] . ').';
    } elseif ($_FILES['photo']['size'] > MAX_FILE_SIZE) {
        $error = 'File is too large. Maximum size is 5 MB.';
    } else {
        $finfo    = new finfo(FILEINFO_MIME_TYPE);
        $mimeType = $finfo->file($_FILES['photo']['tmp_name']);

        // safe
        if (!in_array($mimeType, ALLOWED_TYPES, true)) {
            $error = 'Only JPEG, PNG, WebP and GIF images are allowed.';
        } else {
            $ext = match($mimeType) {
                'image/jpeg' => 'jpg',
                'image/png'  => 'png',
                'image/webp' => 'webp',
                'image/gif'  => 'gif',
            };
            $filename = uniqid('trail_', true) . '.' . $ext;
            $dest     = UPLOAD_DIR . $filename;

            if (!is_dir(UPLOAD_DIR)) {
                mkdir(UPLOAD_DIR, 0755, true);
            }

            if (move_uploaded_file($_FILES['photo']['tmp_name'], $dest)) {
                $ins = $pdo->prepare(
                    'INSERT INTO trail_photos
                        (uploader_id, filename, original_name, trail_name, description)
                     VALUES (:uid, :filename, :orig, :trail, :desc)'
                );
                $ins->execute([
                    ':uid'      => $userId,
                    ':filename' => $filename,
                    ':orig'     => $_FILES['photo']['name'],
                    ':trail'    => $trailName,
                    ':desc'     => $description,
                ]);

                audit_log($sqlite, $username, 'upload_photo:' . $filename);
                header('Location: dashboard.php?uploaded=1');
                exit;
            } else {
                $error = 'Could not save the file. Check server permissions.';
            }
        }// end if mime

    }
}

// load park for dropdown 
$parksJson = file_get_contents(__DIR__ . '/parks.json');
$parksByZone = json_decode($parksJson, true) ?? [];
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Upload Trail Photo – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
<div>
    <div><a href="dashboard.php">← Back to Dashboard</a></div>
    <div class="card">
        <h2>Upload a Trail Photo</h2>

        <?php if ($error): ?>
            <div><?= htmlspecialchars($error) ?></div>
        <?php endif; ?>

        <form method="POST" action="upload.php" enctype="multipart/form-data">
            <input type="hidden" name="action" value="upload">

            <div>
                <label for="trail_name">Park / Trail</label>
                <select id="trail_name" name="trail_name" required>
                    <option value="">– Choose a park –</option>
                    <?php foreach ($parksByZone as $zone => $parks): ?>
                        <optgroup label="<?= htmlspecialchars($zone) ?>">
                            <?php foreach ($parks as $park): ?>
                                <option value="<?= htmlspecialchars($park) ?>">
                                    <?= htmlspecialchars($park) ?>
                                </option>
                            <?php endforeach; ?>
                        </optgroup>
                    <?php endforeach; ?>
                    <option value="__other__">Other (type below)</option>
                </select>
            </div>

            <div>
                <label for="trail_name_other">Other Park Name <small>(only if "Other" selected above)</small></label>
                <input type="text" id="trail_name_other" name="trail_name_other"
                       placeholder="Enter park or trail name">
            </div>

            <div>
                <label for="description">Description <small>(optional)</small></label>
                <textarea id="description" name="description"
                          placeholder="Describe this photo or the trail conditions..."></textarea>
            </div>

            <div>
                <label for="photo">Photo File</label>
                <input type="file" id="photo" name="photo"
                       accept="image/jpeg,image/png,image/webp,image/gif" required>
                <small>JPEG, PNG, WebP or GIF · max 5 MB</small>
            </div>

            <button type="submit" class="btn">Upload Photo</button>
        </form>
    </div>
</div>
</body>
</html>