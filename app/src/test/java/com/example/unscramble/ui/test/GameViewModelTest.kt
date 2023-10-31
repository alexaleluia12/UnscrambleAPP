package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {
    // instancia nova GameView a cada teste
    private val viewModel = GameViewModel()

    /*
     * formatdo frase de teste
     thingUnderTest = gameViewModel
    TriggerOfTest = CorrectWordGuessed
    ResultOfTest = ScoreUpdatedAndErrorFlagUnset
     */

    // sucess
    @Test
    fun gameViewModel_CorrectWordGuess_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value

        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambleWord)
        viewModel.updateUserGuest(correctPlayerWord)
        viewModel.checkUserGuest()

        currentGameUiState = viewModel.uiState.value
        // Assert that checkUserGuess() method updates isGuessedWordWrong is updated correctly.
        assertFalse(currentGameUiState.isGuessedWordWrong)
        // Assert that score is updated correctly.
        assertEquals(SCORE_VALUE_AFTER_FIRT_RIGHT_ASWER, currentGameUiState.score)
    }

    // error
    @Test
    fun gameViewModel_IncorrectWordGuess_ErrorFlagSet() {
        val incorrectPlayerWrod = "and"
        viewModel.updateUserGuest(incorrectPlayerWrod)
        viewModel.checkUserGuest()

        val currentGameUistate = viewModel.uiState.value
        // assert that the score is unchanged
        assertEquals(0, currentGameUistate.score)
        // assert that .checkUserGuest() updated isGuestWrong correctly
        assertTrue(currentGameUistate.isGuessedWordWrong)
    }

    // boudary
    @Test
    fun gameViewModel_Inicilization_FirstWordLoaded() {
        val gameCurrentState = viewModel.uiState.value
        val unScrumbleWord = getUnscrambledWord(gameCurrentState.currentScrambleWord)

        assertNotEquals(unScrumbleWord, gameCurrentState.currentWordCount)
        assertFalse(gameCurrentState.isGameOver)
        assertFalse(gameCurrentState.isGuessedWordWrong)
        assertEquals("", viewModel.userGuess)
        assertEquals(0, gameCurrentState.score)
        assertEquals(1, gameCurrentState.currentWordCount)
    }
    @Test
    fun gameViewModel_FinishGame_UpdateUiCorrect() {
        var currentGameUiState = viewModel.uiState.value
        var correctWorld = getUnscrambledWord(currentGameUiState.currentScrambleWord)
        repeat(MAX_NO_OF_WORDS) {
            viewModel.updateUserGuest(correctWorld)
            viewModel.checkUserGuest()
            currentGameUiState = viewModel.uiState.value
            correctWorld = getUnscrambledWord(currentGameUiState.currentScrambleWord)
        }

        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)
    }

    // increse coverage
    @Test
    fun gameViewModel_WordSkiped_ScoreUnchageWrodCountIncreased() {
        viewModel.skipWord()
        val currentGameUiState = viewModel.uiState.value
        assertEquals(2, currentGameUiState.currentWordCount)
        assertEquals(0, currentGameUiState.score)
    }


    companion object {
        private const val SCORE_VALUE_AFTER_FIRT_RIGHT_ASWER = SCORE_INCREASE
    }
}