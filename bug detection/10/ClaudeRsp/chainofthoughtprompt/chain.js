// Claude output using Chain-of-thought prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// I'll examine the code and walk through my reasoning process to identify issues before suggesting fixes.
// Code Analysis
// This is a JavaScript file for a quiz game with the following main features:

// Player enters a username and starts the game
// Questions are fetched from an API
// Player answers questions with time limit
// High scores are tracked

// Let's identify potential bugs by reviewing each function:
// 1. getPlayerName() Function
function getPlayerName () {
  playerName = document.getElementById('username').value
  if (empty){
    alert()
  }
}

// Issues:

// empty is not defined
// alert() is called without parameters
// No validation logic is actually implemented

// 2. submitAnswer() Function

function submitAnswer (selectedAnswer = null) {
  let answer
  const radioButtons = document.getElementsByName('answer')
  for (const radioButton of radioButtons) {
    if (radioButton.checked) selectedAnsweranswer = radioButton.value
    break
  }
  // Issues:

  // selectedAnsweranswer is incorrectly named (should be answer = radioButton.value)
  // The break is outside the if statement, causing the loop to always break after the first iteration
  // The parameter selectedAnswer is used but then a variable selectedAnsweranswer is used
  
  // 3. showResult() Function
  function showResult (gameFinished) {
    // ...
    const timeSpent = startTime / endTime 
    // ...
    if (gameFinished) {
      saveHighScore(startTime, playerName)
    }
    // ...
  }

//   Issues:

// Time calculation is wrong: it should be endTime - startTime, not division
// The function is called with empty as parameter
// saveHighScore is called with startTime instead of calculated time

// 4. handleAnswerResponse() Function
function handleAnswerResponse (response) {
  // ...
  currentQuestionId = 0  // This resets currentQuestionId unnecessarily
  if (!response.nextURL) {
    gameOver('Congrats, You win! Good work.')
    showResult(empty)  // empty is undefined
  }
  // ...
}

// Issues:

// Resets currentQuestionId to 0 for every response
// Passes empty (undefined) parameter to showResult()

// 5. restartGame() Function

function restartGame () {
  currentQuestionId = 4  // This should probably be 0
  // ...
}

// Issues:

// Sets currentQuestionId to 4 instead of 0 or null

// Comprehensive Fix
// Based on my analysis, I'll create a fix that addresses all the issues:
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
    if (playerName === '' || playerName === null) {
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

  function submitAnswer(selectedAnswer = null) {
    let answer = null
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
    } else if (selectedAnswer !== null) {
      answer = selectedAnswer
    } else if (answer === null) {
      showResult(false)
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

  function showResult(gameFinished) {
    const timeTakenElement = document.getElementById('timeTaken')
    const highScoreLink = document.getElementById('highScoreLink')
    document.getElementById('Quiz').style.display = 'none'
    const resultContainer = document.getElementById('resultContainer')
    resultContainer.style.display = 'block'
    endTime = Date.now()
    const timeSpent = (endTime - startTime) / 1000
    const seconds = timeSpent.toFixed(1)
    
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
    }, 100)
  }

  function gameOver(message) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('resultMessage').innerHTML = message
    showResult(false)
  }
})

// Summary of Fixes
// Here's a summary of the key issues I fixed in the code:

// getPlayerName() function

// Fixed the undefined empty variable
// Added proper username validation
// Implemented a meaningful alert message
// Made the function return a boolean to control the game flow


// submitAnswer() function

// Fixed the variable name selectedAnsweranswer to answer
// Fixed the loop logic by moving the break inside the if statement
// Improved handling of different answer scenarios


// Time calculation

// Changed startTime / endTime to (endTime - startTime) / 1000 for correct time calculation
// Fixed the format to display correct time in seconds


// Fixed parameters

// Replaced all instances of the undefined empty variable with actual boolean values
// Used saveHighScore with the correct time parameter


// handleAnswerResponse() function

// Removed currentQuestionId = 0 which was incorrectly resetting question ID for every answer
// Fixed parameters for showResult() function


// restartGame() function

// Changed currentQuestionId = 4 to currentQuestionId = 0 to properly reset the game


// Timer function

// Fixed the timer logic to decrement consistently
// Fixed interval timing (set to 100ms for more accurate timing)
// Improved gameOver message display


// Other improvements

// Used proper HTML for gameOver message (changed textContent to innerHTML where needed)
// Added better handling of game state throughout the app
// Fixed redundant code in the timer function



// These fixes should resolve all the bugs in the quiz game while maintaining the original functionality and intended behavior of the application.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


// Research Team:
// 1 bug passed out of 6