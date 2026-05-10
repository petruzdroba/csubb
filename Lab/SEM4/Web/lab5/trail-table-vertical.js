let trailData = [];
let sortState = { key: null, asc: true };

function renderVerticalTable(data) {
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

  $("#table-trail").html(headerRow + bodyRows);

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
  const $headers = $("#table-trail th[data-key]");

  $headers.removeClass("sorted-asc sorted-desc");

  $headers.each(function () {
    const $h = $(this);

    if ($h.data("key") === sortState.key) {
      $h.addClass(sortState.asc ? "sorted-asc" : "sorted-desc");
    }
  });
}

$(function () {
  $.getJSON("trail-data.json")
    .done(function (data) {
      trailData = data;
      renderVerticalTable(trailData);
    });

  $("#table-trail").on("click", "th[data-key]", function () {
    sortBy($(this).data("key"));
  });
});