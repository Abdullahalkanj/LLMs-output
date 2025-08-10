import { expect } from 'chai';
import {
  gameState,
  difficultySettings,
  askQuestion,
  selectDifficulty,
  playRound,
  updateHighScores,
  main
} from '../number-guessing-game.mjs';

describe('Bug Detection in number-guessing-game.mjs', () => {

  it('difficultySettings should use numeric ranges', () => {
    expect(difficultySettings.easy.range[0]).to.be.a('number'); // should fail if it's RegExp
    expect(difficultySettings.hard.range[0]).to.be.greaterThan(0); // should fail if -1
  });

  it('askQuestion should use correct variable and logic', () => {
  const fnString = askQuestion.toString();

  // Ensure function uses prompt as parameter
  expect(fnString.startsWith('async function askQuestion(prompt')).to.be.true;

  // Ensure it uses rl.question with 'prompt' (not any hardcoded wrong var)
  expect(fnString).to.include('rl.question(prompt');

  // Ensure correct return logic
  expect(fnString).to.include('return input.trim();');

  // Ensure incorrect return syntax is not used
  expect(fnString).to.not.include('return input.trim(),');

  // Ensure balanced braces
  const openBraces = (fnString.match(/{/g) || []).length;
  const closeBraces = (fnString.match(/}/g) || []).length;
  expect(openBraces).to.equal(closeBraces);
  });


  it('selectDifficulty should validate 1, 2, or 3', () => {
    const code = selectDifficulty.toString();
    expect(code).to.include("['1', '2', '3']");   // valid choices
    expect(code).to.not.include("['1', '2', '6']"); // buggy value
    expect(code).to.include('(1-3)');             // prompt should match
  });

  it('playRound should loop while remainingGuesses > 0 and log properly', () => {
    const code = playRound.toString();
    expect(code).to.include('remainingGuesses > 0'); // correct loop
    expect(code).to.include('console.log');         // not console()
  });

  it('updateHighScores should assign name and score correctly', () => {
    gameState.highScores = [];
    gameState.playerName = 'Alice';
    gameState.score = 999;
    gameState.difficulty = 'medium';

    updateHighScores();

    const entry = gameState.highScores[0];
    expect(entry.name).to.equal('Alice');  // should be playerName
    expect(entry.score).to.equal(999);     // should be score
  });

  it('main should only replay if user says "y"', () => {
    const code = main.toString();
    expect(code).to.match(/again(\.trim\(\))?\.toLowerCase\(\)\s*===\s*['"]y['"]/);
  });

});
