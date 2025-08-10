##  How to Set Up and Run the Tests

Follow these steps to install dependencies and run the unit tests for the **number-guessing-game** project.

---

### 1. Initialize the Project (if not already done)

```bash
npm init -y
```

This will create a basic `package.json` file.

---

### 2. Install Test Dependencies

Install Mocha (test runner) and Chai (assertion library):

```bash
npm install --save-dev mocha chai
```

---

### 3. Configure `package.json` to Run Tests

Open your `package.json` file and make sure it includes:

```json
"type": "module",
"scripts": {
  "test": "mocha"
}
```

This tells Node.js to treat `.mjs` files as ES modules and lets you run tests using:

```bash
npm test
```

---

### 4. Project Folder Structure

Make sure your project looks like this:

```
version[]/
├── number-guessing-game.mjs           # Main code
├── package.json
└── test/
    └── number-guessing-game.test.mjs  # Unit test file
```

---

### 5. Export Functions from Your Code File

In `number-guessing-game.mjs`, make sure the following is added at the bottom:

```js
export {
  gameState,
  difficultySettings,
  askQuestion,
  selectDifficulty,
  playRound,
  updateHighScores,
  main
};

if (import.meta.url === process.argv[1]) {
  main();
}
```

This ensures tests can import your functions, and `main()` only runs when the script is executed directly.

---

### 6. Run the Tests

Now run the tests using:

```bash
npm test
```

You should see test output like this:

```
  Bug Detection in number-guessing-game.mjs
    ✔ difficultySettings should use numeric ranges
    ✔ askQuestion should use correct variable and logic
    ✔ selectDifficulty should validate 1, 2, or 3
    ✔ playRound should loop while remainingGuesses > 0 and log properly
    ✔ updateHighScores should assign name and score correctly
    ✔ main should only replay if user says "y"

  6 passing (Xms)
```

---
