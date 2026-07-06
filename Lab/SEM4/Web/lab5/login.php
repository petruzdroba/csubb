<?php
require_once 'includes/db.php';
require_once 'includes/session.php';
require_once 'includes/captcha.php';

try_remember_me($mysqli);

if (is_logged_in()) {
    header('Location: dashboard.php');
    exit;
}

$error    = '';
$username = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = trim($_POST['username'] ?? ''); // if null -> empty
    $password = $_POST['password'] ?? '';
    $captcha  = trim($_POST['captcha'] ?? '');
    $remember = !empty($_POST['remember']);

    $expected = $_SESSION['captcha_code'] ?? '';
    unset($_SESSION['captcha_code']);

    if ($captcha !== $expected) {
        $error = 'Incorrect answer. Please try again.';
    } elseif (empty($username) || empty($password)) {
        $error = 'Please enter both username and password.';

    } else {
        $stmt = $mysqli->prepare(
            'SELECT id, password, full_name, role FROM users WHERE username = ?'
        );
        $stmt->bind_param('s', $username);// bind to string
        $stmt->execute();
        $stmt->bind_result($id, $hash, $full_name, $role); //store result in vars
        $found = $stmt->fetch();
        $stmt->close();

        if ($found && $password === $hash) {
            session_regenerate_id(true);
            $_SESSION['user_id']   = $id;
            $_SESSION['username']  = $username;
            $_SESSION['full_name'] = $full_name;
            $_SESSION['role']      = $role;

            // save a remember cookie if user checks
            if ($remember) {
                $token = bin2hex(random_bytes(32));
                $upd   = $mysqli->prepare(
                    'UPDATE users SET remember_token = ? WHERE id = ?'
                );
                $upd->bind_param('si', $token, $id);
                $upd->execute();
                $upd->close();
                set_remember_cookie($token);
            }

            // log action to database
            audit_log($sqlite, $username, 'login');

            $next = $_GET['next'] ?? 'dashboard.php';
            header('Location: ' . $next);
            exit;
        } else {
            $error = 'Invalid username or password.';
        }
    }

    // regenerate, if failed
    require_once 'includes/captcha.php';
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – National Park Portal</title>
    <link rel="stylesheet" href="style1.css">
</head>
<body>
<div>
    <div class="card">
        <h1>Park Portal</h1>
        <p>Sign in to access your account</p>

        <?php if ($error): ?>
            <div><?= htmlspecialchars($error) ?></div>
        <?php endif; ?>

        <form method="POST" action="login.php<?= isset($_GET['next']) ? '?next='.urlencode($_GET['next']) : '' ?>">

            <div>
                <label for="username">Username</label>
                <input
                    type="text"
                    id="username"
                    name="username"
                    value="<?= htmlspecialchars($username) ?>"
                    autocomplete="username"
                    required
                >
            </div>

            <div>
                <label for="password">Password</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    autocomplete="current-password"
                    required
                >
            </div>

            <div>
                <label for="captcha"><?= htmlspecialchars($_SESSION['captcha_question'] ?? '') ?></label>
                <input
                    type="text"
                    name="captcha"
                    id="captcha"
                    maxlength="3"
                    placeholder="Answer"
                    autocomplete="off"
                    required
                >
            </div>

            <div>
                <input type="checkbox" id="remember" name="remember" value="1">
                <label for="remember">Remember me for <?= REMEMBER_DAYS ?> days</label>
            </div>

            <button type="submit" class="btn">Sign In</button>
            <br>
            <br>
        </form>

        <div>
            No account? <a href="register.php">Register here</a>
        </div>
        <br>
        <div>
            <a href="index.html">Back to park home</a>
        </div>
    </div>
</div>
</body>
</html>