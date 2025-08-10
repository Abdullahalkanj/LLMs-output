### **Bug List**

---

**B1 – `getPlayerName()` method**
**Description:** Missing validation for empty username; allows the game to start without a name
**Buggy Code Line / Issue:**

```js
if (empty) { alert() } // `empty` is undefined; alert is called with no message; no return value to stop game
```

**Detected by Test(s):**

* `should alert or return false if username is empty`
* `should return true if username is not empty`

---

**B2 – `submitAnswer()` method**
**Description:** Selected answer is never properly stored; syntax error in assignment and misuse of undefined variable
**Buggy Code Line / Issue:**

```js
if (radioButton.checked) selectedAnsweranswer = radioButton.value // Invalid variable name  
...  
else if (Answer !== null) { ... } // Incorrect use of undefined `Answer`
```

**Detected by Test(s):**

* `should correctly select answer from checked radio button`

---

**B3 – `handleAnswerResponse()` → `showResult()`**
**Description:** High score can be saved even when username is empty; function called with undefined `empty`
**Buggy Code Line / Issue:**

```js
showResult(empty) // `empty` is not defined  
// No guard against empty `playerName` inside `showResult`
```

**Detected by Test(s):**

* `should not call saveHighScore if playerName is empty`

---

**B4 – `showResult()` method**
**Description:** Time is calculated using incorrect formula (`startTime / endTime`); results in NaN or invalid numbers
**Buggy Code Line / Issue:**

```js
const timeSpent = startTime / endTime // Should be `endTime - startTime`
```

**Detected by Test(s):**

* `should not produce NaN or non-positive values for time taken`

---

**B5 – `restartGame()` method**
**Description:** Game restarts at incorrect question ID (4 instead of 0)
**Buggy Code Line / Issue:**

```js
currentQuestionId = 4 // Should be 0 on game restart
```

**Detected by Test(s):**

* `should set currentQuestionId to 0 after restartGame()`

---

**B6 – `startTimer()` function**
**Description:** Timer runs too fast due to two separate `timeLeft` decrements per tick; also interval speed is too short
**Buggy Code Line / Issue:**

```js
timeLeft = (timeLeft - 0.1).toFixed(1)  
...
timeLeft -= 0.1 // Double decrement every 195ms
```

**Detected by Test(s):**

* `should not decrement too fast (simulate 2s real time)`

---