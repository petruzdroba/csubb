const cardFront = document.querySelector('.card-front');
const cardBack = document.querySelector('.card-back');
const nextButton = document.querySelector('.next-button');
const switchButton = document.querySelector('.switch-course');
const numberButton = document.querySelector('.number-button');
const scoreButton = document.querySelector('.score-button');
const cardInner = document.querySelector('.card-inner');

let score = 0;
let count = 0;
let flashcards = [];
let answered = [];
let missed = [];
let total = 0;
let start = 0;
let courses = [
    { name: 'Web', file: 'flashcards_web.json' },
    { name: 'Web 2', file: 'flashcards_web_2.json'},
    { name: 'Web 3', file: 'flashcards_web_3.json'},
];
let emojis = ["💻", "🌐", "📱", "⌨️", "🖱️", "🔗", "📧", "🔐", "⚙️", "🚀", "📡"];

function fetchInput(inputfile) {
    fetch(inputfile)
        .then(response => response.json())
        .then(data => {
            flashcards = data;
            total = flashcards.length;
        })
        .catch(error => console.error('Error loading JSON:', error));
}

function getRandomFlashcard() {
    const randomIndex = Math.floor(Math.random() * flashcards.length);
    return flashcards[randomIndex];
}

function shootEmoji() {
    const emojiEl = document.createElement("div");
    emojiEl.textContent = emojis[Math.floor(Math.random() * emojis.length)];
    emojiEl.className = 'emoji';
    document.body.appendChild(emojiEl);

    const vw = window.innerWidth;
    const vh = window.innerHeight;

    const direction = Math.floor(Math.random() * 4);

    let startX, startY, endX, endY;
    switch (direction) {
        case 0: 
            startX = 0;
            endX = vw;
            startY = endY = Math.random() * vh;
            break;
        case 1: 
            startX = vw;
            endX = 0;
            startY = endY = Math.random() * vh;
            break;
        case 2: 
            startY = 0;
            endY = vh;
            startX = endX = Math.random() * vw;
            break;
        case 3: 
            startY = vh;
            endY = 0;
            startX = endX = Math.random() * vw;
            break;
    }
    console.log("start x, y: ", startX, startY);
    console.log("end x, y: ", endX, endY);

    let controlX, controlY;

    if (direction === 0 || direction === 1) {
        controlX = (startX + endX) / 2;
        controlY = startY + (Math.random() < 0.5 ? -150 : 150);
    } else {
        controlY = (startY + endY) / 2;
        controlX = startX + (Math.random() < 0.5 ? -150 : 150);
    }


    gsap.set(emojiEl, { x: startX, y: startY });
    gsap.to(emojiEl, {
        duration: 3,
        ease: "power1.inOut",
        motionPath: {
            path: [
                { x: startX, y: startY },
                { x: controlX, y: controlY },
                { x: endX, y: endY }
            ],
            curviness: 1.25,
        },
        onComplete: () => emojiEl.remove()
    });
}

function checkEnd() {
    if (flashcards.length === 0) {
        cardFront.textContent = "Aceasta a fost ultima intrebare";
        cardBack.textContent = "Da refresh la pagina pentru a incepe din nou. ";
        setTimeout(() => {
            cardBack.style.visibility = 'visible';
        }, 300);
        return true;
    }
}

function setFlashcard(flashcard) {
    cardFront.innerHTML = '';
    cardBack.innerHTML = '';

    const questionWrapperFront = document.createElement('div');
    questionWrapperFront.className = 'question-wrapper';

    const questionWrapperBack = document.createElement('div');
    questionWrapperBack.className = 'question-wrapper';

    const questionElFront = document.createElement('h3');
    questionElFront.textContent = flashcard.question;
    questionElFront.style.margin = '0';

    const questionElBack = document.createElement('h3');
    questionElBack.textContent = flashcard.question;
    questionElBack.style.margin = '0';

    questionWrapperFront.appendChild(questionElFront);
    cardFront.appendChild(questionWrapperFront);
    cardFront.appendChild(document.createElement('br'));

    questionWrapperBack.appendChild(questionElBack);
    cardBack.appendChild(questionWrapperBack);
    cardBack.appendChild(document.createElement('br'));

    if (flashcard.options && Array.isArray(flashcard.options)) {
        const shuffledOptions = [...flashcard.options];
        for (let i = shuffledOptions.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [shuffledOptions[i], shuffledOptions[j]] = [shuffledOptions[j], shuffledOptions[i]];
        }
        shuffledOptions.forEach(opt => {
            const label = document.createElement('label');
            label.className = 'cheeky-checkbox-label'

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.value = opt;
            checkbox.className = 'cheeky-checkbox'

            label.appendChild(checkbox);
            label.appendChild(document.createTextNode(' ' + opt));

            cardFront.appendChild(label);
        });
    }

    window.currentFlashcard = flashcard;
}

function checkAnswer() {
    if (!currentFlashcard) return;
    if (answered.includes(window.currentFlashcard)) return;
    answered.push(window.currentFlashcard);
    const checkboxes = cardFront.querySelectorAll('input[type="checkbox"]');
    const selectedOptions = [];
    checkboxes.forEach(cb => {
        if (cb.checked) selectedOptions.push(cb.value);
    });

    let correct = false;
    if (Array.isArray(currentFlashcard.answer)) {
        const sortedSelected = selectedOptions.slice().sort();
        const sortedAnswer = currentFlashcard.answer.slice().sort();
        correct = sortedSelected.length === sortedAnswer.length &&
            sortedSelected.every((val, idx) => val === sortedAnswer[idx]);
    } else {
        correct = selectedOptions.length === 1 && selectedOptions[0] === currentFlashcard.answer;
    }

    if (Array.isArray(currentFlashcard.answer)) {
        for (let ans of currentFlashcard.answer) {
            var answer = document.createElement('label');
            answer.innerHTML = "• " + ans;
            answer.className = 'cheeky-checkbox-label';
            cardBack.appendChild(answer);
        }
    } else {
        var answer = document.createElement('label');
        answer.innerHTML = "• " + currentFlashcard.answer;
        cardBack.appendChild(answer);
    }

    if (correct && !currentFlashcard.answered) {
        score++;
        scoreButton.textContent = "✅ " + score;
        currentFlashcard.answered = true;
        var times = Math.floor(Math.random() * 10) + 5;
        for (var i = 0; i < times; i++) {
            setTimeout(() => {
                shootEmoji();
            }, i * 300);
        }
    }
    if (!correct)
        missed.push(currentFlashcard);

    if (!correct) {
        missed.push(currentFlashcard);
        cardInner.classList.add('flipped');
    }
}

function updateCard() {
    cardInner.classList.remove('flipped');
    cardBack.style.visibility = 'hidden';
    if (checkEnd()) {
        return;
    }
    if (count > 0)
        checkAnswer();
    const flashcard = getRandomFlashcard();
    numberButton.textContent = ((count++) + 1) + '/' + total;
    setFlashcard(flashcard);
    flashcards = flashcards.filter(card => card !== flashcard);
    nextButton.textContent = 'Next';
    setTimeout(() => {
        cardBack.style.visibility = 'visible';
    }, 300);
}

function updateWidth() {
    const card = document.querySelector('.card');
    if (!card) return;
    if (switchButton.textContent === 'Web') {
        card.style.width = '400px';
        card.style.height = '400px';
        document.querySelectorAll('.next-button, .switch-course, .number-button, .score-button').forEach(btn => {
            btn.style.width = '400px';
        });
    }
}

function changeCourse() {
    count = 0;
    const currentIndex = courses.findIndex(course => course.name === switchButton.textContent);
    const nextIndex = (currentIndex + 1) % courses.length;
    switchButton.textContent = courses[nextIndex].name;
    fetchInput(courses[nextIndex].file);
    updateWidth();
}
gsap.registerPlugin(MotionPathPlugin);

cardInner.addEventListener('click', (e) => {
    const tag = e.target.tagName.toLowerCase();
    if (tag === 'input' || tag === 'label' || tag === 'button') {
        return;
    }
    cardInner.classList.toggle('flipped');
});

fetchInput('flashcards_web.json');
if (flashcards.length > 0)
    updateCard();

nextButton.addEventListener('click', updateCard);
switchButton.addEventListener('click', changeCourse);
scoreButton.addEventListener('click', checkAnswer);
updateWidth();
setFlashcard({ "question": "Selecteaza materia si apasa pe Next pentru a incepe!", "options": ["aceasta aplicatie a fost dezvoltata pentru a facilita procesul de invatare!", "bafta!"], "answer": [""] })