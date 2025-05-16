// ChatGPT4o output using Structured prompt


//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// This JavaScript quiz application contains several bugs and issues that can cause unexpected behavior or failure. Below is a breakdown of the most critical bugs, their explanations, and how to fix them.

// 🔍 BUG 1: getPlayerName - Empty Name Check is Broken
// Problem:

function getPlayerName () {
  playerName = document.getElementById('username').value
  if (empty){
    alert()
  }
}
// empty is not defined — this causes a runtime error.

// No check is performed to ensure playerName is not blank.

// alert() is called with no message.

// ✅ Fix:

function getPlayerName () {
  playerName = document.getElementById('username').value.trim()
  if (!playerName) {
    alert('Please enter your name before starting the game.')
    throw new Error('Player name is required.')
  }
}
// Also, in startGame(), wrap getPlayerName() in a try-catch to avoid continuing if name is invalid.


function startGame () {
  try {
    getPlayerName()
  } catch {
    return
  }
  ...
}
// 🔍 BUG 2: Typo in submitAnswer → selectedAnsweranswer
// Problem:

if (radioButton.checked) selectedAnsweranswer = radioButton.value
// selectedAnsweranswer is not a variable.

// Likely intended: selectedAnswer = radioButton.value.

// ✅ Fix:

if (radioButton.checked) selectedAnswer = radioButton.value
// 🔍 BUG 3: Incorrect logic in submitAnswer → Answer is undefined
// Problem:

} else if (Answer !== null) {
  answer = selectedAnswer
}
// Answer is not declared. This is a reference error.

// Likely meant: selectedAnswer !== null.

// ✅ Fix:

} else if (selectedAnswer !== null) {
  answer = selectedAnswer
}
// 🔍 BUG 4: startTime / endTime in showResult → Incorrect time calculation
// Problem:

// const timeSpent = startTime / endTime 
// This performs division, not subtraction.

// timeSpent should be the difference in milliseconds.

// ✅ Fix:

const timeSpent = endTime - startTime
// 🔍 BUG 5: Misuse of empty in handleAnswerResponse
// Problem:

showResult(empty)
// empty is undefined here. This will cause a runtime error.

// ✅ Fix:
// Use true to indicate game finished.


showResult(true)
// 🔍 BUG 6: submitButton.removeEventListener is unnecessary
// Problem:

submitButton.removeEventListener('click', submitAnswer)
submitButton.addEventListener('click', submitAnswer)
// You're adding a new button each time. There is no existing listener to remove on a new element.

// removeEventListener is ineffective unless the exact same function reference was previously attached.

// ✅ Fix:
// Just keep the addEventListener:


submitButton.addEventListener('click', submitAnswer)
// 🔍 BUG 7: Inconsistent currentQuestionId reset
// Problem:

currentQuestionId = 0
// In handleAnswerResponse, this line appears before checking if response.nextURL exists. It might cause issues with ID tracking.

// ✅ Fix:
Remove that line unless needed, or place it only in restartGame().

// 🔍 BUG 8: restartGame() sets currentQuestionId = 4
// Problem:

currentQuestionId = 4  
// Why 4? Shouldn’t a new game start from question 0?

// ✅ Fix:

currentQuestionId = 0
// ✅ Summary of Fixes (Quick List)
// Location	Fix
// getPlayerName()	Check for empty name and define playerName properly
// submitAnswer()	Fix selectedAnsweranswer typo and use selectedAnswer correctly
// submitAnswer()	Replace Answer !== null with selectedAnswer !== null
// showResult()	Use subtraction for time calculation: endTime - startTime
// handleAnswerResponse()	Replace showResult(empty) with showResult(true)
// submitButton setup	Remove unnecessary removeEventListener
// restartGame()	Reset currentQuestionId = 0
// Would you like me to apply all these corrections and give you a cleaned-up version of the full code?

// You said:
// yes
// ChatGPT said:
// Here is the cleaned-up version of your full code with all bugs fixed and improvements applied:
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
      alert('Please enter your name before starting the game.')
      throw new Error('Player name is required.')
    }
  }

  function startGame () {
    try {
      getPlayerName()
    } catch {
      return
    }

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
    let answer = null
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
          gameOver('Wrong answer 😔 Game over.')
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
    timeTakenElement.textContent = `${playerName} Time taken: ${seconds} seconds`
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
      timeLeft = (timeLeft - 0.1).toFixed(1)
      timeElement.textContent = timeLeft

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