import { showHighScores, saveHighScore } from './highScore.js'

document.addEventListener('DOMContentLoaded', () => {
  // — Event listeners
  document.getElementById('start-btn').addEventListener('click', startGame)
  document.getElementById('restartGameButton').addEventListener('click', restartGame)
  document.getElementById('highScoreLink').addEventListener('click', showHighScores)
  document.getElementById('username').addEventListener('keypress', e => {
    if (e.key === 'Enter') {
      e.preventDefault()
      startGame()
    }
  })

  // — State
  // let currentQuestionId = 0
  // let playerName = ''
  // let startTime = 0
  // let timer = null
  window.currentQuestionId = 0
  window.playerName = ''
  window.startTime = 0
  window.endTime = 0
  
  function getPlayerName() {
    playerName = document.getElementById('username').value.trim()
    if (!playerName) {
      alert('Please enter your name.')
      throw new Error('No player name')
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
    loadQuestion()
  }

  function loadQuestion() {
    fetch(`https://courselab.lnu.se/quiz/question/${currentQuestionId + 1}`)
      .then(res => res.json())
      .then(showQuestion)
      .catch(console.error)
  }

  function showQuestion(question) {
    currentQuestionId = question.id
    document.getElementById('question').innerHTML = `<h2>${question.question}</h2>`
    resetAndStartTimer()

    const container = document.getElementById('optionsContainer')
    container.innerHTML = ''

    if (question.alternatives) {
      // Multiple choice
      Object.entries(question.alternatives).forEach(([key, text]) => {
        const label = document.createElement('label')
        const rb = document.createElement('input')
        rb.type = 'radio'
        rb.name = 'answer'
        rb.value = key
        label.append(rb, text)
        container.append(label)
      })
      const btn = document.createElement('button')
      btn.textContent = 'Submit'
      btn.className = 'btn'
      btn.addEventListener('click', () => submitAnswer())
      container.append(btn)

    } else {
      // Free text
      container.innerHTML = `
        <label for="textAnswer">Your answer:</label>
        <input type="text" id="textAnswer">
        <button class="btn" id="submitBtn">Submit</button>
      `
      document.getElementById('textAnswer')
        .addEventListener('keypress', e => { if (e.key === 'Enter') submitAnswer() })
      document.getElementById('submitBtn')
        .addEventListener('click', submitAnswer)
    }
  }

  function submitAnswer() {
    // Pick up a selected radio if any
    let answer = ''
    const radios = document.getElementsByName('answer')
    for (const r of radios) {
      if (r.checked) {
        answer = r.value
        break
      }
    }
    // Otherwise check text input
    if (!answer) {
      const txt = document.getElementById('textAnswer')
      if (txt) answer = txt.value.trim()
    }
    if (!answer) {
      // No answer at all → treat as game over
      return gameOver('No answer—game over.')
    }

    clearInterval(timer)
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

  async function sendAnswer(id, answer) {
    const res = await fetch(`https://courselab.lnu.se/quiz/answer/${id}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ answer })
    })
    if (res.status === 400) throw new Error('Incorrect answer')
    return res.json()
  }

  function handleAnswerResponse(resp) {
    if (resp.message) {
      document.getElementById('quizTitle').textContent = resp.message
    }
    if (resp.nextURL) {
      fetch(resp.nextURL)
        .then(r => r.json())
        .then(showQuestion)
      return
    }
    // No nextURL → player won
    const totalMs = Date.now() - startTime
    showResult(true, totalMs)
    gameOver('Congrats! You win!')
  }

  function showResult(finished, totalMs) {
    clearInterval(timer)
    document.getElementById('Quiz').style.display = 'none'
    document.getElementById('timer').style.display = 'none'
    const secs = Math.floor(totalMs / 1000)
    if (finished) {
      saveHighScore(secs, playerName)
    }
    document.getElementById('timeTaken').textContent =
      `${playerName}, time: ${secs} s`
    document.getElementById('resultContainer').style.display = 'block'
    document.getElementById('restartGameButton').style.display = 'block'
    document.getElementById('highScoreLink').style.display = 'block'
  }

  function restartGame() {
    clearInterval(timer)
    document.getElementById('resultContainer').style.display = 'none'
    document.getElementById('startContainer').style.display = 'block'
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'none'
    document.getElementById('highScoreLink').style.display = 'none'
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('username').value = ''
    currentQuestionId = 0
  }

  function resetAndStartTimer() {
    clearInterval(timer)
    let timeLeft = 10.0
    const el = document.getElementById('time')
    el.style.color = 'green'
    timer = setInterval(() => {
      el.textContent = timeLeft.toFixed(1)
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up! Game over.")
        return
      }
      if (timeLeft < 4) el.style.color = 'red'
      else if (timeLeft < 7) el.style.color = 'orange'
      timeLeft -= 0.1
    }, 100)
  }

  function gameOver(msg) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = msg
    // leave showResult to reveal the resultContainer
    showResult(false, Date.now() - startTime)
  }

  if (typeof window !== 'undefined') {
    window.getPlayerName = getPlayerName
    window.submitAnswer = submitAnswer
    window.restartGame = restartGame
    window.showResult = showResult
    window.resetAndStartTimer = resetAndStartTimer
  }
})
