import { expect } from 'chai'
import { JSDOM } from 'jsdom'
import sinon from 'sinon'
import fs from 'fs'
import path from 'path'
import { fileURLToPath } from 'url'
import fetch from 'node-fetch'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

// Setup JSDOM with test HTML
const dom = new JSDOM(`<!DOCTYPE html><html><body>
  <div id="start-btn"></div>
  <div id="restartGameButton"></div>
  <div id="highScoreLink"></div>
  <input id="username" />
  <div id="startContainer"></div>
  <div id="Quiz"></div>
  <div id="timer"><span id="time"></span></div>
  <div id="resultContainer"></div>
  <div id="timeTaken"></div>
  <div id="question"></div>
  <div id="optionsContainer"></div>
  <div id="resultMessage"></div>
  <div id="quizTitle"></div>
  <input type="text" id="textAnswer" />
  <div id="highscore"></div>
</body></html>`, {
  url: 'http://localhost',
  runScripts: 'dangerously',
  resources: 'usable',
})

// Assign globals
// Assign globals in the correct order
global.window = dom.window
global.document = dom.window.document

// Mock alert AFTER global.window is set
window._alertCalled = false
window.alert = () => { window._alertCalled = true }
global.alert = window.alert


global.fetch = fetch
window.fetch = fetch

// Mock highScore.js functions
window.showHighScores = () => {}
window.saveHighScore = () => {}

// Load and inject script.js content
const scriptPath = path.join(__dirname, '../script.js')
let scriptContent = fs.readFileSync(scriptPath, 'utf-8')

// Remove ESM imports to avoid breaking the test
scriptContent = scriptContent.replace(/import .* from .*/g, '')

// Inject script into the DOM
const scriptEl = dom.window.document.createElement('script')
scriptEl.type = 'text/javascript'
scriptEl.textContent = scriptContent
dom.window.document.body.appendChild(scriptEl)

// Wait for functions to attach to window
let getPlayerName, submitAnswer, restartGame

await new Promise((resolve, reject) => {
  const maxWait = 1000
  let waited = 0
  const interval = 50

  const waitForFunctions = () => {
    getPlayerName = window.getPlayerName
    submitAnswer = window.submitAnswer
    restartGame = window.restartGame

    if (getPlayerName && submitAnswer && restartGame) {
      resolve()
    } else if (waited >= maxWait) {
      reject(new Error('Functions not found on window'))
    } else {
      waited += interval
      setTimeout(waitForFunctions, interval)
    }
  }

  waitForFunctions()
})

// === TESTS ===
describe('Quiz Game Bugs', () => {
  let clock

  beforeEach(() => {
    window._alertCalled = false
    document.getElementById('username').value = ''
  })

  afterEach(() => {
    if (clock) clock.restore()
  })

  describe('Bug 1: Username validation', () => {
    it('should alert or return false if username is empty', () => {
      const result = getPlayerName()
      expect(result).to.be.false
      expect(window._alertCalled).to.be.true
    })

    it('should return true if username is not empty', () => {
      document.getElementById('username').value = 'testuser'
      const result = getPlayerName()
      expect(result).to.be.true
    })
  })

  describe('Bug 2: submitAnswer logic (radio button)', () => {
    it('should correctly select answer from checked radio button', async () => {
      const container = document.getElementById('optionsContainer')
      container.innerHTML = `
        <label><input type="radio" name="answer" value="A" checked> A</label>
        <label><input type="radio" name="answer" value="B"> B</label>
      `
      document.getElementById('textAnswer')?.remove()

      global.currentQuestionId = 1

      // Mock internals to avoid fetch call
      global.sendAnswer = async () => ({ nextURL: null, message: '' })
      global.handleAnswerResponse = () => {}
      global.clearInterval = () => {}
      global.showResult = () => {}

      const result = await submitAnswer()
    })
  })

  describe('Bug 3: showResult should not allow empty playerName', () => {
    it('should not call saveHighScore if playerName is empty', () => {
      // Setup DOM
      let input = document.getElementById('username')
      if (!input) {
        input = document.createElement('input')
        input.id = 'username'
        document.body.appendChild(input)
      }
      input.value = '' // Simulate empty username

      // Run getPlayerName to update playerName properly
      window.getPlayerName()

      // Setup timing and spying
      window.startTime = Date.now() - 5000
      window.endTime = Date.now()

      let saveCalled = false
      window.saveHighScore = () => { saveCalled = true }

      // Run showResult
      window.showResult(true)

      // Assert
      expect(saveCalled).to.be.false
    })
  })


  describe('Bug 4: Time should be calculated as endTime - startTime', () => {
    it('should not produce NaN or non-positive values for time taken', () => {
      global.playerName = 'Tester'
      const start = Date.now() - 5000
      const end = Date.now()
      const totalMs = end - start

      global.startTime = start
      const timeTaken = document.getElementById('timeTaken')
      timeTaken.textContent = ''

      window.showResult(true, totalMs)

      const text = timeTaken.textContent
      const match = text.match(/(\d+)/)
      const seconds = match ? parseInt(match[1]) : NaN

      expect(seconds).to.be.a('number')
      expect(seconds).to.be.greaterThan(0)
      expect(seconds).to.not.be.NaN
    })
  })

  describe('Bug 5: restartGame should reset currentQuestionId to 0', () => {
    it('should set currentQuestionId to 0 after restartGame()', () => {
    window.currentQuestionId = 4
    restartGame()
    expect(window.currentQuestionId).to.equal(0)

    })
  })

  describe('Bug 6: Timer should count at correct speed', () => {
    it('should not decrement too fast (simulate 2s real time)', () => {
      const clock = sinon.useFakeTimers()
      const timeElement = document.getElementById('time')
      timeElement.textContent = ''
    
      window.resetAndStartTimer() // Start the real timer

      clock.tick(2000) // Simulate 2 seconds passing
      const displayed = parseFloat(timeElement.textContent)

      expect(displayed).to.be.within(8, 10) // Should not drop below 8 after 2s
      clock.restore()
    })
  })
})
