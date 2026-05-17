$(function () {
  $("li").each(function () {// for each li tag of the html we apply this
    const $li = $(this);

    const hasSublist = $li.children("ul, ol").length > 0;
    if (!hasSublist) return;

    $li.addClass("has-sublist");

    $li.on("click", function (e) { // on click that is not input, we apply class
      if ($(e.target).is("input")) return;

      $li.toggleClass("open");
    });
  });
});