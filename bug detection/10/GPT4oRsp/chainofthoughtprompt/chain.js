// ChatGPT4o output using chain-of-thought prompt


//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Let's walk through the code step-by-step and highlight potential issues before suggesting any fixes.

// ✅ Overview of What the Code Does
// This JavaScript module handles a quiz game where:

// The user starts the game by entering a name.

// Each question is fetched from an API.

// The user answers either via radio buttons or text input.

// Timer functionality adds urgency.

// High scores are saved and shown.

// 🚨 Issues Identified and Why
// getPlayerName() — Broken if (empty)

// Issue: empty is undefined. This will cause a runtime error.

// Fix Needed: You likely meant to check if playerName is empty using if (!playerName).

// submitAnswer() — Typo: selectedAnsweranswer

// Issue: Variable assignment is malformed:


if (radioButton.checked) selectedAnsweranswer = radioButton.value
// selectedAnsweranswer is not defined anywhere and likely a typo.

// Fix Needed: Should be selectedAnswer = radioButton.value

submitAnswer() — Incorrect variable reference: Answer

// Issue: Answer is not defined; JavaScript is case-sensitive. This line:


else if (Answer !== null)
// should be else if (selectedAnswer !== null)

// showResult() — Incorrect time calculation

// Issue:


const timeSpent = startTime / endTime 
// This incorrectly divides timestamps rather than subtracting them.

// Fix Needed: Use:


const timeSpent = endTime - startTime
// showResult() — Unclear boolean check: if (gameFinished)

// Issue: showResult(empty) is called, but empty is undefined again. That results in undefined passed as the argument.

// Fix Needed: Pass a proper boolean like true or false to indicate if the game finished successfully.

// Redundant removeEventListener()

// Issue:


submitButton.removeEventListener('click', submitAnswer)
// This is unnecessary since the button was just created and has no existing listener.

// Hardcoded restart behavior

// Issue:


currentQuestionId = 4
// on restart seems arbitrary. Should it be 0 to restart from the beginning?

// Inconsistent camelCase

// Variables like Quiz, startContainer, textAnswer, timeTaken are accessed via getElementById() and work only if matching the exact HTML id. This isn't an error but can lead to confusion.

// ✅ Suggested Fixes
// Here’s the corrected version of key areas based on the reasoning above:

// ✅ getPlayerName():

function getPlayerName () {
  playerName = document.getElementById('username').value.trim()
  if (!playerName) {
    alert("Please enter your name to start the quiz.")
  }
}
// ✅ submitAnswer():

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
        gameOver('Wrong answer <😔> game over.')
      } else {
        console.error('Error submitting answer:', error)
      }
    })
}
// ✅ showResult():

function showResult (gameFinished) {
  const timeTakenElement = document.getElementById('timeTaken')
  const highScoreLink = document.getElementById('highScoreLink')
  document.getElementById('Quiz').style.display = 'none'
  const resultContainer = document.getElementById('resultContainer')
  resultContainer.style.display = 'block'
  endTime = Date.now()
  const timeSpent = endTime - startTime
  const seconds = ((timeSpent % 60000) / 1000).toFixed(0)

  if (gameFinished) {
    saveHighScore(startTime, playerName)
  }

  const restartGameButton = document.getElementById('restartGameButton')
  restartGameButton.addEventListener('click', restartGame)
  document.getElementById('timer').style.display = 'none'
  document.getElementById('restartGameButton').style.display = 'block'
  timeTakenElement.textContent = `${playerName} Time taken:  ${seconds} seconds`
  highScoreLink.style.display = 'block'
}
// Would you like me to go ahead and patch the entire code for you into a cleaned-up, working version?

// You said:
// yes
// ChatGPT said:
// Here is your cleaned-up and corrected version of the quiz game code, with all issues fixed and logic made more robust:
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

  function getPlayerName () {
    playerName = document.getElementById('username').value.trim()
    if (!playerName) {
      alert("Please enter your name to start the quiz.")
    }
  }

  function startGame () {
    getPlayerName()
    if (!playerName) return

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
      submitButton.addEventListener('click', submitAnswer)
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
          gameOver('Wrong answer <😔> game over.')
        } else {
          console.error('Error submitting answer:', error)
        }
      })
  }

  function handleAnswerResponse (response) {
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
    const resultContainer = document.getElementById('resultContainer')
    resultContainer.style.display = 'block'
    endTime = Date.now()
    const timeSpent = endTime - startTime
    const seconds = ((timeSpent % 60000) / 1000).toFixed(0)

    if (gameFinished) {
      saveHighScore(startTime, playerName)
    }

    const restartGameButton = document.getElementById('restartGameButton')
    restartGameButton.addEventListener('click', restartGame)
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'block'
    timeTakenElement.textContent = `${playerName} Time taken:  ${seconds} seconds`
    highScoreLink.style.display = 'block'
  }

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

  function startTimer () {
    let timeLeft = 10
    const timeElement = document.getElementById('time')
    timeElement.textContent = timeLeft
    timeElement.style.color = 'green'
    timer = setInterval(() => {
      timeElement.textContent = Math.floor(timeLeft)
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up <br> Game Over")
        document.getElementById('timeTaken').style.display = 'none'
        return
      }
      if (timeLeft < 4) {
        timeElement.style.color = 'red'
      } else if (timeLeft < 7) {
        timeElement.style.color = 'orange'
      }
      timeLeft = (timeLeft - 0.1).toFixed(1)
    }, 195)
  }

  function gameOver (message) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('resultMessage').innerHTML = message
    showResult(false)
  }
})
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------