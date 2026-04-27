let slides = [];
let index = 0; // indexul din lista al imaginii
let timer;

//ce imagine este diplayed in momentul curent
const linkEl = document.getElementById("carousel-link");
const imgEl = document.getElementById("carousel-img");
const textEl = document.getElementById("carousel-text");

function render() {
  const slide = slides[index];
  if (!slide) return;

  linkEl.href = slide.link;
  imgEl.src = slide.image;
  imgEl.alt = slide.text;
  textEl.textContent = slide.text;
}

function next() { // exp n=5, index = 5, 6 % 5 =1 => incarcam slide-ul 1
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

document.addEventListener("DOMContentLoaded", () => {
  fetch("carousel.json")
    .then((res) => {
      if (!res.ok) throw new Error("Failed to load carousel.json");
      return res.json();
    })
    .then((data) => {
      slides = data;
      index = 0;

      render();
      startAuto();
    })
    .catch((err) => {
      console.error("Carousel load error:", err);
    });

  document.querySelector(".next").addEventListener("click", () => { // selectam clasele next si prev si ascultam pt un click
    next();
    resetAuto(); // reset the timer, de ex timer 2 sec + next => render() + timer la 0 din nou
  });

  document.querySelector(".prev").addEventListener("click", () => {
    prev();
    resetAuto();
  });
});