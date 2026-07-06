let trailData = [];
let sortState = { key: null, asc: true };

function renderTable(data) {
  const header = `
    <tr>
      <th data-key="sign">Sign Type / Symbol</th>
      <th data-key="meaning">Meaning</th>
    </tr>
  `;

  const rows = data
    .map(row => `
      <tr>
        <td>${row.sign}</td>
        <td>${row.meaning}</td>
      </tr>
    `)
    .join("");

  $("#table-trail").html(header + rows);
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
  const $ths = $("#table-trail th");

  $ths.removeClass("sorted-asc sorted-desc");

  $ths.each(function () {
    const $th = $(this);

    if ($th.data("key") === sortState.key) {
      $th.addClass(sortState.asc ? "sorted-asc" : "sorted-desc");
    }
  });
}

$(function () {
  $.getJSON("trail-data.json")
    .done(function (data) {
      trailData = data;
      renderTable(trailData);
    });

  $("#table-trail").on("click", "th", function (e) {
    sortBy($(this).data("key"));
  });
});