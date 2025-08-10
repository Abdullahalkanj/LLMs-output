import readline from 'readline';
import chalk from 'chalk';

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
  easy:   { range: [1,   50],  guesses: 10, multiplier: 1 },  // ✅ fixed [1,50]
  medium: { range: [1,  100],  guesses: 7,  multiplier: 2 },
  hard:   { range: [1,  200],  guesses: 5,  multiplier: 3 }   // ✅ range now starts at 1
};

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve =>
      rl.question(prompt, resolve)           // ✅ use `prompt`, not `pr`
    );

    if (input.trim() === '') {
      console.log(colors.error('Error: Cannot enter an empty answer.'));
      continue;                              // ✅ retry on empty
    }

    return input.trim();                     // ✅ return valid input
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
    Enter choice (1-3): `));               // ✅ corrected label

    if (['1', '2', '3'].includes(diff)) {  // ✅ accept 1,2,3
      const choices = ['easy', 'medium', 'hard'];
      gameState.difficulty = choices[parseInt(diff, 10) - 1];
      const { range, guesses } = difficultySettings[gameState.difficulty];
      gameState.secretNumber =
        Math.floor(Math.random() * (range[1] - range[0] + 1)) + range[0];
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
  const baseScore = timeBonus - guessPenalty;      // ✅ penalties subtract
  gameState.score = Math.max(0, baseScore) * multiplier;
  return gameState.score;
}

async function getHint() {
  const { secretNumber, totalGuesses, difficulty } = gameState;
  const rangeMax = difficultySettings[difficulty].range[1];
  const hints = [
    `The number is ${secretNumber % 2 === 0 ? 'even' : 'odd'}.`,
    `The number is between ${Math.max(1, secretNumber - 10)} and ${Math.min(rangeMax, secretNumber + 10)}.`,
    `The sum of its digits is ${
      String(secretNumber)
        .split('')
        .reduce((a, b) => a + parseInt(b, 10), 0)
    }.`
  ];
  console.log(colors.warning(`\n💡 Hint: ${hints[totalGuesses % hints.length]}\n`));
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {    // ✅ loop while guesses remain
    try {
      const guess = await askQuestion(colors.highlight(`
      Round ${gameState.currentRound} - ${gameState.remainingGuesses} guesses left
      Enter your guess (${difficultySettings[gameState.difficulty].range.join('-')}): `));

      const number = parseInt(guess, 10);
      const [min, max] = difficultySettings[gameState.difficulty].range;
      if (isNaN(number) || number < min || number > max) {
        console.log(colors.error(`Please enter a number between ${min} and ${max}!`));
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (number === gameState.secretNumber) {
        console.log(colors.success(`
        🎉 Correct! Final score: ${calculateScore()}
        Time taken: ${Math.floor((Date.now() - gameState.startTime) / 1000)} seconds
        `));
        updateHighScores();
        return true;
      }

      console.log(colors.warning(`Too ${number < gameState.secretNumber ? 'low' : 'high'}!`));
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
  `));                                        // ✅ use console.log
  return false;
}

function updateHighScores() {
  gameState.highScores.push({
    name: gameState.playerName,               // ✅ correct mapping
    score: gameState.score,
    difficulty: gameState.difficulty
  });
  gameState.highScores.sort((a, b) => b.score - a.score);
  gameState.highScores = gameState.highScores.slice(0, 5);
}

function displayHighScores() {
  console.log(colors.info('\n🏆 High Scores:'));
  gameState.highScores.forEach((entry, i) => {
    console.log(`${i + 1}. ${entry.name} - ${entry.score} (${entry.difficulty})`);
  });
}

async function main() {
  try {
    await initializeGame();
    const win = await playRound();
    displayHighScores();

    if (win) {
      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.trim().toLowerCase() === 'y') {  // ✅ explicit check
        gameState.currentRound++;
        const { range, guesses } = difficultySettings[gameState.difficulty];
        gameState.secretNumber =
          Math.floor(Math.random() * (range[1] - range[0] + 1)) + range[0];
        gameState.remainingGuesses = guesses;
        gameState.totalGuesses = 0;            // ✅ reset counters for new round
        gameState.startTime = Date.now();
        await playRound();
        displayHighScores();
      }
    }
  } catch (error) {
    console.log(colors.error('Fatal error:', error));
  } finally {
    rl.close();
  }
}

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
