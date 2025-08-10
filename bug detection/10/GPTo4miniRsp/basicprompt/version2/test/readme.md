
#  Quiz Game Test Runner – Instructions

This test suite checks your `script.js` file for common bugs. Please follow these steps **exactly** before running the tests.

---

## Step-by-Step: How to Run the Tests

### 1. Initialize Your Project

If this is your first time running Node.js in this folder, start by creating a `package.json` file:

```bash
npm init -y


---

### 2. Install Required Dependencies

Install the following packages using `npm`:

```bash
npm install mocha chai jsdom sinon node-fetch
```

---

### 3. Prepare `script.js` for Testing

Before running the tests, you **must** comment out or modify certain parts of your script.

---

##  A. Comment Out All DOM-Dependent Code

The tests run in a fake browser environment (JSDOM). These lines **must be commented out** to prevent errors:

###  Comment the following (example):

```js
// document.getElementById('start-btn').addEventListener('click', startGame)
// document.getElementById('restartGameButton').addEventListener('click', restartGame)

// const highScoreButton = document.getElementById('highScoreLink')
// highScoreButton.addEventListener('click', showHighScores)

// const usernameInput = document.getElementById('username')
// usernameInput.addEventListener('keypress', function (event) {
//   if (event.key === 'Enter') {
//     event.preventDefault()
//     startGame()
//   }
// })

// document.getElementById('quizTitle').textContent = ...
// document.getElementById('Quiz').style.display = ...
// document.getElementById('resultContainer').style.display = ...
// document.getElementById('timer').style.display = ...
// document.getElementById('restartGameButton').style.display = ...
```

These are only needed in the real UI — not for automated testing.

---

## B. Expose Your Game Variables

Declare and assign the following at the top of your `DOMContentLoaded` block or script:

```js
window.currentQuestionId = 0
window.playerName = ''
window.startTime = 0
window.endTime = 0
```

---

## C. Expose Your Functions to `window`

At the **bottom of `script.js`**, add:

```js
if (typeof window !== 'undefined') {
  window.getPlayerName = getPlayerName
  window.submitAnswer = submitAnswer
  window.restartGame = restartGame
  window.showResult = showResult
  window.resetAndStartTimer = resetAndStartTimer
}
```

---

### 4. Run the Tests

Now run this command:

```bash
npx mocha test/script.test.mjs
```

You should see output showing whether each of the 6 bugs has been fixed.

---
