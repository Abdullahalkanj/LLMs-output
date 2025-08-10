import { showHighScores, saveHighScore } from './highScore.js'

document.addEventListener('DOMContentLoaded', () => {
  // Event listeners
  document.getElementById('start-btn').addEventListener('click', startGame)
  document.getElementById('restartGameButton').addEventListener('click', restartGame)
  const highScoreButton = document.getElementById('highScoreLink')
  highScoreButton.addEventListener('click', showHighScores)
  document.getElementById('username').addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
      event.preventDefault()
      startGame()
    }
  })

  // Game state
  // let currentQuestionId = null
  // let playerName = ''
  // let startTime = 0
  // let timer = null

  window.currentQuestionId = 0
  window.playerName = ''
  window.startTime = 0
  window.endTime = 0

  function getPlayerName() {
    playerName = document.getElementById('username').value.trim()
    // 1. Check for empty string, not undefined variable
    if (!playerName) {
      alert('Please enter your name before starting.')
      throw new Error('No player name') // stop startGame if no name
    }
  }

  function startGame() {
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

  function getNextQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(res => res.json())
      .then(q => showQuestion(q))
      .catch(console.error)
  }

  function showQuestion(question) {
    currentQuestionId = question.id
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`
    resetAndStartTimer()

    const opts = document.getElementById('optionsContainer')
    opts.innerHTML = ''

    if (question.alternatives) {
      // multiple choice
      Object.entries(question.alternatives).forEach(([key, val]) => {
        const label = document.createElement('label')
        const rb = document.createElement('input')
        rb.type = 'radio'
        rb.name = 'answer'
        rb.value = key
        rb.addEventListener('keypress', e => {
          if (e.key === 'Enter') submitAnswer()
        })
        label.append(rb, document.createTextNode(val))
        opts.appendChild(label)
      })
      const btn = document.createElement('button')
      btn.textContent = 'Submit Answer'
      btn.className = 'btn'
      btn.addEventListener('click', submitAnswer)
      opts.appendChild(btn)

    } else {
      // text input
      opts.innerHTML = `
        <label for="textAnswer">Press enter or Submit to submit your answer:</label>
        <input type="text" id="textAnswer" placeholder="Your answer">
        <button class="btn" id="submit-answer">Submit</button>
      `
      const textInput = document.getElementById('textAnswer')
      textInput.addEventListener('keypress', e => {
        if (e.key === 'Enter') submitAnswer()
      })
      // No need to remove listener on a brand-new button
      document.getElementById('submit-answer')
        .addEventListener('click', submitAnswer)
    }
  }

  function submitAnswer() {
    clearInterval(timer)

    // 2. Correctly pick the selected radio if any:
    let answer = null
    const radios = document.getElementsByName('answer')
    for (const rb of radios) {
      if (rb.checked) {
        answer = rb.value
        break  // only break when we've found the checked one
      }
    }

    // 3. Override with text input if present
    const txt = document.getElementById('textAnswer')
    if (txt) {
      answer = txt.value.trim()
    }

    if (!answer) {
      // No answer given
      gameOver('No answer provided. Game over.')
      return
    }

    // Send and handle
    sendAnswer(currentQuestionId, answer)
      .then(res => handleAnswerResponse(res))
      .catch(err => {
        if (err.message === 'Incorrect answer') {
          gameOver('Wrong answer 😔 Game over.')
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
      // finished all questions
      endGame(true)
    } else {
      fetch(response.nextURL)
        .then(res => res.json())
        .then(q => showQuestion(q))
        .catch(console.error)
    }
  }

  async function sendAnswer(qId, answer) {
    const res = await fetch(`https://courselab.lnu.se/quiz/answer/${qId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ answer })
    })
    if (res.status === 400) {
      throw new Error('Incorrect answer')
    }
    return res.json()
  }

  function endGame(isWin) {
    clearInterval(timer)
    document.getElementById('Quiz').style.display = 'none'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('resultContainer').style.display = 'block'

    const timeTakenMs = Date.now() - startTime  // 4. subtraction, not division
    const seconds = Math.round(timeTakenMs / 1000)  // 5. straightforward ms→s

    if (isWin) {
      saveHighScore(seconds, playerName)  // 6. pass elapsed seconds, not startTime
    }

    document.getElementById('timeTaken').textContent =
      `${playerName} Time taken: ${seconds} second${seconds !== 1 ? 's' : ''}`

    document.getElementById('restartGameButton').style.display = 'block'
    document.getElementById('highScoreLink').style.display = 'block'
  }

  function restartGame() {
    currentQuestionId = null  // 7. clear back to “no question” state
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
    const timeEl = document.getElementById('time')
    timeEl.textContent = timeLeft
    timeEl.style.color = 'green'

    timer = setInterval(() => {
      timeLeft = +(timeLeft - 0.1).toFixed(1)  // decrement once
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up! Game over.")
        return
      }
      timeEl.textContent = timeLeft
      if (timeLeft < 4) timeEl.style.color = 'red'
      else if (timeLeft < 7) timeEl.style.color = 'orange'
    }, 100)  // fixed interval for smooth countdown
  }

  function gameOver(message) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = message
    endGame(false)  // mark as loss, so no high-score save
  }

  if (typeof window !== 'undefined') {
    window.getPlayerName = getPlayerName
    window.submitAnswer = submitAnswer
    window.restartGame = restartGame
    window.showResult = showResult
    window.resetAndStartTimer = resetAndStartTimer
  }
})
