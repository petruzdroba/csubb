<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

if (empty($_SESSION['captcha_code'])) {
    $a = random_int(1, 9);
    $b = random_int(1, 9);
    $_SESSION['captcha_code']     = (string)($a + $b);
    $_SESSION['captcha_question'] = "What is $a + $b?";
}
?>