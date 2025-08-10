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
  easy: { range: [1, 50], guesses: 10, multiplier: 1 },   // Fix: removed stray slash before 1
  medium: { range: [1, 100], guesses: 7, multiplier: 2 },
  hard:   { range: [1, 200], guesses: 5, multiplier: 3 }   // Fix: changed -1 to 1 for lower bound
};

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve => rl.question(prompt, resolve));  // Fix: use the correct variable name `prompt`
    if (input.trim() === '') {
      console.log(colors.error('Error: Cannot enter an empty answer.'));       // Fix: on empty input, log error and continue loop
      continue;
    }
    return input.trim();                                                      // Fix: return the non-empty trimmed input
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
    Enter choice (1-3): `));                                           // Fix: corrected prompt range to 1-3

    if (['1', '2', '3'].includes(diff)) {                               // Fix: include '3' instead of '6'
      const choices = ['easy', 'medium', 'hard'];
      gameState.difficulty = choices[parseInt(diff, 10) - 1];
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
  const difficultyMultiplier = difficultySettings[gameState.difficulty].multiplier;
  // Fix: score should reward time and penalize guesses, then apply multiplier
  const baseScore = gameState.score + timeBonus - guessPenalty;
  return (gameState.score = Math.max(0, baseScore) * difficultyMultiplier);
}

async function getHint() {
  const maxRange = difficultySettings[gameState.difficulty].range[1];
  const hints = [
    `The number is ${gameState.secretNumber % 2 === 0 ? 'even' : 'odd'}`,
    `The number is between ${Math.max(
      1,
      gameState.secretNumber - 10
    )} and ${Math.min(maxRange, gameState.secretNumber + 10)}`,
    `The sum of its digits is ${String(gameState.secretNumber)
      .split('')
      .reduce((a, b) => a + parseInt(b, 10), 0)}`
  ];
  console.log(colors.warning(`\n💡 Hint: ${hints[gameState.totalGuesses % 3]}\n`));
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {                               // Fix: loop while guesses remain (was < 0)
    try {
      const guess = await askQuestion(colors.highlight(`
      Round ${gameState.currentRound} - ${gameState.remainingGuesses} guesses left
      Enter your guess (${difficultySettings[gameState.difficulty].range[0]}-${difficultySettings[gameState.difficulty].range[1]}): `));

      const number = parseInt(guess, 10);
      const [min, max] = difficultySettings[gameState.difficulty].range;
      if (isNaN(number) || number < min || number > max) {
        console.log(colors.error(`Please enter a number between ${min} and ${max}!`));
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

      console.log(colors.warning(`Too ${number < gameState.secretNumber ? 'low' : 'high'}!`));

      if (gameState.totalGuesses % 2 === 0) {
        await getHint();
      }
    } catch (error) {
      console.log(colors.error('An unexpected error occurred:', error.message));
    }
  }

  console.log(colors.error(`                                         // Fix: use console.log, not console()
  😞 Game Over! The number was ${gameState.secretNumber}
  Your final score: ${calculateScore()}
  `));
  return false;
}

function updateHighScores() {
  gameState.highScores.push({
    name: gameState.playerName,    // Fix: swapped to store playerName
    score: gameState.score,        // Fix: swapped to store score
    difficulty: gameState.difficulty
  });
  gameState.highScores.sort((a, b) => b.score - a.score);
  gameState.highScores = gameState.highScores.slice(0, 5);
}

function displayHighScores() {
  console.log(colors.info('\n🏆 High Scores:'));
  gameState.highScores.forEach((entry, index) => {
    console.log(`${index + 1}. ${entry.name} - ${entry.score} (${entry.difficulty})`);
  });
}

async function main() {
  try {
    await initializeGame();
    const win = await playRound();
    displayHighScores();

    if (win) {
      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.trim().toLowerCase() === 'y') {        // Fix: explicitly check for 'y'
        gameState.currentRound++;
        const settings = difficultySettings[gameState.difficulty];
        gameState.secretNumber = Math.floor(
          Math.random() * (settings.range[1] - settings.range[0] + 1)
        ) + settings.range[0];
        gameState.remainingGuesses = settings.guesses;
        await playRound();
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