<?php
require_once 'includes/db.php';
require_once 'includes/session.php';

try_remember_me($pdo);

require_staff(); // auto redirects if not staff
audit_log($sqlite, $_SESSION['username'], 'view_hire_page');
?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="sprite/sprites.css">
    <link rel="stylesheet" href="style1.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <title>Get Hired! - National Park Service</title>
  </head>
  <body>
      <header>
      <h1>NATIONAL PARK SERVICE</h1>
      <img height="70" src="img/logo.png" alt="National Park Service logo">
            <h2>Safer trails and hikes</h2>
    </header>

    <nav>
  <ul>
    <li><a href="index.html"><span class="sprite-icon icon-home"></span> Welcome</a></li>
        <li><a href="welcome5.html"><span class="sprite-icon icon-tree"></span> Parks</a></li>
        <li><a href="help5.html"><span class="sprite-icon icon-warning"></span> Help</a></li>
        <li><a href="hire5.php"><span class="sprite-icon icon-people"></span> Hire</a></li>
        <li><a href="feedback.html"><span class="sprite-icon icon-star"></span> Feedback</a></li>
        <li><a href="dashboard.php"><span class="sprite-icon icon-user"></span> Profile</a></li>
    <li>
      <?php if (is_logged_in()): ?>
        <a href="dashboard.php"> <?= htmlspecialchars($_SESSION['full_name']) ?></a>
        &nbsp;|&nbsp;
        <a href="logout.php">Log Out</a>
      <?php else: ?>
        <a href="login.php">Log In</a>
      <?php endif; ?>
    </li>
  </ul>
</nav>

    <main>
      <div>
        <h3>We need all the help we can get!</h3>
        <p>Take a quick employer survey and we will reach out to you in 3-5 days.</p>

        <form method="post">

          <fieldset>
            <legend>Personal information</legend>

            <label for="first-name">First Name:</label>
            <input type="text" id="first-name" name="first-name" maxlength="20" placeholder="John" required>

            <label for="last-name">Last Name:</label>
            <input type="text" id="last-name" name="last-name" maxlength="20" placeholder="Doe" required>

            <br>

            <label for="phone-prefix">Phone prefix:</label>
            <select name="phone-prefix" id="phone-prefix">
              <option value="+40">România (+40)</option>
              <option value="+33">France (+33)</option>
              <option value="+1">USA / Canada (+1)</option>
              <option value="+44">UK (+44)</option>
              <option value="+86">China (+86)</option>
              <option value="">Other</option>
            </select>

            <label for="phone">Phone Number:</label>
            <input type="tel" id="phone" name="phone" size="15" placeholder="0744 444 444">

            <br>

            <label for="age">Age:</label>
            <input type="number" id="age" name="age" min="18" max="80" placeholder="18">

            <br>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" size="25" placeholder="john.doe@example.com">
          </fieldset>

          <fieldset>
            <legend>Available Locations</legend>
            <label for="zone">Mountain Zone</label>
            <select id="zone">
              <option value="">Select zone</option>
            </select>

            <label for="park">Park</label>
            <select id="park">
              <option value="">Select park</option>
            </select>
          </fieldset>

          <fieldset>
            <legend>Your Skills</legend>

            <p><strong>Mountain</strong></p>
            <ul>
              <li><label><input type="checkbox" name="skill" value="alpinism"> Alpinism</label></li>
              <li><label><input type="checkbox" name="skill" value="sport_climbing"> Sport Climbing Lead 6a+ or above</label></li>
              <li><label><input type="checkbox" name="skill" value="top_roping"> Top Roping 6b+ or above</label></li>
              <li><label><input type="checkbox" name="skill" value="multipitch"> Multipitch Lead 6a+ / Second 6b+</label></li>
              <li><label><input type="checkbox" name="skill" value="belay"> Belaying - Self-Assisted (GriGri, Neox) and Non-Assisted (Reverso, ATC)</label></li>
              <li><label><input type="checkbox" name="skill" value="traditional"> Traditional (one pitch - on gear) 6a+</label></li>
            </ul>

            <p><strong>First Aid</strong></p>
            <ul>
              <li><label><input type="checkbox" name="skill" value="basic_first_aid"> Basic wound treating</label></li>
              <li><label><input type="checkbox" name="skill" value="rope_rescue"> Above-abseil rope rescue</label></li>
              <li><label><input type="checkbox" name="skill" value="helicopter_rescue"> Helicopter abseil rescue</label></li>
            </ul>

            <p><strong>Vehicles</strong></p>
            <ul>
              <li><label><input type="checkbox" name="skill" value="car"> Car</label></li>
              <li><label><input type="checkbox" name="skill" value="atv"> ATV</label></li>
              <li><label><input type="checkbox" name="skill" value="horse"> Horse-back riding</label></li>
              <li><label><input type="checkbox" name="skill" value="kayak"> Kayak</label></li>
              <li><label><input type="checkbox" name="skill" value="helicopter_pilot" disabled> Helicopter (not currently available)</label></li>
            </ul>

            <p><strong>Standalone Skills</strong></p>
            <ul>
              <li><label><input type="checkbox" name="skill" value="cave_diving"> Cave Diving</label></li>
              <li><label><input type="checkbox" name="skill" value="snorkeling"> Snorkeling</label></li>
              <li><label><input type="checkbox" name="skill" value="firewatch"> Fire Spotting and Preventing</label></li>
              <li><label><input type="checkbox" name="skill" value="via_ferrata"> Via Ferrata Maintaining</label></li>
            </ul>
          </fieldset>

          <fieldset>
            <legend>Positions</legend>
            <p>Select the available positions you want to apply for.</p>

            <ol type="A" id="positions">
              <li>
                <strong>Normal Schedule</strong>
                <ul>
                  <li><label><input type="checkbox" name="position" value="ranger"> Ranger</label></li>
                  <li><label><input type="checkbox" name="position" value="guide"> Guide</label></li>
                  <li><label><input type="checkbox" name="position" value="backcountry"> Backcountry Ranger</label></li>
                  <li><label><input type="checkbox" name="position" value="wildlife"> Wildlife Ranger</label></li>
                </ul>
              </li>

              <li>
                <strong>2 ON / 1 OFF Schedule (for 21 or older)</strong>
                <ul>
                  <li><label><input type="checkbox" name="position" value="rescue"> Rescue Ranger</label></li>
                  <li><label><input type="checkbox" name="position" value="fire_ranger"> Fire Watch Ranger</label></li>
                </ul>
              </li>

              <li>
                <strong>On Call Schedule</strong>
                <ul>
                  <li><label><input type="checkbox" name="position" value="helicopter_pilot_pos" disabled> Helicopter Pilot (not currently available)</label></li>
                  <li><label><input type="checkbox" name="position" value="manager" disabled> Manager (not currently available)</label></li>
                  <li><label><input type="checkbox" name="position" value="brigade"> Fire Brigade</label></li>
                </ul>
              </li>

              <li>
                <strong>Half a Year</strong>
                <ul>
                  <li><label><input type="checkbox" name="position" value="fire_watcher"> Fire Watcher</label></li>
                </ul>
              </li>
            </ol>
          </fieldset>

          <input type="submit" value="Submit Application">

        </form>
    </main>

    <footer>
      <p>National Park Service - Safer trails and hikes</p>
      <p>
        For emergencies call 
        <a href="https://www.salvamontromania.ro/" target="_blank" rel="noopener noreferrer">Salvamont România</a>
      </p>
    </footer>

    <script src="form_validation.js"></script>
    <script src="hire5.js"></script>
    <script src="collapsible-list.js"></script>
  </body>
</html>