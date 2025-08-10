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
  error:   chalk.redBright,
  success: chalk.green,
  warning: chalk.yellow,
  info:    chalk.cyan,
  highlight: chalk.magenta.bold
};

const difficultySettings = {
  easy:   { range: [  1,  50], guesses: 10, multiplier: 1 }, // fixed regex → number
  medium: { range: [  1, 100], guesses:  7, multiplier: 2 },
  hard:   { range: [  1, 200], guesses:  5, multiplier: 3 }  // fixed -1 → 1
};

async function askQuestion(prompt) { // fixed param name
  while (true) {
    const input = await new Promise(resolve =>
      rl.question(prompt, resolve)      // ← use prompt, not pr
    );
    if (input.trim() === '') {
      console.log(colors.error('Error: Cannot enter an empty answer.'));
      continue;
    }
    return input.trim();               // ← return only the trimmed input
  }
}

async function initializeGame() {
  console.log(colors.info(`
  … ASCII BANNER …
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
Enter choice (1-3): `
    ));

    if (['1', '2', '3'].includes(diff)) { // ← check 1–3
      const choices = ['easy','medium','hard'];
      gameState.difficulty = choices[+diff - 1];
      const s = difficultySettings[gameState.difficulty];
      gameState.secretNumber = Math.floor(
        Math.random() * (s.range[1] - s.range[0] + 1)
      ) + s.range[0];
      gameState.remainingGuesses = s.guesses;
      return;
    }
    console.log(colors.error('Invalid choice! Please enter 1, 2, or 3.'));
  }
}

function calculateScore() {
  // (Optional) use a clear formula instead of the original penalty-only approach
  const timeBonus = Math.max(0, 300 - Math.floor((Date.now() - gameState.startTime)/1000));
  const guessPenalty = gameState.totalGuesses * 50;
  const base = 1000 - guessPenalty + timeBonus;
  const m = difficultySettings[gameState.difficulty].multiplier;
  gameState.score = Math.max(0, base) * m;
  return gameState.score;
}

async function getHint() {
  const s = difficultySettings[gameState.difficulty];
  const hints = [
    `The number is ${gameState.secretNumber % 2 === 0 ? 'even' : 'odd'}`,
    `The number is between ${Math.max(s.range[0], gameState.secretNumber - 10)} and ${Math.min(s.range[1], gameState.secretNumber + 10)}`,
    `The sum of its digits is ${String(gameState.secretNumber).split('').reduce((a,b) => a + +b, 0)}`
  ];
  console.log(colors.warning(`💡 Hint: ${hints[gameState.totalGuesses % 3]}`));
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {  // ← was < 0
    try {
      const guess = await askQuestion(colors.highlight(`
Round ${gameState.currentRound} - ${gameState.remainingGuesses} guesses left
Enter your guess (${difficultySettings[gameState.difficulty].range[0]}-${difficultySettings[gameState.difficulty].range[1]}): `
      ));
      const number = parseInt(guess, 10);
      const s = difficultySettings[gameState.difficulty];
      if (isNaN(number) || number < s.range[0] || number > s.range[1]) {
        console.log(colors.error(`Please enter a number between ${s.range[0]} and ${s.range[1]}!`));
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (number === gameState.secretNumber) {
        console.log(colors.success(`
🎉 Correct! Final score: ${calculateScore()}
Time taken: ${Math.floor((Date.now() - gameState.startTime)/1000)} seconds
`));
        updateHighScores();
        return true;
      }

      console.log(colors.warning(`Too ${number < gameState.secretNumber ? 'low' : 'high'}!`));

      if (gameState.totalGuesses % 2 === 0) {
        await getHint();
      }
    } catch (err) {
      console.log(colors.error('An unexpected error occurred:', err.message));
    }
  }

  console.log(colors.error(`
😞 Game Over! The number was ${gameState.secretNumber}
Your final score: ${calculateScore()}
`));                      // ← fixed console.log
  return false;
}

function updateHighScores() {
  gameState.highScores.push({
    name:       gameState.playerName,  // ← fixed swap
    score:      gameState.score,
    difficulty: gameState.difficulty
  });
  gameState.highScores.sort((a, b) => b.score - a.score);
  gameState.highScores = gameState.highScores.slice(0, 5);
}

function displayHighScores() {
  console.log(colors.info('\n🏆 High Scores:'));
  gameState.highScores.forEach((e, i) =>
    console.log(`${i+1}. ${e.name} - ${e.score} (${e.difficulty})`)
  );
}

async function main() {
  try {
    await initializeGame();
    const win = await playRound();
    displayHighScores();

    if (win) {
      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.trim().toLowerCase().startsWith('y')) {  // ← correct check
        gameState.currentRound++;
        const s = difficultySettings[gameState.difficulty];
        gameState.secretNumber = Math.floor(
          Math.random() * (s.range[1] - s.range[0] + 1)
        ) + s.range[0];
        gameState.remainingGuesses = s.guesses;
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
