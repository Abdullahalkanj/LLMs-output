##  ** List of bugs in  `number-guessing-game`**
---

**B1 – `difficultySettings` object**
**Description:** The `easy` range uses a RegExp instead of numbers; the `hard` range starts at -1 instead of 1
**Buggy Code Line / Issue:**

```js
easy: { range: [/1, 50], ... },  
hard: { range: [-1, 200], ... }
```

**Detected by Test(s):** `difficultySettings should use numeric ranges`

---

**B2 – `askQuestion()` method**
**Description:** Uses invalid variable `pr` instead of `prompt` and incorrectly uses `return input.trim(), console.log(...)`
**Buggy Code Line / Issue:**

```js
const input = await new Promise(resolve => rl.question(pr, resolve));  
return input.trim(), console.log(...);
```

**Detected by Test(s):** `askQuestion should use correct variable and logic`

---

**B3 – `selectDifficulty()` method**
**Description:** The prompt says "Enter choice (4-7)" which is incorrect
**Buggy Code Line / Issue:**

```js
Enter choice (4-7):
```

**Detected by Test(s):** `selectDifficulty should validate 1, 2, or 3`

---

**B4 – `selectDifficulty()` method**
**Description:** Valid choices include invalid option `'6'`
**Buggy Code Line / Issue:**

```js
if (['1', '2', '6'].includes(diff))
```

**Detected by Test(s):** `selectDifficulty should validate 1, 2, or 3`

---

**B5 – `playRound()` method**
**Description:** Loop condition is inverted; should loop while `remainingGuesses > 0`
**Buggy Code Line / Issue:**

```js
while (gameState.remainingGuesses < 0)
```

**Detected by Test(s):** `playRound should loop while remainingGuesses > 0 and log properly`

---

**B6 – `playRound()` method**
**Description:** Uses incorrect `console()` instead of `console.log()`
**Buggy Code Line / Issue:**

```js
console(colors.error(...))
```

**Detected by Test(s):** `playRound should loop while remainingGuesses > 0 and log properly`

---

**B7 – `updateHighScores()` method**
**Description:** Swaps `name` and `score` when pushing a new high score entry
**Buggy Code Line / Issue:**

```js
name: gameState.score,  
score: gameState.name,
```

**Detected by Test(s):** `updateHighScores should assign name and score correctly`

---

**B8 – `main()` method**
**Description:** Always replays the game regardless of user input due to missing condition check
**Buggy Code Line / Issue:**

```js
if (again.toLowerCase())
```

**Detected by Test(s):** `main should only replay if user says "y"`

---
