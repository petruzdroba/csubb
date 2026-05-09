$(function () {
  const $ageInput = $("#age");

  const $rescue = $('input[value="rescue"]');
  const $fireRanger = $('input[value="fire_ranger"]');

  $ageInput.on("input", function () {
    const age = parseInt($(this).val());

    if (isNaN(age)) return;

    if (age < 25) {
      $rescue.prop("disabled", true).prop("checked", false);
      $fireRanger.prop("disabled", true).prop("checked", false);
    } else {
      $rescue.prop("disabled", false);
      $fireRanger.prop("disabled", false);
    }
  });

  const $zoneSelect = $("#zone");
  const $parkSelect = $("#park");

  $.getJSON("parks.json")
    .done(function (data) {
      // fill zones
      Object.keys(data).forEach(function (zone) {
        $zoneSelect.append(
          $("<option>").val(zone).text(zone)
        );
      });

      $zoneSelect.on("change", function () {
        const selectedZone = $(this).val();

        $parkSelect.html('<option>Select park</option>');

        if (!selectedZone) return;

        data[selectedZone].forEach(function (park) {
          $parkSelect.append(
            $("<option>").val(park).text(park)
          );
        });
      });
    });
});