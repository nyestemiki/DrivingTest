package tests;

import exceptions.NrAllowedIncorrectQuestionsExceededException;
import controllers.enums.GAME_OVER_STATUS;
import controllers.GameManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests the GameManager singleton
 */
@RunWith(JUnit4.class)
public class GameManagerTest {

  /**
   * Simulates two tests, one with failed answers, one successful
   */
  @Test
  public void gameManagerSimulationTest() {
    GameManager.getInstance().correctAnswer();
    GameManager.getInstance().correctAnswer();
    GameManager.getInstance().correctAnswer();
    GameManager.getInstance().correctAnswer();

    try {
      GameManager.incorrectAnswer();
      GameManager.incorrectAnswer();
    } catch (Exception e) {
      assert false;
    }

    GameManager.getInstance().correctAnswer();
    GameManager.getInstance().correctAnswer();
    GameManager.getInstance().correctAnswer();

    Assert.assertEquals(9, GameManager.getInstance().currentQuestionIndex());

    try {
      GameManager.incorrectAnswer();
      GameManager.incorrectAnswer();
    } catch (Exception e) {
      assert false;
    }

    try {
      GameManager.incorrectAnswer();
    } catch (NrAllowedIncorrectQuestionsExceededException e) {
      GameManager.getInstance().gameOver_failed_answers();
    }

    Assert.assertEquals(GAME_OVER_STATUS.FAILED_ANSWERS, GameManager.getInstance().getGame_over_status());

    GameManager.getInstance().newGame();

    for (int i = 0; i < 26; i++) {
      GameManager.getInstance().correctAnswer();
    }

    GameManager.getInstance().gameOver_successful();
    Assert.assertEquals(GAME_OVER_STATUS.SUCCESSFUL, GameManager.getInstance().getGame_over_status());
  }
}
