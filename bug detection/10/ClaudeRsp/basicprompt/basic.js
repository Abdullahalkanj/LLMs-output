// Claude output using basic prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

// I'll analyze the code and identify bugs. This appears to be a quiz game application with timer functionality and high score tracking. Let me find and fix the issues:
// Bugs Identified

// Empty validation in getPlayerName(): The empty check is incorrectly implemented
// Alert function call has no parameters
// Variable reference issues in the submitAnswer function
// Missing break statement in the radio button loop
// Incorrect time calculation in showResult
// Empty parameter passed to showResult

// Let me fix these issues one by one:
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
    playerName = document.getElementById('username').value
    if (playerName.trim() === '') {
      alert('Please enter a username')
      return false
    }
    return true
  }

  function startGame() {
    if (!getPlayerName()) {
      return
    }
    startTime = Date.now()
    currentQuestionId = 0
    document.getElementById('startContainer').style.display = 'none'
    document.getElementById('Quiz').style.display = 'block'
    document.getElementById('timer').style.display = 'block'
    getNextQuestion()
  }


  function getNextQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(response => response.json())
      .then(question => showQuestion(question))
  }

  function showQuestion(question) {
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

  function submitAnswer() {
    let answer
    const radioButtons = document.getElementsByName('answer')
    
    for (const radioButton of radioButtons) {
      if (radioButton.checked) {
        answer = radioButton.value
        break
      }
    }
  
    const textAnswerInput = document.getElementById('textAnswer')
    if (textAnswerInput) {
      answer = textAnswerInput.value.trim()
    } else if (!answer) {
      // No answer selected or entered
      alert('Please select or enter an answer')
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
          gameOver('Wrong answer 😔 game over.')
        } else {
          console.error('Error submitting answer:', error)
        }
      })
  }
  

  function handleAnswerResponse(response) {
    if (response.message) {
      document.getElementById('quizTitle').textContent = response.message
    }
    
    if (!response.nextURL) {
      gameOver('Congrats, You win! Good work.')
      showResult(true)
    } else {
      fetch(response.nextURL)
        .then(response => response.json())
        .then(nextQuestion => showQuestion(nextQuestion))
    }
  }
  
  async function sendAnswer(questionId, answer) {
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

  function showResult(gameFinished = false) {
    const timeTakenElement = document.getElementById('timeTaken')
    const highScoreLink = document.getElementById('highScoreLink')
    document.getElementById('Quiz').style.display = 'none'
    const resultContainer = document.getElementById('resultContainer')
    resultContainer.style.display = 'block'
    endTime = Date.now()
    const timeSpent = (endTime - startTime) 
    const seconds = (timeSpent / 1000).toFixed(1)
    
    if (gameFinished) {
      saveHighScore(timeSpent, playerName)
    }
    
    const restartGameButton = document.getElementById('restartGameButton')
    restartGameButton.addEventListener('click', restartGame)
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'block'
    timeTakenElement.textContent = `${playerName} Time taken: ${seconds} seconds`
    highScoreLink.style.display = 'block'
  }

  function restartGame() {
    currentQuestionId = 0  // Fixed from 4 to 0
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
    let timeLeft = 10
    const timeElement = document.getElementById('time')
    timeElement.textContent = timeLeft
    timeElement.style.color = 'green'
    timer = setInterval(() => {
      timeLeft = (timeLeft - 0.1).toFixed(1)
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up - Game Over")
        document.getElementById('timeTaken').style.display = 'none'
        return
      }
      timeElement.textContent = timeLeft
      if (timeLeft < 4) {
        timeElement.style.color = 'red'
      } else if (timeLeft < 7) {
        timeElement.style.color = 'orange'
      }
    }, 100) // Fixed from 195 to make it closer to 100ms for accurate timing
  }

  function gameOver(message) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('resultMessage').innerHTML = message
    showResult()
  }
})

// Bug Fixes Explained
// I've fixed several bugs in the code:

// Fixed the getPlayerName function:

// Added proper empty check with playerName.trim() === ''
// Added a proper alert message
// Added return values to signal success/failure


// Fixed submitAnswer function:

// Fixed variable references (selectedAnsweranswer → answer)
// Added a missing break statement in the radio button loop
// Improved logic for determining the answer
// Fixed the answer validation flow


// Fixed time calculation in showResult:

// Changed startTime / endTime to endTime - startTime
// Properly calculated seconds with timeSpent / 1000


// Fixed handleAnswerResponse:

// Removed currentQuestionId = 0 which incorrectly reset the question ID
// Fixed parameter for showResult (changed empty to true)


// Fixed restartGame:

// Changed currentQuestionId = 4 to currentQuestionId = 0 to properly reset


// Fixed timer function:

// Removed redundant timeLeft decrement that was occurring twice
// Changed interval from 195ms to 100ms for more accurate timing
// Fixed the HTML message format in gameOver with proper usage of innerHTML


// General improvements:

// Fixed parameter handling with default values
// Improved error checking



// These changes should make the quiz function properly, maintaining the correct timer, properly handling answers, and correctly tracking scores.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
