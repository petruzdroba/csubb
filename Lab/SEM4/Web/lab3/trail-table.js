let trailData = [];
let sortState = { key: null, asc: true };

function renderTable(data) {
  const table = document.getElementById("table-trail");

  const header = `
    <tr>
      <th data-key="sign">Sign Type / Symbol</th>
      <th data-key="meaning">Meaning</th>
    </tr>
  `;

  const rows = data
    .map(
      (row) => `
      <tr>
        <td>${row.sign}</td>
        <td>${row.meaning}</td>
      </tr>
    `,
    )
    .join("");

  table.innerHTML = header + rows;
}

function sortBy(key) {
  if (sortState.key === key) {
    sortState.asc = !sortState.asc;
  } else {
    sortState.key = key;
    sortState.asc = true;
  }

  trailData.sort((a, b) => {
    const x = a[key].toLowerCase();
    const y = b[key].toLowerCase();

    if (x < y) return sortState.asc ? -1 : 1;
    if (x > y) return sortState.asc ? 1 : -1;
    return 0;
  });

  renderTable(trailData);
  updateHeaderState();
}

function updateHeaderState() {
  const ths = document.querySelectorAll("#table-trail th");

  ths.forEach((th) => {
    th.classList.remove("sorted-asc", "sorted-desc");

    if (th.dataset.key === sortState.key) {
      th.classList.add(sortState.asc ? "sorted-asc" : "sorted-desc");
    }
  });
}

document.addEventListener("DOMContentLoaded", () => {
  fetch("trail-data.json")
    .then((res) => res.json())
    .then((data) => {
      trailData = data;
      renderTable(trailData);
    });

  document.getElementById("table-trail").addEventListener("click", (e) => {
    if (e.target.tagName === "TH") {
      sortBy(e.target.dataset.key);
    }
  });
});