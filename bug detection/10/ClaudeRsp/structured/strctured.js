// Claude output using Structured prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// I'll analyze the code you've provided, identify the bugs, and provide fixes.
// Code Analysis
// This appears to be JavaScript code for a quiz application. The code handles:

// Starting the quiz
// Fetching questions from an API
// Displaying questions and options
// Handling user answers
// Tracking time and score
// Displaying results

// Bugs Identified
// I've found several bugs in the code:
// 1. Empty Check in getPlayerName() Function
// There's an incomplete empty check that will cause an error:

function getPlayerName() {
  playerName = document.getElementById('username').value
  if (empty) {  // 'empty' is not defined
    alert()     // No message in alert
  }
}

// 2. Variable Name Inconsistency in submitAnswer()
// There's confusion between selectedAnswer and Answer:
for (const radioButton of radioButtons) {
  if (radioButton.checked) selectedAnsweranswer = radioButton.value  // Merged variable names
  break  // This break is also incorrectly placed inside the if block
}
// 3. Time Calculation in showResult()
// The time calculation is incorrect:
const timeSpent = startTime / endTime  // Should be endTime - startTime

// 4. Incorrect Parameter in showResult()
// The function call passes empty which is undefined:
showResult(empty)

// 5. Incorrect Reset of currentQuestionId in restartGame()
// Setting to 4 instead of 0:

currentQuestionId = 4  // Should be 0

// 6. The timer has a problem with decreasing the time
// There's repeated timeLeft -= 0.1 in the timer function.
// Fixed Code
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
    if (playerName === '') {
      alert('Please enter a username')
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
    let answer;
    
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
    } else if (!answer) {
      // No answer selected
      alert('Please select an answer')
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
      timeLeft -= 0.1
      timeLeft = parseFloat(timeLeft.toFixed(1)) // Fix floating point precision issues
      
      timeElement.textContent = timeLeft
      
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up - Game Over")
        document.getElementById('timeTaken').style.display = 'none'
        return
      }
      
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
    showResult()
  }
})

// Bug Details and Fixes
// Let me explain each bug and the fix I've implemented:
// 1. Empty Username Check
// Bug: The code was using an undefined variable empty and calling alert() without a message.
// Fix: Changed the check to verify if playerName is an empty string and added a proper alert message. Also added a return value to stop the game from starting with an empty username.
// Before
if (empty) {
  alert()
}

// After
if (playerName === '') {
  alert('Please enter a username')
  return false
}
// return true
// 2. Variable Name Confusion in submitAnswer()
// Bug: The code had a merged variable name selectedAnsweranswer and a misplaced break statement.
// Fix: Fixed the variable names and placed the break outside the if block.

// Before
if (radioButton.checked) selectedAnsweranswer = radioButton.value
break

// After
if (radioButton.checked) {
  answer = radioButton.value
  break
}

// 3. Incorrect Time Calculation
// Bug: The time calculation used division instead of subtraction: startTime / endTime.
// Fix: Changed to (endTime - startTime) / 1000 to get the seconds elapsed.

// Before
const timeSpent = startTime / endTime

// After
const timeSpent = (endTime - startTime) / 1000
const seconds = timeSpent.toFixed(1)

// 4. Incorrect Parameter in showResult()
// Bug: The function used an undefined empty variable.
// Fix: Changed to pass true to indicate the game was finished successfully.

// Before
showResult(empty)

// After
showResult(true)

// 5. Incorrect Reset Value for currentQuestionId
// Bug: The restartGame() function set currentQuestionId to 4 instead of 0.
// Fix: Changed to reset currentQuestionId to 0.
// Before
currentQuestionId = 4

// After
currentQuestionId = 0

// 6. Timer Decrement Issues
// Bug: The timer was decrementing timeLeft twice in each interval, and had a mismatch between the interval time (195ms) and decrement amount (0.1).
// Fix: Removed the duplicate decrement and adjusted the interval timing to 100ms, added precision handling to fix floating point issues.
// Before
timer = setInterval(() => {
  document.getElementById('time').textContent = Math.floor(timeLeft)
  timeLeft = (timeLeft - 0.1).toFixed(1)
  // ... code ...
  timeLeft -= 0.1
}, 195)

// After
timer = setInterval(() => {
  timeLeft -= 0.1
  timeLeft = parseFloat(timeLeft.toFixed(1)) // Fix floating point precision issues
  timeElement.textContent = timeLeft
  // ... code ...
}, 100)
// 7. Other Improvements

// Added proper error handling in submitAnswer()
// Fixed HTML rendering (changed textContent to innerHTML where HTML content is expected)
// Removed incorrect reset of currentQuestionId to 0 in handleAnswerResponse()
// Added proper default parameter in showResult(gameFinished = false)

// The fixed code should now work correctly for the quiz application.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


// Research Team:
// 1 bug passed out of 6