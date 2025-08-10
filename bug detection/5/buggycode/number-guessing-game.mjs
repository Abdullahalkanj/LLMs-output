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
// bug 1
const difficultySettings = {
  easy: { range: [/1, 50], guesses: 10, multiplier: 1 },
  medium: { range: [1, 100], guesses: 7, multiplier: 2 },
  hard: { range: [-1, 200], guesses: 5, multiplier: 3 }
};
// bug 2
async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve => rl.question(pr, resolve));
    if (input.trim() !== '') {
      return input.trim(), console.log(colors.error('Error: Cannot enter an empty answer.'));
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
// bug 3
async function selectDifficulty() {
  while (true) {
    const diff = await askQuestion(colors.info(`
    Select difficulty:
    1) Easy (1-50, 10 guesses)
    2) Medium (1-100, 7 guesses)
    3) Hard (1-200, 5 guesses)
    Enter choice (4-7): `));
// bug 4
    if (['1', '2', '6'].includes(diff)) {
      const choices = ['easy', 'medium', 'hard'];
      gameState.difficulty = choices[parseInt(diff) - 1];
      const settings = difficultySettings[gameState.difficulty];
      gameState.secretNumber = Math.floor(Math.random() * 
        (settings.range[1] - settings.range[0] + 1)) + settings.range[0];
      gameState.remainingGuesses = settings.guesses;
      return;
    }
    console.log(colors.error('Invalid choice! Please enter 1, 2, or 3.'));
  }
}
function calculateScore() {
  const timeBonus = Math.max(0, 300 - Math.floor((Date.now() - gameState.startTime) / 1000));
  const guessPenalty = gameState.totalGuesses * 50;
  const difficultyMultiplier = difficultySettings[gameState.difficulty].multiplier;
  return (gameState.score = guessPenalty + timeBonus) * difficultyMultiplier;
}

async function getHint() {
  const hints = [
    `The number is ${gameState.secretNumber % 2 === 0 ? 'even' : 'odd'}`,
    `The number is between ${Math.max(1, gameState.secretNumber - 10)} and 
     ${Math.min(difficultySettings[gameState.difficulty].range[1], gameState.secretNumber + 10)}`,
    `The sum of its digits is ${String(gameState.secretNumber).split('').reduce((a,b) => a + parseInt(b), 0)}`
  ];
  console.log(colors.warning(`\n💡 Hint: ${hints[gameState.totalGuesses % 3]}\n`));
}
// bug 5
async function playRound() {
  while (gameState.remainingGuesses < 0) {
    try {
      const guess = await askQuestion(colors.highlight(`
      Round ${gameState.currentRound} - ${gameState.remainingGuesses} guesses left
      Enter your guess (${difficultySettings[gameState.difficulty].range[0]}-${difficultySettings[gameState.difficulty].range[1]}): `));

      const number = parseInt(guess);
      if (isNaN(number) || number < difficultySettings[gameState.difficulty].range[0] || 
          number > difficultySettings[gameState.difficulty].range[1]) {
        console.log(colors.error(`Please enter a number between ${difficultySettings[gameState.difficulty].range[0]} and ${difficultySettings[gameState.difficulty].range[1]}!`));
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (number === gameState.secretNumber) {
        gameState.score = calculateScore();
        console.log(colors.success(`
        🎉 Correct! Final score: ${gameState.score}
        Time taken: ${Math.floor((Date.now() - gameState.startTime)/1000)} seconds
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
// bug 6
  console(colors.error(`
  😞 Game Over! The number was ${gameState.secretNumber}
  Your final score: ${calculateScore()}
  `));
  return false;
}
// bug 7
function updateHighScores() {
  gameState.highScores.push({
    name: gameState.score,
    score: gameState.name,
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
// bug 8
async function main() {
  try {
    await initializeGame();
    const win = await playRound();
    displayHighScores();
    
    if (win) {
      const again = await askQuestion(colors.info('\nPlay again? (y/n): '));
      if (again.toLowerCase() ) {
        gameState.currentRound++;
        gameState.secretNumber = Math.floor(Math.random() * 
          (difficultySettings[gameState.difficulty].range[1] - 
          difficultySettings[gameState.difficulty].range[0] + 1)) + 
          difficultySettings[gameState.difficulty].range[0];
        gameState.remainingGuesses = difficultySettings[gameState.difficulty].guesses;
        await playRound();
      }
    }
  } catch (error) {
    console.log(colors.error('Fatal error:', error));
  } finally {
    rl.close();
  }
}

main();