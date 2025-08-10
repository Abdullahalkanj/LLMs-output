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

  // let currentQuestionId
  // let playerName
  // let startTime
  // let endTime
  // let timer
  window.currentQuestionId = 0
  window.playerName = ''
  window.startTime = 0
  window.endTime = 0


  function getPlayerName () {
    playerName = document.getElementById('username')?.value || ''
    if (!playerName) {
      alert('Please enter a nickname to start the game.')
      return false
    }
    return true
  }

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
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(response => response.json())
      .then(question => showQuestion(question))
  }

  function showQuestion (question) {
    currentQuestionId = question.id
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`
    resetAndStartTimer()
    const optionsContainer = document.getElementById('optionsContainer')
    if (!optionsContainer) return
    optionsContainer.innerHTML = ''

    if (question.alternatives) {
      Object.entries(question.alternatives).forEach(([key, value]) => {
        const label = document.createElement('label')
        const radioButton = document.createElement('input')
        radioButton.setAttribute('type', 'radio')
        radioButton.setAttribute('name', 'answer')
        radioButton.setAttribute('value', key)
        radioButton.addEventListener('keypress', function (event) {
          if (event.key === 'Enter') {
            event.preventDefault()
            submitAnswer()
          }
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
      textAnswerInput?.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
          submitAnswer()
        }
      })
      const submitButton = document.getElementById('submit-answer')
      submitButton?.removeEventListener('click', submitAnswer)
      submitButton?.addEventListener('click', submitAnswer)
    }
  }

  function submitAnswer (selectedAnswer = null) {
    let answer
    const radioButtons = document.getElementsByName('answer')
    for (const radioButton of radioButtons) {
      if (radioButton.checked) {
        selectedAnswer = radioButton.value
        break
      }
    }
    const textAnswerInput = document.getElementById('textAnswer')
    if (textAnswerInput) {
      answer = textAnswerInput.value.trim()
    } else if (selectedAnswer !== null) {
      answer = selectedAnswer
    } else {
      showResult()
      return
    }
    clearInterval(timer)

    sendAnswer(currentQuestionId, answer)
      .then(response => handleAnswerResponse(response))
      .catch(error => {
        if (error.message === 'Incorrect answer') {
          document.getElementById('quizTitle').textContent = error.message
          document.getElementById('timeTaken').style.display = 'none'
          clearInterval(timer)
          gameOver('Wrong answer <😔> game over.')
        } else {
          console.error('Error submitting answer:', error)
        }
      })
  }

  function handleAnswerResponse (response) {
    document.getElementById('quizTitle').textContent = response.message || ''
    currentQuestionId = 0
    if (!response.nextURL) {
      gameOver('Congrats, You win! Good work.')
      showResult(response)
    } else {
      fetch(response.nextURL)
        .then(response => response.json())
        .then(nextQuestion => showQuestion(nextQuestion))
    }
  }

  async function sendAnswer (questionId, answer) {
    const response = await fetch(`https://courselab.lnu.se/quiz/answer/${questionId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ answer })
    })
    if (!response.ok && response.status === 400) {
      throw new Error('Incorrect answer')
    }
    return await response.json()
  }

  function showResult (gameFinished) {
    const timeTakenElement = document.getElementById('timeTaken')
    const highScoreLink = document.getElementById('highScoreLink')
    document.getElementById('Quiz').style.display = 'none'
    document.getElementById('resultContainer').style.display = 'block'
    endTime = Date.now()
    const timeSpent = endTime - startTime
    const seconds = ((timeSpent % 60000) / 1000).toFixed(0)
    if (gameFinished && playerName) {
      saveHighScore(startTime, playerName)
    }
    document.getElementById('restartGameButton').addEventListener('click', restartGame)
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'block'
    if (timeTakenElement) {
      timeTakenElement.textContent = `${playerName} Time taken:  ${seconds} seconds`
    }
    if (highScoreLink) {
      highScoreLink.style.display = 'block'
    }
  }

  function restartGame () {
    currentQuestionId = 0
    document.getElementById('resultContainer').style.display = 'none'
    document.getElementById('startContainer').style.display = 'block'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'none'
    document.getElementById('highScoreLink').style.display = 'none'
    document.getElementById('quizTitle').textContent = 'The Quiz'
    const username = document.getElementById('username')
    if (username) username.value = ''
  }

  function resetAndStartTimer () {
    clearInterval(timer)
    startTimer()
  }

  function startTimer () {
    let timeLeft = 10
    const timeElement = document.getElementById('time')
    if (timeElement) {
      timeElement.textContent = timeLeft
      timeElement.style.color = 'green'
    }
    timer = setInterval(() => {
      if (timeElement) {
        timeElement.textContent = Math.floor(timeLeft)
      }
      timeLeft = (timeLeft - 0.1).toFixed(1)
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up <br> Game Over")
        const timeTaken = document.getElementById('timeTaken')
        if (timeTaken) timeTaken.style.display = 'none'
        return
      }
      if (timeElement) {
        timeElement.textContent = timeLeft
        if (timeLeft < 4) {
          timeElement.style.color = 'red'
        } else if (timeLeft < 7) {
          timeElement.style.color = 'orange'
        }
      }
    }, 195)
  }

  function gameOver (message) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('timer').style.display = 'none'
    const resultMessage = document.getElementById('resultMessage')
    if (resultMessage) resultMessage.textContent = message
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
