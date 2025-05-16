// ChatGPT4omini output using basic prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// The show/hide flow and your timer all look solid – the blocker is in your submitAnswer() function (and a little in getPlayerName()). You never actually set selectedAnswer, you typo’d the variable name, and you accidentally break out of the loop on the first iteration; then you test Answer (capital A) instead of your selectedAnswer. Here’s the fix:

function getPlayerName () {
  -    playerName = document.getElementById('username').value
  -    if (empty){
  -      alert()
  -    }
  +    playerName = document.getElementById('username').value.trim()
  +    if (!playerName) {
  +      alert('Please enter your name')
  +      // stop the game starting if we have no name
  +      throw new Error('No player name')
  +    }
    }
  
    function submitAnswer (selectedAnswer = null) {
  -    let answer
  -    const radioButtons = document.getElementsByName('answer')
  -    for (const radioButton of radioButtons) {
  -      if (radioButton.checked) selectedAnsweranswer = radioButton.value
  -      break
  -    }
  +    let answer
  +    // first, pick up any checked radio button
  +    const radioButtons = document.getElementsByName('answer')
  +    let selectedRadio = null
  +    for (const radioButton of radioButtons) {
  +      if (radioButton.checked) {
  +        selectedRadio = radioButton.value
  +        break
  +      }
  +    }
  
  -    const textAnswerInput = document.getElementById('textAnswer')
  -    if (textAnswerInput) {
  -      answer = textAnswerInput.value.trim()
  -    } else if (Answer !== null) {
  -      answer = selectedAnswer
  -    } else {
  -      showResult()
  -      return
  -    }
  +    const textAnswerInput = document.getElementById('textAnswer')
  +    if (textAnswerInput) {
  +      answer = textAnswerInput.value.trim()
  +    } else if (selectedRadio !== null) {
  +      answer = selectedRadio
  +    } else {
  +      // no answer selected or typed
  +      showResult()
  +      return
  +    }
  
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
  
    // What changed?
    // getPlayerName
    
    // Replaced the bogus if (empty) check with if (!playerName) to catch blank input.
    
    // Trimmed the input so that “ ” also counts as empty.
    
    // Threw an error (or you could return false) to abort startGame() if no name is provided.
    
    // submitAnswer
    
    // Introduce a local selectedRadio variable, loop until you find a checked radio, then break (inside the if).
    
    // Remove the typo selectedAnsweranswer and instead assign to selectedRadio.
    
    // Test selectedRadio !== null (not Answer!) when deciding whether a radio was chosen.
    
    // Now answer will be either your text‐input or the radio value, and you won’t get a ReferenceError anymore.
    
    // With those two tweaks in place your quiz should correctly pick up the user’s name and their chosen answer.
    
    
    
    
    
    // You said:
    // give me the code 
    
    
    // ChatGPT said:
    // Here’s the full corrected script with the name‐check and answer‐selection bugs fixed. I’ve made getPlayerName() return false on empty input so we can bail out of startGame() cleanly, and rewrote submitAnswer() to properly grab whichever radio button is checked before falling back to the text input:
    import { showHighScores, saveHighScore } from './highScore.js'

    document.addEventListener('DOMContentLoaded', () => {
      document.getElementById('start-btn').addEventListener('click', startGame)
      document.getElementById('restartGameButton').addEventListener('click', restartGame)
      document.getElementById('highScoreLink').addEventListener('click', showHighScores)
      document.getElementById('username').addEventListener('keypress', function (event) {
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
        const name = document.getElementById('username').value.trim()
        if (!name) {
          alert('Please enter your name')
          return false
        }
        playerName = name
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
          .then(res => res.json())
          .then(question => showQuestion(question))
          .catch(err => console.error('Error fetching question:', err))
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
            radioButton.type = 'radio'
            radioButton.name = 'answer'
            radioButton.value = key
            radioButton.addEventListener('keypress', e => {
              if (e.key === 'Enter') submitAnswer()
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
          document.getElementById('textAnswer').addEventListener('keypress', e => {
            if (e.key === 'Enter') submitAnswer()
          })
          const submitButton = document.getElementById('submit-answer')
          submitButton.addEventListener('click', submitAnswer)
        }
      }
    
      function submitAnswer() {
        // 1) check for a selected radio:
        let selectedRadio = null
        document.getElementsByName('answer').forEach(rb => {
          if (rb.checked) selectedRadio = rb.value
        })
    
        // 2) check for a text input:
        const textInput = document.getElementById('textAnswer')
        let answer = null
        if (textInput) {
          answer = textInput.value.trim()
        } else if (selectedRadio !== null) {
          answer = selectedRadio
        } else {
          // nothing to submit
          showResult()
          return
        }
    
        clearInterval(timer)
    
        sendAnswer(currentQuestionId, answer)
          .then(handleAnswerResponse)
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
    
      function handleAnswerResponse(response) {
        if (response.message) {
          document.getElementById('quizTitle').textContent = response.message
        }
    
        if (!response.nextURL) {
          gameOver('Congrats, You win! Good work.')
          showResult(true)
        } else {
          fetch(response.nextURL)
            .then(res => res.json())
            .then(nextQuestion => showQuestion(nextQuestion))
            .catch(err => console.error('Error fetching next question:', err))
        }
      }
    
      async function sendAnswer(questionId, answer) {
        const res = await fetch(`https://courselab.lnu.se/quiz/answer/${questionId}`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ answer })
        })
        if (res.status === 400) throw new Error('Incorrect answer')
        return await res.json()
      }
    
      function showResult(gameFinished) {
        document.getElementById('Quiz').style.display = 'none'
        const resultContainer = document.getElementById('resultContainer')
        resultContainer.style.display = 'block'
        endTime = Date.now()
    
        const durationMs = endTime - startTime
        const seconds = Math.round(durationMs / 1000)
    
        if (gameFinished) {
          saveHighScore(seconds, playerName)
        }
    
        document.getElementById('timeTaken').textContent =
          `${playerName} Time taken: ${seconds} seconds`
        document.getElementById('timer').style.display = 'none'
        document.getElementById('restartGameButton').style.display = 'block'
        document.getElementById('highScoreLink').style.display = 'block'
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
        const timeEl = document.getElementById('time')
        timeEl.textContent = timeLeft
        timeEl.style.color = 'green'
        timer = setInterval(() => {
          timeLeft = (timeLeft - 0.1).toFixed(1)
          if (timeLeft <= 0) {
            clearInterval(timer)
            gameOver("Time's up <br> Game Over")
            return
          }
          timeEl.textContent = timeLeft
          if (timeLeft < 4) {
            timeEl.style.color = 'red'
          } else if (timeLeft < 7) {
            timeEl.style.color = 'orange'
          }
        }, 100)
      }
    
      function gameOver(message) {
        clearInterval(timer)
        document.getElementById('quizTitle').textContent = 'The Quiz'
        document.getElementById('resultMessage').innerHTML = message
        showResult(false)
      }
    })
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


// Research Team:
// 5 bugs passed out of 6