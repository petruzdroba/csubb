let slides = [];
let index = 0;
let timer;

const $link = $("#carousel-link");
const $img = $("#carousel-img");
const $text = $("#carousel-text");

function render() {
  const slide = slides[index];
  if (!slide) return;

  $link.attr("href", slide.link);
  $img.attr("src", slide.image).attr("alt", slide.text);
  $text.text(slide.text);
}

function next() {
  index = (index + 1) % slides.length;
  render();
}

function prev() {
  index = (index - 1 + slides.length) % slides.length;
  render();
}

function startAuto() {
  timer = setInterval(next, 3000);
}

function resetAuto() {
  clearInterval(timer);
  startAuto();
}

$(function () {
  $.getJSON("carousel.json")
    .done(function (data) {
      slides = data;
      index = 0;

      render();
      startAuto();
    })
    .fail(function (err) {
      console.error("Carousel load error:", err);
    });

  $(".next").on("click", function () {
    next();
    resetAuto();
  });

  $(".prev").on("click", function () {
    prev();
    resetAuto();
  });
});