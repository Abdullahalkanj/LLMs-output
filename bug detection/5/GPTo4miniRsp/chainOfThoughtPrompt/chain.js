// ChatGPT4omini output using Chain-of-thought prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Here’s a walkthrough of what’s going wrong, step by step, and then a consolidated set of fixes you can apply to get the game running as intended.

// 1. The askQuestion helper is broken

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve => rl.question(pr, resolve));
    if (input.trim() !== '') {
      return input.trim(), console.log(colors.error('Error: Cannot enter an empty answer.'));
  }
}
// Wrong variable name: You pass pr to rl.question instead of the prompt argument.

// Logic inverted: You want to reject empty input, but the if checks for non-empty.

// Comma operator misuse:


return input.trim(), console.log(…)
// returns the result of console.log (i.e. undefined) instead of the trimmed string.

// Missing braces: You never close the if, the while, or the function properly.

// 2. difficultySettings has typo’d ranges

const difficultySettings = {
  easy:   { range: [/1, 50], guesses: 10, multiplier: 1 },
  medium: { range: [1, 100], guesses: 7,  multiplier: 2 },
  hard:   { range: [-1, 200], guesses: 5,  multiplier: 3 }
};
// The easy range is written as [/1, 50] (a regex!) instead of [1, 50].

// The hard range starts at -1; you almost certainly meant [1, 200].

// 3. selectDifficulty checks the wrong inputs

const diff = await askQuestion(…);
if (['1', '2', '6'].includes(diff)) { … }
// You prompt “Enter choice (4-7):” even though your valid options are 1–3.

// You check for ‘6’ instead of ‘3’, so users can never pick “hard.”

// 4. The main guessing loop never runs

async function playRound() {
  while (gameState.remainingGuesses < 0) {
    …
  }
  // drop out immediately to “game over”
}
// Since remainingGuesses starts positive, the condition < 0 is false, so you never enter the loop!

// 5. Miscellaneous typos & swapped fields
// Game over display:


console(colors.error(`…`));
// should be console.log(...).

// High-score entry:


gameState.highScores.push({
  name: gameState.score,
  score: gameState.name,
  …
});
// You’ve swapped name and score – and gameState.name doesn’t even exist.

// “Play again?” logic:


if (again.toLowerCase() ) { … }
// toLowerCase() returns a non-empty string, so this branch always runs. You need to compare it to e.g. 'y'.

// Score calculation assignment:


return (gameState.score = guessPenalty + timeBonus) * difficultyMultiplier;
// That sets gameState.score to the pre-multiplier value, then returns the multiplied result – your stored score and your returned score diverge.

// Suggested Fixes
// Below is a sketch of the corrected functions. You can integrate these into your file in place of the broken ones.

// <details> <summary><strong>1. A fixed <code>askQuestion</code></strong></summary>

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve =>
      rl.question(prompt, resolve)
    );
    if (input.trim() === '') {
      console.log(colors.error('Error: Cannot enter an empty answer.'));
      continue;
    }
    return input.trim();
  }
}
// Uses the correct prompt variable.

// Rejects empty lines, logs an error, and loops.

// Returns the trimmed answer directly.

// </details> <details> <summary><strong>2. Corrected <code>difficultySettings</code></strong></summary>

const difficultySettings = {
  easy:   { range: [1, 50],   guesses: 10, multiplier: 1 },
  medium: { range: [1, 100],  guesses: 7,  multiplier: 2 },
  hard:   { range: [1, 200],  guesses: 5,  multiplier: 3 }
};
// </details> <details> <summary><strong>3. Fixed <code>selectDifficulty</code></strong></summary>

async function selectDifficulty() {
  const map = { '1': 'easy', '2': 'medium', '3': 'hard' };
  while (true) {
    const diff = await askQuestion(colors.info(`
Select difficulty:
1) Easy   (1–50, 10 guesses)
2) Medium (1–100, 7 guesses)
3) Hard   (1–200, 5 guesses)
Enter choice (1-3): `));

    if (map[diff]) {
      gameState.difficulty = map[diff];
      const { range, guesses } = difficultySettings[gameState.difficulty];
      gameState.secretNumber = Math.floor(
        Math.random() * (range[1] - range[0] + 1)
      ) + range[0];
      gameState.remainingGuesses = guesses;
      return;
    }

    console.log(colors.error('Invalid choice! Please enter 1, 2, or 3.'));
  }
}
// </details> <details> <summary><strong>4. Fixed <code>playRound</code> loop & console</strong></summary>

async function playRound() {
  while (gameState.remainingGuesses > 0) {
    try {
      const guessStr = await askQuestion(colors.highlight(`
Round ${gameState.currentRound + 1} — ${gameState.remainingGuesses} guesses left
Enter your guess (${difficultySettings[gameState.difficulty].range.join('-')}): `));

      const guess = parseInt(guessStr, 10);
      const [low, high] = difficultySettings[gameState.difficulty].range;
      if (isNaN(guess) || guess < low || guess > high) {
        console.log(colors.error(`Please enter a number between ${low} and ${high}!`));
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (guess === gameState.secretNumber) {
        gameState.score = calculateScore();
        console.log(colors.success(`
🎉 Correct! Final score: ${gameState.score}
Time taken: ${Math.floor((Date.now() - gameState.startTime) / 1000)} seconds
        `));
        updateHighScores();
        return true;
      }

      console.log(colors.warning(`Too ${guess < gameState.secretNumber ? 'low' : 'high'}!`));
      if (gameState.totalGuesses % 2 === 0) {
        await getHint();
      }
    } catch (err) {
      console.log(colors.error('An unexpected error occurred:', err.message));
    }
  }

  console.log(colors.error(`
😞 Game Over! The number was ${gameState.secretNumber}.
Your final score: ${calculateScore()}
  `));
  return false;
}
// </details> <details> <summary><strong>5. Correct the score calc and high‐score update</strong></summary>


function calculateScore() {
  const timeBonus = Math.max(
    0,
    300 - Math.floor((Date.now() - gameState.startTime) / 1000)
  );
  const guessPenalty = gameState.totalGuesses * 50;
  const multiplier = difficultySettings[gameState.difficulty].multiplier;
  const raw = guessPenalty + timeBonus;
  gameState.score = raw * multiplier;
  return gameState.score;
}

function updateHighScores() {
  gameState.highScores.push({
    name:      gameState.playerName,
    score:     gameState.score,
    difficulty: gameState.difficulty
  });
  gameState.highScores.sort((a,b) => b.score - a.score);
  gameState.highScores.splice(5); // keep top 5
}
// </details> <details> <summary><strong>6. Fix the “play again” check</strong></summary>

const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
if (again.trim().toLowerCase() === 'y') {
  gameState.currentRound++;
  // …reset secretNumber and remainingGuesses…
  await playRound();
}
// </details>
// With those changes in place:

// Empty inputs get properly rejected.

// Difficulty is selected from the correct 1–3 menu.

// The guess loop actually runs while you have guesses left.

// Scores store and sort correctly.

// The “play again” prompt only continues on a “y.”

// That should resolve the errors and get your number-guessing game working smoothly!
const readline = require('readline');
const chalk = require('chalk');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

let gameState = {
  score: 1000,
  highScores: [],
  currentRound: 0,
  totalGuesses: 0,
  timeLimit: null,
  difficulty: 'medium',
  playerName: '',
  secretNumber: 0,
  remainingGuesses: 0,
  startTime: null
};

const colors = {
  error:   chalk.redBright,
  success: chalk.green,
  warning: chalk.yellow,
  info:    chalk.cyan,
  highlight: chalk.magenta.bold
};

const difficultySettings = {
  easy:   { range: [1, 50],  guesses: 10, multiplier: 1 },
  medium: { range: [1, 100], guesses: 7,  multiplier: 2 },
  hard:   { range: [1, 200], guesses: 5,  multiplier: 3 }
};

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve =>
      rl.question(prompt, resolve)
    );
    if (input.trim() === '') {
      console.log(colors.error('Error: Cannot enter an empty answer.'));
      continue;
    }
    return input.trim();
  }
}

async function initializeGame() {
  console.log(colors.info(`
  ██████╗ ██╗   ██╗███████╗███████╗███████╗
  ██╔══██╗██║   ██║██╔════╝██╔════╝██╔════╝
  ██████╔╝██║   ██║█████╗  ███████╗███████╗
  ██╔══██╗██║   ██║██╔══╝  ╚════██║╚════██║
  ██║  ██║╚██████╔╝███████╗███████║███████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚══════╝╚══════╝
  `));

  gameState.playerName = await askQuestion(colors.info("Enter your name: "));
  await selectDifficulty();
  gameState.startTime = Date.now();
}

async function selectDifficulty() {
  const map = { '1': 'easy', '2': 'medium', '3': 'hard' };
  while (true) {
    const diff = await askQuestion(colors.info(`
Select difficulty:
1) Easy   (1–50,  10 guesses)
2) Medium (1–100, 7 guesses)
3) Hard   (1–200, 5 guesses)
Enter choice (1-3): `));

    if (map[diff]) {
      gameState.difficulty = map[diff];
      const { range, guesses } = difficultySettings[gameState.difficulty];
      gameState.secretNumber = Math.floor(
        Math.random() * (range[1] - range[0] + 1)
      ) + range[0];
      gameState.remainingGuesses = guesses;
      return;
    }

    console.log(colors.error('Invalid choice! Please enter 1, 2, or 3.'));
  }
}

function calculateScore() {
  const timeBonus = Math.max(
    0,
    300 - Math.floor((Date.now() - gameState.startTime) / 1000)
  );
  const guessPenalty = gameState.totalGuesses * 50;
  const multiplier = difficultySettings[gameState.difficulty].multiplier;
  const rawScore = guessPenalty + timeBonus;
  gameState.score = rawScore * multiplier;
  return gameState.score;
}

async function getHint() {
  const maxRange = difficultySettings[gameState.difficulty].range[1];
  const hints = [
    `The number is ${gameState.secretNumber % 2 === 0 ? 'even' : 'odd'}.`,
    `The number is between ${Math.max(1, gameState.secretNumber - 10)} and ${Math.min(maxRange, gameState.secretNumber + 10)}.`,
    `The sum of its digits is ${String(gameState.secretNumber)
      .split('')
      .reduce((a, b) => a + parseInt(b, 10), 0)}.`
  ];
  console.log(colors.warning(`\n💡 Hint: ${hints[gameState.totalGuesses % hints.length]}\n`));
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {
    try {
      const [low, high] = difficultySettings[gameState.difficulty].range;
      const guessStr = await askQuestion(colors.highlight(`
Round ${gameState.currentRound + 1} — ${gameState.remainingGuesses} guesses left
Enter your guess (${low}-${high}): `));

      const guess = parseInt(guessStr, 10);
      if (isNaN(guess) || guess < low || guess > high) {
        console.log(colors.error(`Please enter a number between ${low} and ${high}!`));
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (guess === gameState.secretNumber) {
        gameState.score = calculateScore();
        console.log(colors.success(`
🎉 Correct! Final score: ${gameState.score}
Time taken: ${Math.floor((Date.now() - gameState.startTime) / 1000)} seconds
        `));
        updateHighScores();
        return true;
      }

      console.log(colors.warning(`Too ${guess < gameState.secretNumber ? 'low' : 'high'}!`));
      if (gameState.totalGuesses % 2 === 0) {
        await getHint();
      }
    } catch (err) {
      console.log(colors.error('An unexpected error occurred:', err.message));
    }
  }

  console.log(colors.error(`
😞 Game Over! The number was ${gameState.secretNumber}.
Your final score: ${calculateScore()}
  `));
  return false;
}

function updateHighScores() {
  gameState.highScores.push({
    name:       gameState.playerName,
    score:      gameState.score,
    difficulty: gameState.difficulty
  });
  gameState.highScores.sort((a, b) => b.score - a.score);
  gameState.highScores.splice(5); // Keep top 5 only
}

function displayHighScores() {
  console.log(colors.info('\n🏆 High Scores:'));
  gameState.highScores.forEach((entry, index) => {
    console.log(`${index + 1}. ${entry.name} — ${entry.score} (${entry.difficulty})`);
  });
}

async function main() {
  try {
    await initializeGame();
    const win = await playRound();
    displayHighScores();

    if (win) {
      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.trim().toLowerCase() === 'y') {
        gameState.currentRound++;
        // reset only the round-specific state:
        const { range, guesses } = difficultySettings[gameState.difficulty];
        gameState.secretNumber = Math.floor(
          Math.random() * (range[1] - range[0] + 1)
        ) + range[0];
        gameState.remainingGuesses = guesses;
        await playRound();
        displayHighScores();
      }
    }
  } catch (error) {
    console.log(colors.error('Fatal error:', error.message));
  } finally {
    rl.close();
  }
}

main();
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
