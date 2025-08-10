import { showHighScores, saveHighScore } from './highScore.js';

document.addEventListener('DOMContentLoaded', () => {
  // Event listeners
  document.getElementById('start-btn').addEventListener('click', startGame);
  document.getElementById('restartGameButton').addEventListener('click', restartGame);
  const highScoreButton = document.getElementById('highScoreLink');
  highScoreButton.addEventListener('click', showHighScores);
  const usernameInput = document.getElementById('username');
  usernameInput.addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
      event.preventDefault();
      startGame();
    }
  });

  // Game state variables
  // let currentQuestionId;
  // let playerName;
  // let startTime;
  // let endTime;
  // let timer;
  window.currentQuestionId = 0
  window.playerName = ''
  window.startTime = 0
  window.endTime = 0

  //updated
  function getPlayerName() {
    playerName = document.getElementById('username').value.trim();
    if (!playerName) {
      alert('Please enter your name!');
      return false;
    }
    return true;
  }
//updated
  function startGame() {
    if (!getPlayerName()) return;
    currentQuestionId = 0;
    document.getElementById('startContainer').style.display = 'none';
    document.getElementById('Quiz').style.display = 'block';
    document.getElementById('timer').style.display = 'block';
    getNextQuestion();
  }

  function getNextQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(response => response.json())
      .then(question => showQuestion(question))
      .catch(console.error);
  }

  function showQuestion(question) {
    currentQuestionId = question.id;
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`;
    resetAndStartTimer();

    const optionsContainer = document.getElementById('optionsContainer');
    optionsContainer.innerHTML = '';

    if (question.alternatives) {
      Object.entries(question.alternatives).forEach(([key, value]) => {
        const label = document.createElement('label');
        const radioButton = document.createElement('input');
        radioButton.type = 'radio';
        radioButton.name = 'answer';
        radioButton.value = key;

        radioButton.addEventListener('keypress', event => {
          if (event.key === 'Enter') submitAnswer();
        });

        label.appendChild(radioButton);
        label.appendChild(document.createTextNode(value));
        optionsContainer.appendChild(label);
      });

      const submitButton = document.createElement('button');
      submitButton.textContent = 'Submit Answer';
      submitButton.className = 'btn';
      submitButton.addEventListener('click', submitAnswer);
      optionsContainer.appendChild(submitButton);

    } else {
      optionsContainer.innerHTML = `
        <label for="textAnswer">Press Enter or click Submit to send your answer:</label>
        <input type="text" id="textAnswer" placeholder="Your answer">
        <button class="btn" id="submit-answer">Submit</button>
      `;
      const textAnswerInput = document.getElementById('textAnswer');
      textAnswerInput.addEventListener('keypress', e => {
        if (e.key === 'Enter') submitAnswer();
      });
      const submitButton = document.getElementById('submit-answer');
      submitButton.addEventListener('click', submitAnswer);
    }
  }

  //updated
  function submitAnswer() {
    // 1) Read selected radio button (if any)
    const radioButtons = document.getElementsByName('answer');
    let selectedAnswer = null;
    for (const rb of radioButtons) {
      if (rb.checked) {
        selectedAnswer = rb.value;
        break;
      }
    }

    /// 2) Read text answer (if radio wasn’t used)
    const textInput = document.getElementById('textAnswer');
    let answer;
    if (textInput) {
      answer = textInput.value.trim();
    } else if (selectedAnswer !== null) {
      answer = selectedAnswer;
    } else {
      // No answer given: treat as end of quiz
      showResult(/* gameFinished= */ false);
      return;
    }

    clearInterval(timer);

    sendAnswer(currentQuestionId, answer)
      .then(handleAnswerResponse)
      .catch(error => {
        if (error.message === 'Incorrect answer') {
          gameOver('Wrong answer 😔 Game over.');
        } else {
          console.error('Error submitting answer:', error);
        }
      });
  }

  function handleAnswerResponse(response) {
    if (response.message) {
      document.getElementById('quizTitle').textContent = response.message;
    }

    if (!response.nextURL) {
      gameOver('Congrats, you win! 🎉');
      showResult(true);
    } else {
      fetch(response.nextURL)
        .then(res => res.json())
        .then(nextQuestion => showQuestion(nextQuestion))
        .catch(console.error);
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
    return await res.json();
  }

  //updated
  function showResult(gameFinished) {
    document.getElementById('Quiz').style.display = 'none';
    const resultContainer = document.getElementById('resultContainer');
    resultContainer.style.display = 'block';
    endTime = Date.now();
    // … other DOM updates …
    const timeSpentMs = endTime - startTime;
    const seconds = Math.floor(timeSpentMs / 1000);

    if (gameFinished) {
      saveHighScore(seconds, playerName);
    }

    const timeTakenElement = document.getElementById('timeTaken');
    timeTakenElement.textContent = `${playerName} — Time taken: ${seconds} seconds`;
  
    // document.getElementById('timeTaken')
    //     .textContent = `${playerName} Time taken: ${seconds} seconds`;

        
    document.getElementById('timer').style.display = 'none';
    document.getElementById('restartGameButton').style.display = 'block';
    document.getElementById('highScoreLink').style.display = 'block';
    document.getElementById('restartGameButton')
            .addEventListener('click', restartGame);
  }

  function restartGame() {
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
    let timeLeft = 10;
    const timeElement = document.getElementById('time');
    timeElement.textContent = timeLeft.toFixed(1);
    timeElement.style.color = 'green';

    timer = setInterval(() => {
      timeLeft -= 0.1;
      if (timeLeft <= 0) {
        clearInterval(timer);
        gameOver("Time's up 😱");
        return;
      }
      timeElement.textContent = timeLeft.toFixed(1);
      if (timeLeft < 4) {
        timeElement.style.color = 'red';
      } else if (timeLeft < 7) {
        timeElement.style.color = 'orange';
      }
    }, 100);
  }

  function gameOver(message) {
    clearInterval(timer);
    document.getElementById('quizTitle').textContent = 'The Quiz';
    document.getElementById('resultMessage').textContent = message;
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
