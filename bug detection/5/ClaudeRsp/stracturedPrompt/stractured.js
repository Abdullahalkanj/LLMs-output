// Claude output using Structured prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// I'll analyze the code you've provided from the number guessing game and identify any bugs or issues.
// Code Analysis
// This is a command-line number guessing game with the following features:

// Different difficulty levels (easy, medium, hard)
// Score calculation based on time taken, number of guesses, and difficulty
// Hints system
// High score tracking

// Looking carefully at the code, I've identified a bug related to game state management when the player chooses to play multiple rounds.
// Bug Identification
// The main bug is in the game's state management when playing multiple rounds. When a player chooses to play again, only a few game state properties are reset:

// secretNumber (generating a new random number)
// remainingGuesses (resetting based on difficulty)
// currentRound (incremented)

// However, several crucial properties are not reset between rounds:

// totalGuesses - This accumulates across all rounds
// startTime - This remains set to the first game's start time

// This causes two significant issues:

// The score calculation uses totalGuesses to assess penalties, but this includes guesses from all previous rounds
// The time bonus is calculated from the initial game start time, not the current round's start time

// Fix
// Here's how to fix the bug:

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
  resetRound();
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
      return;
    }
    console.log(colors.error('Invalid choice! Please enter 1, 2, or 3.'));
  }
}

function resetRound() {
  const settings = difficultySettings[gameState.difficulty];
  gameState.secretNumber = Math.floor(Math.random() * 
    (settings.range[1] - settings.range[0] + 1)) + settings.range[0];
  gameState.remainingGuesses = settings.guesses;
  gameState.totalGuesses = 0;
  gameState.startTime = Date.now();
}

function calculateScore() {
  const timeBonus = Math.max(0, 300 - Math.floor((Date.now() - gameState.startTime) / 1000));
  const guessPenalty = gameState.totalGuesses * 50;
  const difficultyMultiplier = difficultySettings[gameState.difficulty].multiplier;
  return Math.max(0, (1000 - guessPenalty + timeBonus) * difficultyMultiplier);
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

async function playRound() {
  while (gameState.remainingGuesses > 0) {
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
    console.log(`${index + 1}. ${entry.name} - ${entry.score} (${entry.difficulty})`);
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
        resetRound(); // Reset game state for new round
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

// Key Changes Made

// Created a new resetRound() function that properly resets all the necessary game state for each round:

// Generates a new random number
// Resets remaining guesses based on difficulty
// Resets the total guesses counter to 0
// Sets a fresh start time


// Reorganized the code flow:

// The difficulty selection now only sets the difficulty level
// The actual game state initialization happens in the new resetRound() function
// Called resetRound() from both initializeGame() and when the player chooses to play again


// Modified the initialization process:

// In the original code, selectDifficulty() was doing too much (setting difficulty AND initializing game state)
// Now the functions have clearer responsibilities



// Benefits of the Fix

// More accurate scoring: Each round is now scored independently based only on:

// Guesses made in the current round
// Time spent in the current round
// Current difficulty setting


// Better code organization: The game state management is now more centralized and logical with the new resetRound() function.
// Improved gameplay experience: Players won't be unfairly penalized for accumulated guesses or time from previous rounds.

// These changes ensure that each round is a fresh start, making the game fair and consistent while maintaining all the original features and gameplay mechanics.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
