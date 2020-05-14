package tests.controller_test;

import controllers.GameManager;
import controllers.MainController;
import controllers.QuestionController;
import java.util.List;
import models.Question;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QuestionControllerTest {

  /**
   * Simulates a test run
   */
  @Test
  public void questionControllerTest() {
    QuestionController questionController = new QuestionController();

    // get the stored questions
    new MainController();
    List<Question> questions = MainController.getInstance().getCurrentQuestionSet();
    // for each question
    for (Question question : questions) {
      // get the correct answers
      List<Long> correctAnswers = question.getCorrectAnswers();
      // toggle them
      for (Long answer : correctAnswers) {
        if (answer == 0) {
          questionController.toggleAnswer1();
        }
        if (answer == 1) {
          questionController.toggleAnswer2();
        }
        if (answer == 2) {
          questionController.toggleAnswer3();
        }
        if (answer == 3) {
          questionController.toggleAnswer4();
        }
      }

      //    call checkAnswers
      Assert.assertTrue(questionController.checkAnswer());
      questionController.clearSelectedAnswers();
      GameManager.getInstance().correctAnswer();
    }
  }
}
