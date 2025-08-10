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
  easy:   { range: [1, 50],   guesses: 10, multiplier: 1 },
  medium: { range: [1, 100],  guesses: 7,  multiplier: 2 },
  hard:   { range: [1, 200],  guesses: 5,  multiplier: 3 }
};

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve => rl.question(prompt, resolve));
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
  gameState.playerName = await askQuestion(colors.info('Enter your name: '));
  await selectDifficulty();
  gameState.startTime = Date.now();
}

async function selectDifficulty() {
  const choices = ['easy', 'medium', 'hard'];
  while (true) {
    const diff = await askQuestion(colors.info(`
Select difficulty:
  1) Easy   (1–50,  10 guesses)
  2) Medium (1–100, 7 guesses)
  3) Hard   (1–200, 5 guesses)
Enter choice (1–3): `));

    if (['1', '2', '3'].includes(diff)) {
      gameState.difficulty = choices[parseInt(diff, 10) - 1];
      const settings = difficultySettings[gameState.difficulty];
      const [min, max] = settings.range;
      gameState.secretNumber = Math.floor(Math.random() * (max - min + 1)) + min;
      gameState.remainingGuesses = settings.guesses;
      return;
    }
    console.log(colors.error('Invalid choice! Please enter 1, 2, or 3.'));
  }
}

function calculateScore() {
  const elapsed = Math.floor((Date.now() - gameState.startTime) / 1000);
  const timeBonus = Math.max(0, 300 - elapsed);
  const guessPenalty = gameState.totalGuesses * 50;
  const multiplier = difficultySettings[gameState.difficulty].multiplier;
  return (gameState.score = (timeBonus - guessPenalty) * multiplier);
}

async function getHint() {
  const secret = gameState.secretNumber;
  const settings = difficultySettings[gameState.difficulty];
  const hints = [
    `The number is ${secret % 2 === 0 ? 'even' : 'odd'}.`,
    `The number is between ${Math.max(settings.range[0], secret - 10)} and ${Math.min(settings.range[1], secret + 10)}.`,
    `The sum of its digits is ${String(secret).split('').reduce((a,b) => a + parseInt(b,10), 0)}.`
  ];
  const idx = gameState.totalGuesses % hints.length;
  console.log(colors.warning(`\n💡 Hint: ${hints[idx]}\n`));
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {
    try {
      const prompt = colors.highlight(
        `\nRound ${gameState.currentRound} — ${gameState.remainingGuesses} guesses left\n` +
        `Enter your guess (${difficultySettings[gameState.difficulty].range[0]}–${difficultySettings[gameState.difficulty].range[1]}): `
      );
      const guess = await askQuestion(prompt);
      const number = parseInt(guess, 10);

      const [min, max] = difficultySettings[gameState.difficulty].range;
      if (isNaN(number) || number < min || number > max) {
        console.log(colors.error(`Please enter a number between ${min} and ${max}!`));
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (number === gameState.secretNumber) {
        const finalScore = calculateScore();
        console.log(colors.success(`
🎉 Correct! Final score: ${finalScore}
⏱ Time taken: ${Math.floor((Date.now() - gameState.startTime)/1000)} seconds
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
  gameState.highScores.forEach((entry, i) => {
    console.log(`${i + 1}. ${entry.name} — ${entry.score} (${entry.difficulty})`);
  });
}

async function main() {
  try {
    await initializeGame();
    const win = await playRound();
    displayHighScores();

    if (win) {
      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.toLowerCase() === 'y') {
        gameState.currentRound++;
        gameState.totalGuesses = 0;
        gameState.startTime = Date.now();
        const settings = difficultySettings[gameState.difficulty];
        const [min, max] = settings.range;
        gameState.secretNumber = Math.floor(Math.random() * (max - min + 1)) + min;
        gameState.remainingGuesses = settings.guesses;
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
