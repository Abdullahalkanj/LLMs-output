// ChatGPT4omini output using basic prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// There are actually two show-stopper bugs here that will either crash your game immediately or make the guessing loop never run.

// 1) Typo in askQuestion
// You declared the parameter as prompt but then wrote rl.question(pr, …), which blows up with “​ReferenceError: pr is not defined”. Also your early-return logic is inverted. Here’s the fixed version:

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve => rl.question(prompt, resolve));
    if (!input.trim()) {
      console.log(colors.error('Error: Cannot enter an empty answer.'));
      continue;
    }
    return input.trim();
  }
}
// Use rl.question(prompt, …) instead of pr

// If the user just hits Enter, log the error and continue the loop

// Otherwise return input.trim()

// 2) Wrong loop condition in playRound
// You have:


async function playRound() {
  while (gameState.remainingGuesses < 0) {
    // … never runs, because remainingGuesses starts positive …
  }
  console(colors.error(`…`));
  return false;
}
// That guard should be > 0, not < 0, and the final “Game Over” line needs to be console.log, not console(...). Here’s the fix:


async function playRound() {
  while (gameState.remainingGuesses > 0) {
    try {
      // … all your guessing logic …
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
// With those two changes in place your game will actually prompt the user correctly and enter the guessing loop until the guesses run out. (You’ll probably also want to fix a few of the other typos—e.g. ['1','2','6'] → ['1','2','3'] in selectDifficulty, and swapping name/score in your high-scores—but those are cosmetic compared to the two above.)





// You said:
// give the whole code 
// ChatGPT said:
// Here’s the fully revised version with all of the fixes applied:
const readline = require('readline');
const chalk = require('chalk');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

let gameState = {
  score: 0,
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
  easy:   { range: [1,  50],  guesses: 10, multiplier: 1 },
  medium: { range: [1,  100], guesses: 7,  multiplier: 2 },
  hard:   { range: [1,  200], guesses: 5,  multiplier: 3 }
};

async function askQuestion(prompt) {
  while (true) {
    const input = await new Promise(resolve => rl.question(prompt, resolve));
    if (!input.trim()) {
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
  const map = { '1': 'easy', '2': 'medium', '3': 'hard' };
  while (true) {
    const choice = await askQuestion(colors.info(`
Select difficulty:
1) Easy   (1–50,  10 guesses)
2) Medium (1–100, 7 guesses)
3) Hard   (1–200, 5 guesses)
Enter choice (1–3): `));
    if (map[choice]) {
      gameState.difficulty = map[choice];
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
  const timeBonus = Math.max(
    0,
    300 - Math.floor((Date.now() - gameState.startTime) / 1000)
  );
  const guessPenalty = gameState.totalGuesses * 50;
  const multiplier = difficultySettings[gameState.difficulty].multiplier;
  // Assign to gameState.score, then apply multiplier
  gameState.score = timeBonus - guessPenalty;
  return gameState.score * multiplier;
}

async function getHint() {
  const s = difficultySettings[gameState.difficulty];
  const hints = [
    `The number is ${gameState.secretNumber % 2 === 0 ? 'even' : 'odd'}.`,
    `It’s between ${Math.max(s.range[0], gameState.secretNumber - 10)} and ${Math.min(s.range[1], gameState.secretNumber + 10)}.`,
    `The sum of its digits is ${String(gameState.secretNumber)
      .split('')
      .reduce((a, b) => a + parseInt(b, 10), 0)}.`
  ];
  console.log(colors.warning(`\n💡 Hint: ${hints[gameState.totalGuesses % hints.length]}\n`));
}

async function playRound() {
  while (gameState.remainingGuesses > 0) {
    try {
      const prompt = colors.highlight(`
Round ${gameState.currentRound} — ${gameState.remainingGuesses} guesses left
Enter your guess (${difficultySettings[gameState.difficulty].range[0]}–${difficultySettings[gameState.difficulty].range[1]}): `);
      const guess = await askQuestion(prompt);
      const num = parseInt(guess, 10);
      const s = difficultySettings[gameState.difficulty];
      if (isNaN(num) || num < s.range[0] || num > s.range[1]) {
        console.log(
          colors.error(`Please enter a number between ${s.range[0]} and ${s.range[1]}!`)
        );
        continue;
      }

      gameState.totalGuesses++;
      gameState.remainingGuesses--;

      if (num === gameState.secretNumber) {
        const finalScore = calculateScore();
        console.log(colors.success(`
🎉 Correct! Final score: ${finalScore}
Time taken: ${Math.floor((Date.now() - gameState.startTime) / 1000)}s
`));
        updateHighScores();
        return true;
      }

      console.log(colors.warning(`Too ${num < gameState.secretNumber ? 'low' : 'high'}!`));
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
  gameState.highScores.forEach((e, i) => {
    console.log(`${i + 1}. ${e.name} — ${e.score} (${e.difficulty})`);
  });
}

async function main() {
  try {
    await initializeGame();
    let playAgain = true;

    while (playAgain) {
      const won = await playRound();
      displayHighScores();

      if (won) {
        const ans = await askQuestion(colors.info('\nPlay again? (y/n): '));
        playAgain = ans.trim().toLowerCase().startsWith('y');
        if (playAgain) {
          gameState.currentRound++;
          gameState.totalGuesses = 0;
          gameState.startTime = Date.now();
          const s = difficultySettings[gameState.difficulty];
          gameState.secretNumber = Math.floor(
            Math.random() * (s.range[1] - s.range[0] + 1)
          ) + s.range[0];
          gameState.remainingGuesses = s.guesses;
        }
      } else {
        playAgain = false;
      }
    }
  } catch (err) {
    console.log(colors.error('Fatal error:', err.message));
  } finally {
    rl.close();
  }
}

main();
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
