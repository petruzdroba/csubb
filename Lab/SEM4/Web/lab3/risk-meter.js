function calculateRisk() {
  let score = 0;

  const urgency = document.querySelector('input[name="hazard_urgency"]:checked')?.value;

  if (urgency === "Tolerable") score += 1;
  if (urgency === "Minor") score += 2;
  if (urgency === "Moderate") score += 3;
  if (urgency === "Serious") score += 4;
  if (urgency === "Critical") score += 5;

  const causes = document.querySelectorAll('input[name="cause"]:checked').length;
  score += causes * 2; // luam toate cauzele si adaugam 2 puncte per checked

  const equipment = document.querySelectorAll('input[name="equipment"]:checked').length;
  if (equipment < 2) score += 2;

  return score;
}

function updateRiskMeter() {
  const score = calculateRisk();

  const fill = document.getElementById("risk-fill"); 
  const label = document.getElementById("risk-label");

  let percent = Math.min(score * 10, 100);
  fill.style.width = percent + "%";

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

  fill.style.background = color; // adaugam claselor din css contentul si culoarea, ele sunt empty
  label.textContent = text;
}

document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form");
  form.addEventListener("input", updateRiskMeter);
  form.addEventListener("change", updateRiskMeter); // per schimbare in form reupdatam datele
});