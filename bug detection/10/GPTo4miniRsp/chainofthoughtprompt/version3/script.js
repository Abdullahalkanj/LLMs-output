import { showHighScores, saveHighScore } from './highScore.js';

document.addEventListener('DOMContentLoaded', () => {
  // Cache DOM elements
  const startBtn = document.getElementById('start-btn');
  const restartBtn = document.getElementById('restartGameButton');
  const highScoreLink = document.getElementById('highScoreLink');
  const usernameInput = document.getElementById('username');
  const startContainer = document.getElementById('startContainer');
  const quizContainer  = document.getElementById('Quiz');
  const timerContainer = document.getElementById('timer');
  const resultContainer= document.getElementById('resultContainer');

  // Game state
  // let currentQuestionId = 0;
  // let playerName = '';
  // let startTime = 0;
  // let endTime = 0;
  let timer = null;
  let timeLeft = 0;
  window.currentQuestionId = 0
  window.playerName = ''
  window.startTime = 0
  window.endTime = 0

  // EVENT LISTENERS (once)
  startBtn.addEventListener('click', startGame);
  restartBtn.addEventListener('click', restartGame);
  highScoreLink.addEventListener('click', showHighScores);
  usernameInput.addEventListener('keypress', e => {
    if (e.key === 'Enter') {
      e.preventDefault();
      startGame();
    }
  });

  // 1) Validate and store player name
  function getPlayerName() {
    const name = usernameInput.value.trim();
    if (!name) {
      alert('Please enter your name to start the quiz.');
      return false;
    }
    playerName = name;
    return true;
  }

  // 2) Start quiz
  function startGame() {
    if (!getPlayerName()) return;           // abort on invalid name
    startTime = Date.now();
    currentQuestionId = 0;
    startContainer.style.display = 'none';
    quizContainer.style.display = 'block';
    timerContainer.style.display = 'block';
    getNextQuestion();
  }

  // 3) Fetch next question
  function getNextQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(res => res.json())
      .then(showQuestion)
      .catch(err => {
        console.error('Failed to load question:', err);
        gameOver('Unable to load question. Try again later.');
      });
  }

  // 4) Render question & options
  function showQuestion(question) {
    currentQuestionId = question.id;
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`;
    resetAndStartTimer();

    const optionsContainer = document.getElementById('optionsContainer');
    optionsContainer.innerHTML = '';

    if (question.alternatives) {
      // Multiple choice
      Object.entries(question.alternatives).forEach(([key, val]) => {
        const label = document.createElement('label');
        const radio = document.createElement('input');
        radio.type = 'radio';
        radio.name = 'answer';
        radio.value = key;
        label.append(radio, document.createTextNode(val));
        optionsContainer.appendChild(label);
      });
      const btn = document.createElement('button');
      btn.textContent = 'Submit Answer';
      btn.className = 'btn';
      btn.addEventListener('click', () => submitAnswer());
      optionsContainer.appendChild(btn);

    } else {
      // Free text
      optionsContainer.innerHTML = `
        <label for="textAnswer">Your answer:</label>
        <input type="text" id="textAnswer" placeholder="Type here">
        <button class="btn" id="submit-answer">Submit</button>
      `;
      const textInput = document.getElementById('textAnswer');
      textInput.addEventListener('keypress', e => {
        if (e.key === 'Enter') submitAnswer();
      });
      document.getElementById('submit-answer')
        .addEventListener('click', () => submitAnswer());
    }
  }

  // 5) Gather and send answer
  function submitAnswer() {
    clearInterval(timer);

    // Determine answer
    let answer = '';
    const textEl = document.getElementById('textAnswer');
    if (textEl) {
      answer = textEl.value.trim();
      if (!answer) { gameOver('No answer submitted.'); return; }
    } else {
      const radios = document.getElementsByName('answer');
      const checked = Array.from(radios).find(r => r.checked);
      if (!checked) {
        gameOver('No choice selected.'); 
        return;
      }
      answer = checked.value;
    }

    // POST to API
    sendAnswer(currentQuestionId, answer)
      .then(handleAnswerResponse)
      .catch(err => {
        if (err.message === 'Incorrect answer') {
          gameOver('Wrong answer 😔 Game over.');
        } else {
          console.error(err);
          gameOver('Submission failed. Try again.');
        }
      });
  }

  // 6) Handle API response
  function handleAnswerResponse(res) {
    // Optional message (e.g. “Correct!”)
    if (res.message) {
      document.getElementById('quizTitle').textContent = res.message;
    }

    if (!res.nextURL) {
      // No more questions → win
      endTime = Date.now();
      showResult(true);
    } else {
      // Fetch the next question
      fetch(res.nextURL)
        .then(r => r.json())
        .then(showQuestion)
        .catch(err => {
          console.error('Failed next question:', err);
          gameOver('Error loading next question.');
        });
    }
  }

  // 7) POST helper
  async function sendAnswer(questionId, answer) {
    const resp = await fetch(`https://courselab.lnu.se/quiz/answer/${questionId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ answer })
    });
    if (resp.status === 400) {
      throw new Error('Incorrect answer');
    }
    if (!resp.ok) {
      throw new Error('Network error');
    }
    return resp.json();
  }

  // 8) Show result & optionally save high score
  function showResult(didWin) {
    quizContainer.style.display = 'none';
    timerContainer.style.display = 'none';
    resultContainer.style.display = 'block';

    // Compute elapsed time
    const elapsedMs = endTime - startTime;
    const seconds = Math.round(elapsedMs / 1000);

    if (didWin) {
      saveHighScore(seconds, playerName);
    }

    document.getElementById('timeTaken')
      .textContent = `${playerName} Time taken: ${seconds} seconds`;
    highScoreLink.style.display = 'block';
  }

  // 9) Reset to start
  function restartGame() {
    // Reset state & UI
    currentQuestionId = 0;
    usernameInput.value = '';
    document.getElementById('quizTitle').textContent = 'The Quiz';
    startContainer.style.display = 'block';
    quizContainer.style.display = 'none';
    resultContainer.style.display = 'none';
    timerContainer.style.display = 'none';
  }

  // 10) Timer utils
  function resetAndStartTimer() {
    clearInterval(timer);
    startTimer();
  }

  function startTimer() {
    timeLeft = 10;
    const timeEl = document.getElementById('time');
    timeEl.style.color = 'green';

    timer = setInterval(() => {
      timeLeft -= 1;
      timeEl.textContent = timeLeft;
      if (timeLeft <= 0) {
        clearInterval(timer);
        gameOver("Time's up 😬 Game over.");
      } else if (timeLeft < 4) {
        timeEl.style.color = 'red';
      } else if (timeLeft < 7) {
        timeEl.style.color = 'orange';
      }
    }, 1000);
  }

  // 11) End game (shared)
  function gameOver(msg) {
    clearInterval(timer);
    document.getElementById('resultMessage').textContent = msg;
    endTime = Date.now();
    showResult(false);
  }

  if (typeof window !== 'undefined') {
    window.getPlayerName = getPlayerName
    window.submitAnswer = submitAnswer
    window.restartGame = restartGame
    window.showResult = showResult
    window.resetAndStartTimer = resetAndStartTimer
  }
});
