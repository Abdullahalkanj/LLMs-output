import { showHighScores, saveHighScore } from './highScore.js'

document.addEventListener('DOMContentLoaded', () => {
  // Event listeners
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

  // Game state variables
  // let currentQuestionId
  // let playerName
  // let startTime
  // let endTime
  // let timer

  window.currentQuestionId = 0
  window.playerName = ''
  window.startTime = 0
  window.endTime = 0
//updated
  function getPlayerName() {
    playerName = document.getElementById('username').value.trim()
    if (!playerName) {
      alert('Please enter your name to start!')
      return false
    }
    return true
  }
//updated
  function startGame () {
    if (!getPlayerName()) return  
    startTime = Date.now()
    currentQuestionId = 0
    document.getElementById('startContainer').style.display = 'none'
    document.getElementById('Quiz').style.display = 'block'
    document.getElementById('timer').style.display = 'block'
    getNextQuestion()
  }


  function getNextQuestion () {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId +1}`)
      .then(response => response.json())
      .then(question => showQuestion(question))
  }

  function showQuestion (question) {
    currentQuestionId = question.id
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`
    resetAndStartTimer()

    const optionsContainer = document.getElementById('optionsContainer')
    optionsContainer.innerHTML = ''

    if (question.alternatives) {
      Object.entries(question.alternatives).forEach(([key, value]) => {
        const label = document.createElement('label')
        const radioButton = document.createElement('input')
        radioButton.setAttribute('type', 'radio')
        radioButton.setAttribute('name', 'answer')
        radioButton.setAttribute('value', key)

        radioButton.addEventListener('keypress', function (event) {
          if (event.key === 'Enter') submitAnswer()
        })

        label.appendChild(radioButton)
        label.appendChild(document.createTextNode(value))
        optionsContainer.appendChild(label)
      })

      const submitButton = document.createElement('button')
      submitButton.textContent = 'Submit Answer'
      submitButton.className = 'btn'
      submitButton.addEventListener('click', submitAnswer)
      optionsContainer.appendChild(submitButton)
    } else {
      optionsContainer.innerHTML = `
        <label for="textAnswer">Press enter or Submit to submit your answer:</label>
        <input type="text" id="textAnswer" placeholder="Your answer">
        <button class="btn" id="submit-answer">Submit</button>
      `
      const textAnswerInput = document.getElementById('textAnswer')
      textAnswerInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') submitAnswer()
      })

      const submitButton = document.getElementById('submit-answer')
      submitButton.removeEventListener('click', submitAnswer)
      submitButton.addEventListener('click', submitAnswer)
    }
  }
//updated
  function submitAnswer() {
    clearInterval(timer)

    // 1) Grab whichever answer is present:
    let answer = null
    const radios = document.getElementsByName('answer')
    for (const radio of radios) {
      if (radio.checked) {
        answer = radio.value
        break
      }
    }
    if (!answer) {
      const textInput = document.getElementById('textAnswer')
      if (textInput) answer = textInput.value.trim()
    }

    if (!answer) {
      // no answer selected/typed
      alert('Select or type an answer before submitting!')
      startTimer()  // resume timer
      return
    }

    sendAnswer(currentQuestionId, answer)
      .then(handleAnswerResponse)
      .catch(err => {
        if (err.message === 'Incorrect answer') {
          gameOver('Wrong answer 😔 Game over.')
        } else {
          console.error(err)
        }
      })
  }
  
//updated
  function handleAnswerResponse(response) {
    if (response.message) {
      document.getElementById('quizTitle').textContent = response.message
    }
    if (!response.nextURL) {
      // end of quiz
      const elapsedMs = Date.now() - startTime
      showResult(true, elapsedMs)
    } else {
      // fetch the next one; don't reset currentQuestionId to 0
      fetch(response.nextURL)
        .then(r => r.json())
        .then(q => showQuestion(q))
    }
  }
  
  async function sendAnswer (questionId, answer) {
    const response = await fetch(`https://courselab.lnu.se/quiz/answer/${questionId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        answer
      })
    })
    if (!response.ok && response.status === 400) {
      throw new Error('Incorrect answer')
    }
    return await response.json()
  }

//updated
  function showResult(didWin, elapsedMs) {
    clearInterval(timer);
    document.getElementById('Quiz').style.display = 'none';
    const resultContainer = document.getElementById('resultContainer');
    resultContainer.style.display = 'block';
    document.getElementById('timer').style.display = 'none';

    const seconds = Math.round(elapsedMs / 1000);
    document.getElementById('timeTaken').textContent =
      `${playerName} Time taken: ${seconds} seconds`;

    if (didWin) {
      saveHighScore(seconds, playerName);
    }

    document.getElementById('restartGameButton').style.display = 'block';
    document.getElementById('highScoreLink').style.display = 'block';
  }
//updated
  function restartGame () {
    currentQuestionId = 0 
    document.getElementById('resultContainer').style.display = 'none'
    document.getElementById('startContainer').style.display = 'block'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'none'
    document.getElementById('highScoreLink').style.display = 'none'
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('username').value = ''
  }

  function resetAndStartTimer () {
    clearInterval(timer)
    startTimer()
  }
//updated
  function startTimer() {
    let timeLeft = 10.0
    timer = setInterval(() => {
      timeLeft = Math.max(0, +(timeLeft - 0.1).toFixed(1))
      updateTimerDisplay(timeLeft)
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up 😱 Game over.")
      }
    }, 100)
  }

  function gameOver (message) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('resultMessage').textContent = message
    showResult()
  }

  if (typeof window !== 'undefined') {
    window.getPlayerName = getPlayerName
    window.submitAnswer = submitAnswer
    window.restartGame = restartGame
    window.showResult = showResult
    window.resetAndStartTimer = resetAndStartTimer
  }
})
