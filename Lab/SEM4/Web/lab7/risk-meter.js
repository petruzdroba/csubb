function calculateRisk() {
  let score = 0;

  const urgency = $('input[name="hazard_urgency"]:checked').val();

  if (urgency === "Tolerable") score += 1;
  if (urgency === "Minor") score += 2;
  if (urgency === "Moderate") score += 3;
  if (urgency === "Serious") score += 4;
  if (urgency === "Critical") score += 5;

  const causes = $('input[name="cause"]:checked').length;
  score += causes * 2;

  const equipment = $('input[name="equipment"]:checked').length;
  if (equipment < 2) score += 2;

  return score;
}

function updateRiskMeter() {
  const score = calculateRisk();

  const $fill = $("#risk-fill");
  const $label = $("#risk-label");

  const percent = Math.min(score * 10, 100);
  $fill.css("width", percent + "%");

  let text = "";
  let color = "";

  if (score <= 2) {
    text = "Safe Trail";
    color = "#2ecc71";
  } else if (score <= 4) {
    text = "Tolerable Conditions";
    color = "#a3d977";
  } else if (score <= 6) {
    text = "Caution";
    color = "#f1c40f";
  } else if (score <= 8) {
    text = "Risky Conditions";
    color = "#e67e22";
  } else if (score <= 11) {
    text = "Dangerous";
    color = "#e74c3c";
  } else {
    text = "GET OUT – Extreme Hazard";
    color = "#8e0000";
  }

  $fill.css("background", color);
  $label.text(text);
}

$(function () {
  const $form = $("form");

  $form.on("input change", updateRiskMeter);
});