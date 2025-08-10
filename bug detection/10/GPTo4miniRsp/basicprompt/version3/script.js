import { showHighScores, saveHighScore } from './highScore.js'

document.addEventListener('DOMContentLoaded', () => {
  // Event listeners
  document.getElementById('start-btn').addEventListener('click', startGame)
  document.getElementById('restartGameButton').addEventListener('click', restartGame)
  document.getElementById('highScoreLink').addEventListener('click', showHighScores)
  document.getElementById('username').addEventListener('keypress', e => {
    if (e.key === 'Enter') {
      e.preventDefault()
      startGame()
    }
  })

  // Game state
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
      alert('Please enter your name')
      throw new Error('Username required')
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
      Object.entries(question.alternatives).forEach(([key, val]) => {
        const label = document.createElement('label')
        const rb = document.createElement('input')
        rb.type = 'radio'
        rb.name = 'answer'
        rb.value = key
        label.append(rb, document.createTextNode(val))
        container.append(label)
      })
      const btn = document.createElement('button')
      btn.textContent = 'Submit Answer'
      btn.className = 'btn'
      btn.addEventListener('click', submitAnswer)
      container.append(btn)

    } else {
      container.innerHTML = `
        <label for="textAnswer">Your answer:</label>
        <input type="text" id="textAnswer" placeholder="Type answer">
        <button class="btn" id="submit-answer">Submit</button>
      `
      document.getElementById('submit-answer')
        .addEventListener('click', submitAnswer)
      document.getElementById('textAnswer')
        .addEventListener('keypress', e => {
          if (e.key === 'Enter') submitAnswer()
        })
    }
  }

  function submitAnswer() {
    // figure out selected answer
    let answer = null
    // radio?
    for (const rb of document.getElementsByName('answer')) {
      if (rb.checked) {
        answer = rb.value
        break
      }
    }
    // text?
    const textInput = document.getElementById('textAnswer')
    if (textInput) {
      answer = textInput.value.trim()
    }

    if (!answer) {
      // no answer given
      return
    }

    clearInterval(timer)

    sendAnswer(currentQuestionId, answer)
      .then(handleAnswerResponse)
      .catch(err => {
        if (err.message === 'Incorrect answer') {
          gameOver('Wrong answer 😔 Game over.', false)
        } else {
          console.error(err)
        }
      })
  }

  function handleAnswerResponse(response) {
    if (response.message) {
      document.getElementById('quizTitle').textContent = response.message
    }
    if (!response.nextURL) {
      // no more questions → player wins
      gameOver('Congrats, You win! 🎉', true)
    } else {
      // load next
      fetch(response.nextURL)
        .then(r => r.json())
        .then(showQuestion)
        .catch(console.error)
    }
  }

  async function sendAnswer(qId, answer) {
    const res = await fetch(`https://courselab.lnu.se/quiz/answer/${qId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ answer })
    })
    if (!res.ok && res.status === 400) {
      throw new Error('Incorrect answer')
    }
    return res.json()
  }

  function showResult(timeSeconds) {
    document.getElementById('Quiz').style.display = 'none'
    const result = document.getElementById('resultContainer')
    result.style.display = 'block'
    document.getElementById('timeTaken').textContent =
      `${playerName} Time taken: ${timeSeconds} seconds`
    document.getElementById('timer').style.display = 'none'
    document.getElementById('restartGameButton').style.display = 'block'
    document.getElementById('highScoreLink').style.display = 'block'
  }

  function restartGame() {
    clearInterval(timer)
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
    const timeEl = document.getElementById('time')
    timer = setInterval(() => {
      if (timeLeft <= 0) {
        clearInterval(timer)
        gameOver("Time's up ⏰ Game over.", false)
        return
      }
      timeEl.textContent = timeLeft.toFixed(1)
      timeEl.style.color = timeLeft < 4 ? 'red' :
                           timeLeft < 7 ? 'orange' : 'green'
      timeLeft -= 0.1
    }, 100)
  }

  function gameOver(message, won) {
    clearInterval(timer)
    document.getElementById('quizTitle').textContent = 'The Quiz'
    document.getElementById('resultMessage').textContent = message

    const endTime = Date.now()
    const elapsedMs = endTime - startTime
    const seconds = (elapsedMs / 1000).toFixed(0)

    if (won) {
      saveHighScore(seconds, playerName)
    }

    showResult(seconds)
  }

  if (typeof window !== 'undefined') {
    window.getPlayerName = getPlayerName
    window.submitAnswer = submitAnswer
    window.restartGame = restartGame
    window.showResult = showResult
    window.resetAndStartTimer = resetAndStartTimer
  }
})
