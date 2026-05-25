<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

if (is_logged_in()) {
    header('Location: dashboard.php');
    exit;
}

$error   = '';
$success = '';
$fields  = ['username' => '', 'full_name' => '', 'email' => ''];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $fields['username']  = trim($_POST['username']  ?? '');
    $fields['full_name'] = trim($_POST['full_name'] ?? '');
    $fields['email']     = trim($_POST['email']     ?? '');
    $password            = $_POST['password']       ?? '';
    $confirm             = $_POST['confirm']        ?? '';

    if (empty($fields['username']) || empty($fields['full_name']) ||
        empty($fields['email'])    || empty($password)) {
        $error = 'All fields are required.';
    } elseif (!filter_var($fields['email'], FILTER_VALIDATE_EMAIL)) {
        $error = 'Invalid email address.';
    } elseif (strlen($password) < 8) {
        $error = 'Password must be at least 8 characters.';
    } elseif ($password !== $confirm) {
        $error = 'Passwords do not match.';
    } else {
        // check if username or email exists
        $check = $mysqli->prepare(
            'SELECT id FROM users WHERE username = ? OR email = ?'
        );
        $check->bind_param('ss', $fields['username'], $fields['email']);
        $check->execute();
        $check->store_result();

        if ($check->num_rows > 0) { // if value is ret, exists
            $error = 'Username or email is already taken.';
        } else {
            $ins = $mysqli->prepare(
                'INSERT INTO users (username, password, full_name, email, role)
                 VALUES (?, ?, ?, ?, "guest")'
            );
            $ins->bind_param('ssss',
                $fields['username'], $password, $fields['full_name'], $fields['email']
            );
            if ($ins->execute()) {
                audit_log($sqlite, $fields['username'], 'register');
                $success = 'Account created! You can now <a href="login.php">log in</a>.';
                $fields  = ['username' => '', 'full_name' => '', 'email' => ''];
            } else {
                $error = 'Registration failed. Please try again.';
            }
            $ins->close();
        }
        $check->close();
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
<div>
    <div class="card card-centered">
        <h1>Park Portal</h1>
        <p>Create a guest account</p>

        <?php if ($error): ?>
            <div><?= htmlspecialchars($error) ?></div>
        <?php endif; ?>
        <?php if ($success): ?>
            <div><?= $success ?></div>
        <?php endif; ?>

        <?php if (!$success): ?>
        <form method="POST" action="register.php">
            <div>
                <label for="full_name">Full Name</label>
                <input type="text" id="full_name" name="full_name"
                       value="<?= htmlspecialchars($fields['full_name']) ?>" required>
            </div>
            <div>
                <label for="username">Username</label>
                <input type="text" id="username" name="username"
                       value="<?= htmlspecialchars($fields['username']) ?>" required>
            </div>
            <div>
                <label for="email">Email</label>
                <input type="email" id="email" name="email"
                       value="<?= htmlspecialchars($fields['email']) ?>" required>
            </div>
            <div>
                <label for="password">Password <small>(min. 8 chars)</small></label>
                <input type="password" id="password" name="password" required>
            </div>
            <div>
                <label for="confirm">Confirm Password</label>
                <input type="password" id="confirm" name="confirm" required>
            </div>
            <button type="submit" class="btn">Create Account</button>
            <br>
            <br>
        </form>
        <?php endif; ?>

        <div>
            Already have an account? <a href="login.php">Sign in</a>
        </div>
    </div>
</div>
</body>
</html>