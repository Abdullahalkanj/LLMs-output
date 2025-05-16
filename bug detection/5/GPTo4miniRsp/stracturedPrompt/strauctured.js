// ChatGPT4omini output using Structured prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Thought for 39 seconds
// The root of the problem is that we never “zero out” our per-round state when the player chooses to play again. In particular:

// gameState.totalGuesses keeps accumulating across rounds, so both your “guess penalty” in calculateScore() and your getHint() index (gameState.totalGuesses % 3) are off once you start round 2, round 3, etc.

// gameState.startTime is only ever set once (in initializeGame()), so your “time bonus” is being computed from the very beginning of the very first game, not from the start of each new round.

// That means on round 2 your score is being unfairly penalized by the guesses you made back in round 1, and your hints come at the wrong times.

// How to fix
// Inside your main() loop, when the user says “yes” to “Play again?”, you already regenerate


gameState.currentRound++;
gameState.secretNumber = /* … */;
gameState.remainingGuesses = /* … */;
// you just need to also reset:


gameState.totalGuesses = 0;
gameState.startTime    = Date.now();
// (optionally) reset the “base” score for clarity:
gameState.score        = 1000;
// Here’s the revised snippet:


     const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
     if (again.toLowerCase() === 'y') {
-      gameState.currentRound++;
-      gameState.secretNumber = Math.floor(Math.random() * 
-        (difficultySettings[gameState.difficulty].range[1] - 
-        difficultySettings[gameState.difficulty].range[0] + 1)) + 
-        difficultySettings[gameState.difficulty].range[0];
-      gameState.remainingGuesses = difficultySettings[gameState.difficulty].guesses;
+      gameState.currentRound++;
+
+      // ✨ Reset per-round state so scoring & hints start fresh
+      gameState.totalGuesses = 0;
+      gameState.startTime    = Date.now();
+      gameState.score        = 1000;
+
+      // regenerate the secret number & guesses
+      const settings = difficultySettings[gameState.difficulty];
+      gameState.secretNumber   =
+        Math.floor(
+          Math.random() * (settings.range[1] - settings.range[0] + 1)
+        ) + settings.range[0];
+      gameState.remainingGuesses = settings.guesses;
     } else {
       playAgain = false;
       console.log(colors.info('Thanks for playing!'));
     }
// With that in place:

// totalGuesses will correctly reflect only the guesses the player makes in this one round, so your penalty (totalGuesses * 50) and your hint-every-2-guesses logic will be back in sync.

// startTime will be the moment the new round began, so your time bonus is measured per round.

// And (optionally) resetting score to 1000 makes it crystal-clear that each round starts at the same 1000-point baseline.

// That change alone will eliminate the “carry-over” penalty and hint misfires you’re seeing once you hit round 2.

t 

const readline = require('readline');
const chalk = require('chalk');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

let gameState = {
  score: 1000,
  highScores: [],
  currentRound: 1,
  totalGuesses: 0,
  timeLimit: null,
  difficulty: 'medium',
  playerName: '',
  secretNumber: 0,
  remainingGuesses: 0,
  startTime: null
};

const colors = {
  error: chalk.redBright,
  success: chalk.green,
  warning: chalk.yellow,
  info: chalk.cyan,
  highlight: chalk.magenta.bold
};

const difficultySettings = {
  easy: { range: [1, 50], guesses: 10, multiplier: 1 },
  medium: { range: [1, 100], guesses: 7, multiplier: 2 },
  hard: { range: [1, 200], guesses: 5, multiplier: 3 }
};

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve => rl.question(prompt, resolve));
    if (input.trim() !== '') {
      return input.trim();
    }
    console.log(colors.error('Error: Cannot enter an empty answer.'));
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
  while (true) {
    const diff = await askQuestion(colors.info(`
    Select difficulty:
    1) Easy (1-50, 10 guesses)
    2) Medium (1-100, 7 guesses)
    3) Hard (1-200, 5 guesses)
    Enter choice (1-3): `));

    if (['1', '2', '3'].includes(diff)) {
      const choices = ['easy', 'medium', 'hard'];
      gameState.difficulty = choices[parseInt(diff) - 1];
      const settings = difficultySettings[gameState.difficulty];
      gameState.secretNumber = Math.floor(
        Math.random() * (settings.range[1] - settings.range[0] + 1)
      ) + settings.range[0];
      gameState.remainingGuesses = settings.guesses;
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
  const difficultyMultiplier =
    difficultySettings[gameState.difficulty].multiplier;
  return Math.max(0, (1000 - guessPenalty + timeBonus) * difficultyMultiplier);
}

async function getHint() {
  const hints = [
    `The number is ${
      gameState.secretNumber % 2 === 0 ? 'even' : 'odd'
    }`,
    `The number is between ${
      Math.max(1, gameState.secretNumber - 10)
    } and ${
      Math.min(
        difficultySettings[gameState.difficulty].range[1],
        gameState.secretNumber + 10
      )
    }`,
    `The sum of its digits is ${String(gameState.secretNumber)
      .split('')
      .reduce((a, b) => a + parseInt(b), 0)}`
  ];
  console.log(
    colors.warning(`\n💡 Hint: ${hints[gameState.totalGuesses % 3]}\n`)
  );
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {
    try {
      const guess = await askQuestion(
        colors.highlight(`
      Round ${gameState.currentRound} - ${gameState.remainingGuesses} guesses left
      Enter your guess (${
        difficultySettings[gameState.difficulty].range[0]
      }-${
          difficultySettings[gameState.difficulty].range[1]
        }): `)
      );

      const number = parseInt(guess, 10);
      const [min, max] = difficultySettings[gameState.difficulty].range;
      if (isNaN(number) || number < min || number > max) {
        console.log(
          colors.error(
            `Please enter a number between ${min} and ${max}!`
          )
        );
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (number === gameState.secretNumber) {
        gameState.score = calculateScore();
        console.log(colors.success(`
        🎉 Correct! Final score: ${gameState.score}
        Time taken: ${Math.floor((Date.now() - gameState.startTime) / 1000)} seconds
        `));
        updateHighScores();
        return true;
      }

      console.log(
        colors.warning(
          `Too ${number < gameState.secretNumber ? 'low' : 'high'}!`
        )
      );

      if (gameState.totalGuesses % 2 === 0) {
        await getHint();
      }
    } catch (error) {
      console.log(colors.error('An unexpected error occurred:', error.message));
    }
  }

  console.log(colors.error(`
  😞 Game Over! The number was ${gameState.secretNumber}
  Your final score: ${calculateScore()}
  `));
  return false;
}

function updateHighScores() {
  gameState.highScores.push({
    name: gameState.playerName,
    score: gameState.score,
    difficulty: gameState.difficulty
  });
  gameState.highScores.sort((a, b) => b.score - a.score);
  gameState.highScores = gameState.highScores.slice(0, 5);
}

function displayHighScores() {
  console.log(colors.info('\n🏆 High Scores:'));
  if (gameState.highScores.length === 0) {
    console.log(colors.info('No high scores yet!'));
    return;
  }
  gameState.highScores.forEach((entry, index) => {
    console.log(
      `${index + 1}. ${entry.name} - ${entry.score} (${entry.difficulty})`
    );
  });
}

async function main() {
  try {
    await initializeGame();
    let playAgain = true;

    while (playAgain) {
      const win = await playRound();
      displayHighScores();

      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.toLowerCase() === 'y') {
        gameState.currentRound++;

        // Reset per-round state so scoring & hints start fresh
        gameState.totalGuesses = 0;
        gameState.startTime    = Date.now();
        gameState.score        = 1000;

        // Regenerate secret number and reset guesses
        const settings = difficultySettings[gameState.difficulty];
        gameState.secretNumber   = Math.floor(
          Math.random() * (settings.range[1] - settings.range[0] + 1)
        ) + settings.range[0];
        gameState.remainingGuesses = settings.guesses;
      } else {
        playAgain = false;
        console.log(colors.info('Thanks for playing!'));
      }
    }
  } catch (error) {
    console.log(colors.error('Fatal error:', error));
  } finally {
    rl.close();
  }
}

main();
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
