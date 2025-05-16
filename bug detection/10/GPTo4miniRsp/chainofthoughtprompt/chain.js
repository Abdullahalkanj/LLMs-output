// ChatGPT4omini output using Chain-of-thought prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Let’s walk through the most glaring issues one by one, then I’ll show you a cleaned-up version of the critical bits.

// 1. getPlayerName uses an undefined empty

function getPlayerName () {
  playerName = document.getElementById('username').value
  if (empty){
    alert()
  }
}
// Problem: There is no variable called empty. You probably meant to check whether playerName is empty.

// Fix:


function getPlayerName() {
  playerName = document.getElementById('username').value.trim();
  if (!playerName) {
    alert('Please enter your name');
    return false;
  }
  return true;
}
2. In startGame, we should bail out if name is missing
Right now startGame() just calls getPlayerName() but doesn’t check its result.

js
Copy
Edit
function startGame () {
  if (!getPlayerName()) return;    // ← stop here if no name
  startTime = Date.now();
  ...
}
// 3. The submitAnswer logic has multiple typos
// Original:

function submitAnswer (selectedAnswer = null) {
  let answer
  const radioButtons = document.getElementsByName('answer')
  for (const radioButton of radioButtons) {
    if (radioButton.checked) selectedAnsweranswer = radioButton.value
    break
  }
  // …
  } else if (Answer !== null) {
    answer = selectedAnswer
  }
  // …
}
// Typos:

// selectedAnsweranswer doesn’t exist.

// The break is outside the if so you only ever look at the very first radio button.

// You test Answer (uppercase) instead of selectedAnswer.

// Fix:


function submitAnswer() {
  // 1) pull out whichever answer they chose
  let selectedAnswer = null;
  const radios = document.getElementsByName('answer');
  for (const r of radios) {
    if (r.checked) {
      selectedAnswer = r.value;
      break;
    }
  }

  // 2) if it’s a free-text question, grab that instead
  const textInput = document.getElementById('textAnswer');
  let answer;
  if (textInput) {
    answer = textInput.value.trim();
    if (!answer) {
      // no text entered
      showResult();
      return;
    }
  } else if (selectedAnswer !== null) {
    answer = selectedAnswer;
  } else {
    // nothing chosen or entered
    showResult();
    return;
  }

  clearInterval(timer);
  sendAnswer(currentQuestionId, answer)
    .then(handleAnswerResponse)
    .catch(/* … */);
}
// 4. You’re resetting currentQuestionId to 0 on every answer

function handleAnswerResponse (response) {
  // …
  currentQuestionId = 0
  if (!response.nextURL) { … }
  else { fetch(response.nextURL)… }
}
// That will always send you back to question 1. Remove that reset so you progress through the quiz correctly.

// 5. Time calculation is backwards

const timeSpent = startTime / endTime;    // nonsense!

// You want to subtract timestamps, not divide them:


const elapsed = endTime - startTime;          // ms elapsed
// 6. showResult’s gameFinished flag is mis-used
// You pass showResult(empty) or call showResult() with no argument, so gameFinished is always undefined. Instead, call it explicitly true or false:


// when the user wins:
gameOver('…congrats…');    // inside gameOver you already call showResult(true)
// on a wrong answer you might do:
gameOver('…sorry…');        // which calls showResult(false)
// And define:


function showResult(gameFinished) {
  // only save if gameFinished true
  if (gameFinished) {
    saveHighScore(seconds, playerName);
  }
  // …
}
// Putting it all together
// Here’s a trimmed-down example of the main control flow with the above fixes applied:


import { showHighScores, saveHighScore } from './highScore.js';

document.addEventListener('DOMContentLoaded', () => {
  let currentQuestionId, playerName, startTime, timer;

  // ——— Name handling ———
  function getPlayerName() {
    playerName = document.getElementById('username').value.trim();
    if (!playerName) {
      alert('Please enter your name');
      return false;
    }
    return true;
  }

  // ——— Start / Restart ———
  function startGame() {
    if (!getPlayerName()) return;
    startTime = Date.now();
    currentQuestionId = 0;
    document.getElementById('startContainer').style.display = 'none';
    document.getElementById('Quiz').style.display = 'block';
    document.getElementById('timer').style.display = 'block';
    getNextQuestion();
  }

  function restartGame() {
    // reset everything back to the intro screen…
  }

  // ——— Fetching & Showing Questions ———
  function getNextQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(r => r.json())
      .then(showQuestion);
  }

  function showQuestion(question) {
    currentQuestionId = question.id;
    // render question text & inputs…
    resetAndStartTimer();
  }

  // ——— Answer submission ———
  function submitAnswer() {
    clearInterval(timer);

    // figure out which answer
    let selectedAnswer = null;
    document.getElementsByName('answer').forEach(r => {
      if (r.checked) selectedAnswer = r.value;
    });

    const textInput = document.getElementById('textAnswer');
    let answer;
    if (textInput) {
      answer = textInput.value.trim();
      if (!answer) return showResult(false);
    } else if (selectedAnswer !== null) {
      answer = selectedAnswer;
    } else {
      return showResult(false);
    }

    sendAnswer(currentQuestionId, answer)
      .then(handleAnswerResponse)
      .catch(err => {
        if (err.message === 'Incorrect answer') {
          gameOver('Wrong answer 😔 Game over.');
        } else {
          console.error(err);
        }
      });
  }

  function handleAnswerResponse(response) {
    if (!response.nextURL) {
      // no more questions → user won
      gameOver('Congrats, you win! 🎉', true);
    } else {
      fetch(response.nextURL).then(r => r.json()).then(showQuestion);
    }
  }

  // ——— Game Over & Results ———
  function gameOver(message, didWin = false) {
    clearInterval(timer);
    document.getElementById('quizTitle').textContent = message;
    document.getElementById('timer').style.display = 'none';

    const endTime = Date.now();
    const elapsed = Math.round((endTime - startTime) / 1000);

    showResult(didWin, elapsed);
  }

  function showResult(didWin, seconds) {
    document.getElementById('Quiz').style.display = 'none';
    document.getElementById('resultContainer').style.display = 'block';
    if (didWin) {
      saveHighScore(seconds, playerName);
    }
    document.getElementById('timeTaken').textContent =
      `${playerName} Time taken: ${seconds} seconds`;
    document.getElementById('timer').style.display = 'none';
    document.getElementById('restartGameButton').style.display = 'block';
    document.getElementById('highScoreLink').style.display = 'block';
  }

  // ——— Timer ———
  function resetAndStartTimer() {
    clearInterval(timer);
    startTimer();
  }

  function startTimer() {
    let timeLeft = 10.0;
    const timeEl = document.getElementById('time');
    timeEl.style.color = 'green';
    timer = setInterval(() => {
      timeLeft = parseFloat((timeLeft - 0.1).toFixed(1));
      if (timeLeft <= 0) {
        clearInterval(timer);
        gameOver("Time's up! Game Over.", false);
        return;
      }
      timeEl.textContent = timeLeft;
      if (timeLeft < 4) timeEl.style.color = 'red';
      else if (timeLeft < 7) timeEl.style.color = 'orange';
    }, 100);
  }

  // ——— Wire up buttons ———
  document.getElementById('start-btn').addEventListener('click', startGame);
  document.getElementById('restartGameButton').addEventListener('click', restartGame);
  document.getElementById('highScoreLink').addEventListener('click', showHighScores);
  document.getElementById('username').addEventListener('keypress', e => {
    if (e.key === 'Enter') startGame();
  });
});
// Summary of the fixes

// Checked for an actual username string, instead of the undefined empty.

// Early-exit startGame if no name was entered.

// Cleaned up the radio/text‐answer logic by using one selectedAnswer variable.

// Removed the accidental reset of currentQuestionId to 0 in each answer.

// Switched time math from division to subtraction for a meaningful elapsed time.

// Made the showResult/gameOver calls explicit about win vs. loss so you only save a high score when appropriate.

// With those changes in place your quiz should flow correctly through each question, enforce a username, calculate time properly, and only save a high score on a win.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
