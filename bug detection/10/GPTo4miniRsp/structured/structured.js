// ChatGPT4omini output using Structured prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// The root cause of your “answer never gets submitted” bug is a combination of three typos/logical errors in your submitAnswer routine:

// selectedAnsweranswer


if (radioButton.checked) selectedAnsweranswer = radioButton.value
// You meant to write


selectedAnswer = radioButton.value
// as you declared function submitAnswer(selectedAnswer = null), but the extra “answer” makes it a reference to an undefined variable.

// Unconditional break
// Because you didn’t wrap your break in braces, it always fires on the very first iteration of the loop—so you never actually scan past the first radio button:


for (…) {
  if (…) selectedAnswer = …
  break       // ← always runs immediately
}
// What you wanted was:

for (…) {
  if (…) {
    selectedAnswer = …
    break
  }
}
// Checking the wrong variable
// Later you do:


} else if (Answer !== null) {
  answer = selectedAnswer
}
// But there is no Answer variable; you almost certainly meant selectedAnswer.

// Here’s a corrected version of submitAnswer (and a small cleanup in showResult)

function submitAnswer() {
  // 1) figure out whether it's a multiple-choice or text answer:
  let answer = null;

  // multiple-choice radios
  const radios = document.getElementsByName('answer');
  for (const radio of radios) {
    if (radio.checked) {
      answer = radio.value;
      break;
    }
  }

  // free-text input
  const textInput = document.getElementById('textAnswer');
  if (textInput) {
    const trimmed = textInput.value.trim();
    if (trimmed !== '') {
      answer = trimmed;
    }
  }

  // if still no answer, bail out
  if (answer === null) {
    showResult();
    return;
  }

  clearInterval(timer);

  sendAnswer(currentQuestionId, answer)
    .then(handleAnswerResponse)
    .catch(error => {
      if (error.message === 'Incorrect answer') {
        document.getElementById('quizTitle').textContent = error.message;
        document.getElementById('timeTaken').style.display = 'none';
        gameOver('Wrong answer <😔> game over.');
      } else {
        console.error('Error submitting answer:', error);
      }
    });
}
// And while we’re here, there’s also a small logical bug in your time-calculations in showResult:


function showResult(gameFinished) {
  // …
  endTime = Date.now();

  // originally: const timeSpent = startTime / endTime
  // should be:
  const timeSpent = endTime - startTime;               // milliseconds elapsed
  const seconds   = Math.round(timeSpent / 1000);      // convert to seconds

  if (gameFinished) {
    // also: probably pass the playerName _and_ the time taken, 
    // not startTime twice
    saveHighScore(playerName, seconds);
  }
  // …
  timeTakenElement.textContent = 
    `${playerName} Time taken: ${seconds} seconds`;
}
// With those three fixes:

// typo in the radio-button loop

// correctly breaking only when you’ve found a checked button

// testing the right variable (selectedAnswer)

// and using the proper time-difference (endTime - startTime)

// —your quiz will actually pick up and submit the user’s answer, and report the right score.
// i said 
// give me the whole code 


// ChatGPT said:
// Here’s the full quiz.js (or whatever you’ve named it) with all of the fixes applied—most importantly to submitAnswer, to the time-difference calculation in showResult, and a proper empty-username check:

import { showHighScores, saveHighScore } from './highScore.js'

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('start-btn').addEventListener('click', startGame)
  document.getElementById('restartGameButton').addEventListener('click', restartGame)
  const highScoreButton = document.getElementById('highScoreLink')
  highScoreButton.addEventListener('click', showHighScores)

  const usernameInput = document.getElementById('username')
  usernameInput.addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
      event.preventDefault()
      startGame()
    }
  })

  let currentQuestionId
  let playerName
  let startTime
  let endTime
  let timer

  function getPlayerName() {
    playerName = document.getElementById('username').value.trim()
    if (!playerName) {
      alert('Please enter your name before starting!')
      return false
    }
    return true
  }

  function startGame() {
    if (!getPlayerName()) return

    startTime = Date.now()
    currentQuestionId = 0

    document.getElementById('startContainer').style.display = 'none'
    document.getElementById('Quiz').style.display = 'block'
    document.getElementById('timer').style.display = 'block'

    getNextQuestion()
  }

  function getNextQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(res => res.json())
      .then(question => showQuestion(question))
      .catch(err => console.error('Failed to load question:', err))
  }

  function showQuestion(question) {
    currentQuestionId = question.id
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`
    resetAndStartTimer()

    const optionsContainer = document.getElementById('optionsContainer')
    optionsContainer.innerHTML = ''

    if (question.alternatives) {
      // Multiple choice
      Object.entries(question.alternatives).forEach(([key, text]) => {
        const label = document.createElement('label')
        const radio = document.createElement('input')
        radio.type = 'radio'
        radio.name = 'answer'
        radio.value = key

        // Allow Enter key on radio
        radio.addEventListener('keypress', e => {
          if (e.key === 'Enter') submitAnswer()
        })

        label.appendChild(radio)
        label.appendChild(document.createTextNode(text))
        optionsContainer.appendChild(label)
      })

      const submitBtn = document.createElement('button')
      submitBtn.textContent = 'Submit Answer'
      submitBtn.className = 'btn'
      submitBtn.addEventListener('click', submitAnswer)
      optionsContainer.appendChild(submitBtn)

    } else {
      // Free-text
      optionsContainer.innerHTML = `
        <label for="textAnswer">Press Enter or click Submit to submit your answer:</label>
        <input type="text" id="textAnswer" placeholder="Your answer">
        <button class="btn" id="submit-answer">Submit</button>
      `
      const textInput = document.getElementById('textAnswer')
      textInput.addEventListener('keypress', e => {
        if (e.key === 'Enter') submitAnswer()
      })
      const submitBtn = document.getElementById('submit-answer')
      submitBtn.addEventListener('click', submitAnswer)
    }
  }

  function submitAnswer() {
    let answer = null

    // 1) Check radios
    const radios = document.getElementsByName('answer')
    for (const radio of radios) {
      if (radio.checked) {
        answer = radio.value
        break
      }
    }

    // 2) Check text input
    const textInput = document.getElementById('textAnswer')
    if (textInput) {
      const val = textInput.value.trim()
      if (val !== '') {
        answer = val
      }
    }

    // 3) If still no answer, just show results (user clicked Submit without selecting anything)
    if (answer === null) {
      showResult(false)
      return
    }

    clearInterval(timer)

    sendAnswer(currentQuestionId, answer)
      .then(handleAnswerResponse)
      .catch(err => {
        if (err.message === 'Incorrect answer') {
          document.getElementById('quizTitle').textContent = err.message
          document.getElementById('timeTaken').style.display = 'none'
          gameOver('Wrong answer <😔> game over.')
        } else {
          console.error('Error submitting answer:', err)
        }
      })
  }

  function handleAnswerResponse(response) {
    if (response.message) {
      document.getElementById('quizTitle').textContent = response.message
    }
    if (!response.nextURL) {
      gameOver('Congrats, You win! Good work.')
    } else {
      fetch(response.nextURL)
        .then(res => res.json())
        .then(nextQ => showQuestion(nextQ))
        .catch(err => console.error('Failed to load next question:', err))
    }
  }

  async function sendAnswer(questionId, answer) {
    const res = await fetch(`https://courselab.lnu.se/quiz/answer/${questionId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ answer })
    })
    if (!res.ok && res.status === 400) {
      throw new Error('Incorrect answer')
    }
    return res.json()
  }

  function showResult(gameFinished) {
    document.getElementById('Quiz').style.display = 'none'
    const resultContainer = document.getElementById('resultContainer')
    const timeTakenEl   = document.getElementById('timeTaken')
    const highScoreLink = document.getElementById('highScoreLink')

    endTime = Date.now()
    const elapsedMs = endTime - startTime
    const seconds   = Math.round(elapsedMs / 1000)

    if (gameFinished) {
      saveHighScore(playerName, seconds)
    }

    resultContainer.style.display = 'block'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'block'
    highScoreLink.style.display = 'block'

    timeTakenEl.textContent =
      `${playerName} Time taken: ${seconds} second${seconds === 1 ? '' : 's'}`
  }

  function restartGame() {
    // reset everything
    currentQuestionId = 0
    document.getElementById('resultContainer').style.display = 'none'
    document.getElementById('startContainer').style.display = 'block'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'none'
    document.getElementById('highScoreLink').style.display = 'none'
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('username').value = ''
  }

  function resetAndStartTimer() {
    clearInterval(timer)
    startTimer()
  }

  function startTimer() {
    let timeLeft = 10.0
    const timeEl = document.getElementById('time')
    timeEl.textContent = timeLeft.toFixed(1)
    timeEl.style.color   = 'green'

    timer = setInterval(() => {
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up <br> Game Over")
        document.getElementById('timeTaken').style.display = 'none'
        return
      }

      timeLeft -= 0.1
      timeEl.textContent = timeLeft.toFixed(1)

      if (timeLeft < 4) {
        timeEl.style.color = 'red'
      } else if (timeLeft < 7) {
        timeEl.style.color = 'orange'
      }
    }, 100) // 100ms interval for smooth countdown
  }

  function gameOver(message) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('resultMessage').innerHTML = message
    showResult(false)
  }
})
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


//Research Team:
//5 bugs  passed out of 6