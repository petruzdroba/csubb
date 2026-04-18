let trailData = [];
let sortState = { key: null, asc: true };

function renderVerticalTable(data) {
  const table = document.getElementById("table-trail");

  const keys = ["sign", "meaning"];

  const headerRow = `
    <tr>
      <th></th>
      ${data.map((_, i) => `<th>Item ${i + 1}</th>`).join("")}
    </tr>
  `;

  const bodyRows = keys
    .map((key) => {
      return `
        <tr>
          <th data-key="${key}">${key}</th>
          ${data.map(item => `<td>${item[key]}</td>`).join("")}
        </tr>
      `;
    })
    .join("");

  table.innerHTML = headerRow + bodyRows;

  updateHeaderState();
}

function sortBy(key) {
  if (sortState.key === key) {
    sortState.asc = !sortState.asc;
  } else {
    sortState.key = key;
    sortState.asc = true;
  }

  trailData.sort((a, b) => {
    const x = (a[key] || "").toLowerCase();
    const y = (b[key] || "").toLowerCase();

    return sortState.asc
      ? x.localeCompare(y)
      : y.localeCompare(x);
  });

  renderVerticalTable(trailData);
}

function updateHeaderState() {
  const headers = document.querySelectorAll("#table-trail th[data-key]");

  headers.forEach((h) => {
    h.classList.remove("sorted-asc", "sorted-desc");

    if (h.dataset.key === sortState.key) {
      h.classList.add(sortState.asc ? "sorted-asc" : "sorted-desc");
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  fetch("trail-data.json")
    .then(r => r.json())
    .then(data => {
      trailData = data;
      renderVerticalTable(trailData);
    });

  document.getElementById("table-trail").addEventListener("click", (e) => {
    const th = e.target.closest("th[data-key]");
    if (!th) return;

    sortBy(th.dataset.key);
  });
});