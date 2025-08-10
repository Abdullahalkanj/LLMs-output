import readline from 'readline';
import chalk from 'chalk';

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

const gameState = {
  score: 0,
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
  easy:   { range: [1,  50],  guesses: 10, multiplier: 1 },
  medium: { range: [1, 100],  guesses:  7, multiplier: 2 },
  hard:   { range: [1, 200],  guesses:  5, multiplier: 3 }
};

async function askQuestion(prompt) {
  // Loop until non-empty input
  while (true) {
    const input = await new Promise(resolve => rl.question(prompt, resolve));
    if (input.trim() === '') {
      console.log(colors.error('Error: Cannot enter an empty answer.'));
    } else {
      return input.trim();
    }
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
      const levels = ['easy','medium','hard'];
      gameState.difficulty = levels[+diff - 1];
      const { range, guesses } = difficultySettings[gameState.difficulty];
      gameState.remainingGuesses = guesses;
      // pick secret number in [min,max]
      gameState.secretNumber = Math.floor(
        Math.random() * (range[1] - range[0] + 1)
      ) + range[0];
      return;
    }
    console.log(colors.error('Invalid choice! Please enter 1, 2, or 3.'));
  }
}

function calculateScore() {
  const elapsed = Math.floor((Date.now() - gameState.startTime) / 1000);
  const timeBonus = Math.max(0, 300 - elapsed);
  const guessPenalty = gameState.totalGuesses * 50;
  const mult = difficultySettings[gameState.difficulty].multiplier;
  const finalScore = (timeBonus - guessPenalty) * mult;
  gameState.score = Math.max(0, finalScore);
  return gameState.score;
}

async function getHint() {
  const secret = gameState.secretNumber;
  const maxR = difficultySettings[gameState.difficulty].range[1];
  const hints = [
    `The number is ${secret % 2 === 0 ? 'even' : 'odd'}.`,
    `It lies between ${Math.max(1, secret - 10)} and ${Math.min(maxR, secret + 10)}.`,
    `The sum of its digits is ${String(secret).split('').reduce((a,b) => a + +b, 0)}.`
  ];
  console.log(colors.warning(`\n💡 Hint: ${hints[gameState.totalGuesses % hints.length]}\n`));
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {
    const prompt = colors.highlight(`
Round ${gameState.currentRound} — ${gameState.remainingGuesses} guesses left
Enter your guess (${difficultySettings[gameState.difficulty].range.join('-')}): `);
    try {
      const guess = await askQuestion(prompt);
      const num = parseInt(guess, 10);
      const [min, max] = difficultySettings[gameState.difficulty].range;
      if (isNaN(num) || num < min || num > max) {
        console.log(colors.error(`Enter a number between ${min} and ${max}!`));
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (num === gameState.secretNumber) {
        const score = calculateScore();
        console.log(colors.success(`
🎉 Correct! Final score: ${score}
Time taken: ${Math.floor((Date.now() - gameState.startTime)/1000)}s
        `));
        updateHighScores();
        return true;
      }

      console.log(colors.warning(`Too ${num < gameState.secretNumber ? 'low' : 'high'}!`));
      if (gameState.totalGuesses % 2 === 0) await getHint();

    } catch (err) {
      console.log(colors.error('An unexpected error occurred:', err.message));
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
  gameState.highScores.forEach((e,i) =>
    console.log(`${i+1}. ${e.name} — ${e.score} (${e.difficulty})`)
  );
}

async function main() {
  try {
    await initializeGame();
    const won = await playRound();
    displayHighScores();

    if (won) {
      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.toLowerCase() === 'y') {
        gameState.currentRound++;
        // reset for next round
        gameState.totalGuesses = 0;
        await playRound();
      }
    }
  } catch (err) {
    console.log(colors.error('Fatal error:', err));
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
