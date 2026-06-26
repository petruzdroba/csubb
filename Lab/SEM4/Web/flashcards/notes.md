Before PHP 7 we avoided SQL injections by using `mysql_real_escape_string()`

Why does centering a *block* element work with `margin: 0 auto` ? 0 defines top and bottom and auto splits available width space equally between left and right
Why dosent this work on *inline element* ? Inline elements dont have a defined width, so the browser ignores the `auto` value

If `return` only finishes executing a function or an inlude statement, how does PHP return values to the client? PHP uses `echo` or `print` to output text/HTML straight to the clients browser, 
a `header()` must be sent before tho
```php
<?php
header('Content-Type: text/html; charset=UTF-8'); // this call comes first before any other sending, otherwise an error will occur

$name = "Alice";
$items = ['Apple', 'Banana', 'Orange'];
?>

<!DOCTYPE html>
<html>
<head>
  <title>My Page</title>
</head>
<body>
  <h1>Welcome, <?php echo $name; ?>!</h1>
  
  <ul>
    <?php foreach ($items as $item): ?>
      <li><?php echo $item; ?></li>
    <?php endforeach; ?>
  </ul>
  
  <p>Current time: <?php echo date('Y-m-d H:i:s'); ?></p>
</body>
</html>
```

For the 2026 exam some questions were related to the following:
- SEO
- CMS (like WordPress i guess)
- SuperGlobals (like _POST, _GET, _REQUEST, _COOKIE)
- colors ( exp. : the color #888888 is what in rgb, can you write it using special HTML chars, what color is it ?)
- * questions in the `flashcards_web.json` file appeared on the exam
- knowing which style selector overrides the other and applies some style (like `background-color: blue` inline vs <style> tag)
- syntax of creating an `Array` object in JS ( like `let x = new Array(1,2) let y = new Array(3)` and what do they each produce)
- about the `prototype` keyword in JS 
