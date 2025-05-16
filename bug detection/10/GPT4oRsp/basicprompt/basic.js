// ChatGPT4o output using Basic prompt


//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Your code contains several bugs and logic issues. Below are the main problems and how to fix them:

// ✅ 1. if (empty) in getPlayerName()
 Bug: empty is undefined.

// Fix: You should check if playerName is empty instead.


if (!playerName.trim()) {
  alert("Please enter your name before starting the quiz.");
  return false;
}
return true;
// Then in startGame():


function startGame () {
  if (!getPlayerName()) return;
  ...
}
// ✅ 2. selectedAnsweranswer = radioButton.value in submitAnswer()
// // Bug: Typo in variable name — selectedAnsweranswer is not defined.

// Fix: Replace with:


selectedAnswer = radioButton.value
// ✅ 3. if (Answer !== null)
// Bug: Answer is undefined and incorrectly capitalized.

// Fix: Replace:


} else if (Answer !== null) {
// With:


} else if (selectedAnswer !== null) {
// ✅ 4. Incorrect time calculation in showResult()
// Bug:


const timeSpent = startTime / endTime
// This is incorrect. You should subtract startTime from endTime.

// Fix:

const timeSpent = endTime - startTime;
const seconds = (timeSpent / 1000).toFixed(0);
// ✅ 5. showResult(empty)
// Bug: empty is undefined again.

// Fix: Just call showResult(true) or showResult(false) depending on your logic:


showResult(true)
// ✅ 6. Double timeLeft -= 0.1 in startTimer()
// Bug: You're decrementing timeLeft twice every interval.

// Fix: Remove the extra line:


 timeLeft = (timeLeft - 0.1).toFixed(1); ← remove this
// Keep only:


timeLeft -= 0.1;
// ✅ 7. submitButton.removeEventListener('click', submitAnswer)
// Bug: This line is useless and may fail since you didn't previously add a listener.

// Fix: Just remove the removeEventListener line.

// ✅ 8. currentQuestionId = 4 in restartGame()
// Bug: Why hardcoded 4?

// Fix: Reset it to 0:


currentQuestionId = 0;
// 🔧 Optional Cleanup Suggestions:
// Declare shared variables (like playerName, currentQuestionId, etc.) outside of DOMContentLoaded for better clarity.

// Add error handling for failed fetch calls.

// Validate user input length (e.g., minimum 1-2 characters).

// Would you like me to apply all fixes and return a cleaned-up version of the full code?

// You said:
// yes
// ChatGPT said:
// Here's the cleaned-up and fully fixed version of your JavaScript code with all the bugs resolved and improvements applied:


import { showHighScores, saveHighScore } from './highScore.js'

document.addEventListener('DOMContentLoaded', () => {
  let currentQuestionId = 0
  let playerName = ''
  let startTime
  let endTime
  let timer

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

  function getPlayerName () {
    playerName = document.getElementById('username').value
    if (!playerName.trim()) {
      alert('Please enter your name before starting the quiz.')
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
      .catch(error => console.error('Failed to load question:', error))
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

  function submitAnswer () {
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
    }

    if (!answer) {
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
        .catch(error => console.error('Failed to load next question:', error))
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
    const seconds = (timeSpent / 1000).toFixed(0)

    if (gameFinished) {
      saveHighScore(startTime, playerName)
    }

    const restartGameButton = document.getElementById('restartGameButton')
    restartGameButton.addEventListener('click', restartGame)
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'block'
    timeTakenElement.textContent = `${playerName}, Time taken: ${seconds} seconds`
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
      timeLeft -= 0.1
      timeLeft = parseFloat(timeLeft.toFixed(1))

      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up. <br> Game Over")
        document.getElementById('timeTaken').style.display = 'none'
        return
      }

      timeElement.textContent = timeLeft

      if (timeLeft < 4) {
        timeElement.style.color = 'red'
      } else if (timeLeft < 7) {
        timeElement.style.color = 'orange'
      } else {
        timeElement.style.color = 'green'
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