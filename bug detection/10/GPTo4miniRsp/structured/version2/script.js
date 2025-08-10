import { showHighScores, saveHighScore } from './highScore.js';

document.addEventListener('DOMContentLoaded', () => {
  // Event listeners
  document.getElementById('start-btn').addEventListener('click', startGame);
  document.getElementById('restartGameButton').addEventListener('click', restartGame);
  document.getElementById('highScoreLink').addEventListener('click', showHighScores);
  document.getElementById('username').addEventListener('keypress', event => {
    if (event.key === 'Enter') {
      event.preventDefault();
      startGame();
    }
  });

  // Game state
  // let currentQuestionId = 0;
  // let playerName = '';
  // let startTime = 0;
  // let endTime = 0;
  // let timer = null;
  window.currentQuestionId = 0
  window.playerName = ''
  window.startTime = 0
  window.endTime = 0

  function getPlayerName() {
    playerName = document.getElementById('username').value.trim();
    if (!playerName) {
      alert('Please enter your name.'); // provide user feedback
      throw new Error('No player name');
    }
  }

  function startGame() {
    try {
      getPlayerName();
    } catch {
      return; // abort if no name
    }
    startTime = Date.now();
    currentQuestionId = 0;
    document.getElementById('startContainer').style.display = 'none';
    document.getElementById('Quiz').style.display = 'block';
    document.getElementById('timer').style.display = 'block';
    getNextQuestion();
  }

  function getNextQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(res => res.json())
      .then(showQuestion)
      .catch(err => console.error('Error fetching question', err));
  }

  function showQuestion(question) {
    currentQuestionId = question.id;
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`;
    resetAndStartTimer();

    const container = document.getElementById('optionsContainer');
    container.innerHTML = '';

    if (question.alternatives) {
      Object.entries(question.alternatives).forEach(([key, text]) => {
        const label = document.createElement('label');
        const radio = document.createElement('input');
        radio.type = 'radio';
        radio.name = 'answer';
        radio.value = key;
        label.append(radio, text);
        container.appendChild(label);
      });
      const btn = document.createElement('button');
      btn.textContent = 'Submit Answer';
      btn.className = 'btn';
      btn.addEventListener('click', submitAnswer);
      container.appendChild(btn);
    } else {
      container.innerHTML = `
        <label for="textAnswer">Your answer:</label>
        <input type="text" id="textAnswer" placeholder="Type and press Enter or Submit">
        <button class="btn" id="submit-answer">Submit</button>
      `;
      document.getElementById('textAnswer').addEventListener('keypress', e => {
        if (e.key === 'Enter') submitAnswer();
      });
      const btn = document.getElementById('submit-answer');
      btn.addEventListener('click', submitAnswer);
    }
  }

  function submitAnswer() {
    // 1. Determine selected answer
    let selectedAnswer = null;
    for (const radio of document.getElementsByName('answer')) {
      if (radio.checked) {
        selectedAnswer = radio.value;
        break;
      }
    }

    // 2. If free-text
    const textInput = document.getElementById('textAnswer');
    const answer = textInput
      ? textInput.value.trim()
      : selectedAnswer;

    if (!answer) {
      // No answer chosen or entered
      showResult(false);
      return;
    }

    clearInterval(timer);

    sendAnswer(currentQuestionId, answer)
      .then(handleAnswerResponse)
      .catch(err => {
        if (err.message === 'Incorrect answer') {
          gameOver('Wrong answer 😔 Game over.');
        } else {
          console.error('Error submitting answer:', err);
        }
      });
  }

  function handleAnswerResponse(response) {
    if (response.message) {
      document.getElementById('quizTitle').textContent = response.message;
    }
    if (!response.nextURL) {
      // No more questions
      gameOver('Congrats, You win! Good work.', true);
    } else {
      // Continue to next
      fetch(response.nextURL)
        .then(res => res.json())
        .then(showQuestion)
        .catch(err => console.error('Error fetching next question', err));
    }
  }

  async function sendAnswer(questionId, answer) {
    const res = await fetch(`https://courselab.lnu.se/quiz/answer/${questionId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ answer })
    });
    if (res.status === 400) {
      throw new Error('Incorrect answer');
    }
    return res.json();
  }

  function showResult(gameFinished) {
    // Compute elapsed time
    endTime = Date.now();
    const elapsedMs = endTime - startTime;
    const seconds = Math.floor(elapsedMs / 1000);

    if (gameFinished) {
      saveHighScore(seconds, playerName); // pass elapsed time
    }

    document.getElementById('Quiz').style.display = 'none';
    document.getElementById('timer').style.display = 'none';
    const result = document.getElementById('resultContainer');
    result.style.display = 'block';
    document.getElementById('timeTaken').textContent =
      `${playerName} Time taken: ${seconds} seconds`;
    document.getElementById('highScoreLink').style.display = 'block';
    document.getElementById('restartGameButton').style.display = 'block';
  }

  function restartGame() {
    // Reset all state
    currentQuestionId = 0;
    document.getElementById('resultContainer').style.display = 'none';
    document.getElementById('startContainer').style.display = 'block';
    document.getElementById('timer').style.display = 'none';
    document.getElementById('restartGameButton').style.display = 'none';
    document.getElementById('highScoreLink').style.display = 'none';
    document.getElementById('quizTitle').textContent = 'The Quiz';
    document.getElementById('username').value = '';
  }

  function resetAndStartTimer() {
    clearInterval(timer);
    startTimer();
  }

  function startTimer() {
    let timeLeft = 10.0;
    const el = document.getElementById('time');
    el.textContent = timeLeft.toFixed(1);
    el.style.color = 'green';

    timer = setInterval(() => {
      timeLeft -= 0.1;               // single decrement
      if (timeLeft <= 0) {
        clearInterval(timer);
        gameOver("Time's up 😢 Game Over", false);
        return;
      }
      el.textContent = timeLeft.toFixed(1);
      if (timeLeft < 4) el.style.color = 'red';
      else if (timeLeft < 7) el.style.color = 'orange';
    }, 100);
  }

  function gameOver(message, didWin) {
    clearInterval(timer);
    document.getElementById('quizTitle').textContent = message;
    showResult(didWin);
  }

  if (typeof window !== 'undefined') {
    window.getPlayerName = getPlayerName
    window.submitAnswer = submitAnswer
    window.restartGame = restartGame
    window.showResult = showResult
    window.resetAndStartTimer = resetAndStartTimer
  }
});
