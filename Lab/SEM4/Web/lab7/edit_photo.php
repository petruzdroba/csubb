<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

try_remember_me($pdo);
require_login();

$userId = (int)$_SESSION['user_id'];
$role   = $_SESSION['role'];

// load all photos for the select dropdown
if ($role === 'staff') {
    $stmt = $pdo->query('SELECT id, trail_name FROM trail_photos ORDER BY id DESC');
} else {
    $stmt = $pdo->prepare('SELECT id, trail_name FROM trail_photos WHERE uploader_id = :uid ORDER BY id DESC');
    $stmt->execute([':uid' => $userId]);
}
$photos = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Photo – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
<div>
    <div><a href="dashboard.php">← Back to Dashboard</a></div>
    <div class="card card-centered">
        <h2>Edit Trail Photo</h2>

        <div>
            <label for="photo-select">Select Photo</label>
            <select id="photo-select">
                <option value=""> Choose a photo </option>
                <?php foreach ($photos as $p): ?>
                    <!-- load id + trail name on dropdown -->
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

<script>
(function () {
    const selectEl     = document.getElementById('photo-select');
    const editForm     = document.getElementById('edit-form');
    const trailNameEl  = document.getElementById('trail-name');
    const descEl       = document.getElementById('description');
    const btnSave      = document.getElementById('btn-save');
    const saveMsg      = document.getElementById('save-msg');

    let originalData = {};
    let isDirty      = false;

    function fetchPhoto(id) {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', 'api/photo_get.php?id=' + id, true);
        xhr.onload = function () {
            if (xhr.status !== 200) return;
            const data      = JSON.parse(xhr.responseText);
            // save original data for dirty check
            originalData    = { trail_name: data.trail_name, description: data.description };
            trailNameEl.value = data.trail_name;
            descEl.value      = data.description;
            editForm.style.display = 'block';
            isDirty           = false;
            btnSave.disabled  = true;
            // clear message on load
            saveMsg.textContent = '';
        };
        xhr.send();
    }

    function savePhoto(id, callback) {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'api/photo_update.php', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onload = function () {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                if (data.success) {
                    originalData  = { trail_name: trailNameEl.value, description: descEl.value };
                    isDirty       = false;
                    btnSave.disabled = true;
                    saveMsg.textContent = 'Saved successfully.';

                    // update the select label too
                    const opt = selectEl.querySelector('option[value="' + id + '"]');
                    if (opt) opt.textContent = '#' + id + ' - ' + trailNameEl.value;

                    if (typeof callback === 'function') callback();
                }
            } else {
                saveMsg.textContent = 'Error saving.';
            }
        };
        xhr.send(JSON.stringify({
            id:          parseInt(id),
            trail_name:  trailNameEl.value,
            description: descEl.value
        }));
    }

    // check for changes
    function hasChanges() {
        return trailNameEl.value !== originalData.trail_name ||
               descEl.value      !== originalData.description;
    }

    selectEl.addEventListener('change', function () {
        const newId  = this.value;
        const oldId  = selectEl.dataset.current;

        if (!newId) {
            editForm.style.display = 'none';
            return;
        }

        if (isDirty && oldId) {
            const save = confirm('You have unsaved changes. Save before continuing?');
            if (save) {
                savePhoto(oldId, function () {
                    selectEl.dataset.current = newId;
                    fetchPhoto(newId);
                });
                return;
            }
        }

        selectEl.dataset.current = newId;
        fetchPhoto(newId);
    });

    // mark dirty on any input change
    trailNameEl.addEventListener('input', function () {
        isDirty          = hasChanges(); // see diff and save result
        btnSave.disabled = !isDirty;
        saveMsg.textContent = '';
    });

    descEl.addEventListener('input', function () {
        isDirty          = hasChanges();
        btnSave.disabled = !isDirty;
        saveMsg.textContent = '';
    });

    btnSave.addEventListener('click', function () {
        savePhoto(selectEl.value, null);
    });
}());
</script>
</body>
</html>