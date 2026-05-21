<?php
require_once 'includes/db.php';

$results = [];
$search  = '';

if (isset($_GET['q']) && $_GET['q'] !== '') {
    $search = $_GET['q'];

    $stmt = $pdo->prepare("SELECT trail_name, description, filename FROM trail_photos WHERE trail_name = ?");
    $stmt->execute([$search]);
    $results = $stmt->fetchAll();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
    <div><a href="dashboard.php">← Back to Dashboard</a></div>
<div class="card">
    <h1>Search Trail Photos</h1>
    <form method="GET" action="search.php">
        <input type="text" name="q" value="<?= htmlspecialchars($search) ?>" placeholder="Trail name...">
        <button type="submit" class="btn">Search</button>
    </form>

    <?php if (!empty($results)): ?>
    <table border="1" cellpadding="8">
        <thead><tr><th>Trail</th><th>Description</th><th>Preview</th></tr></thead>
        <tbody>
        <?php foreach ($results as $r): ?>
            <tr>
                <td><?= htmlspecialchars($r['trail_name']) ?></td>
                <td><?= htmlspecialchars($r['description']) ?></td>
                <td><img src="uploads/<?= htmlspecialchars($r['filename']) ?>" style="height:60px;"></td>
            </tr>
        <?php endforeach; ?>
        </tbody>
    </table>
    <?php elseif ($search !== ''): ?>
        <p>No results found.</p>
    <?php endif; ?>
</div>
</body>
</html>