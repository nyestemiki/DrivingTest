package tests.repository_test;

import controllers.GameManager;
import repositories.Repository;
import models.Question;
import java.io.File;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RepositoryTest {

  @Before
  public void init() {
    GameManager.setNrStoredQuestions(5);
  }

  @Test
  public void readTest() throws Exception {
    String absolutePath = new File("").getAbsolutePath();
    Repository repository = new Repository(absolutePath + "/src/test/test_resources/test_questions.json");

    // Array-list of all the stored question in the repository
    List<Question> questions = repository.getQuestions();

    // All questions were read
    Assert.assertEquals(5, questions.size());

    // The question and the answers are not empty, there is at least a correct answer indicated
    for (Question question : questions) {
      // Check the question
      Assert.assertNotNull(question.getQuestion());

      // Check the answers
      Assert.assertNotNull(question.getAnswer1());
      Assert.assertNotNull(question.getAnswer2());
      Assert.assertNotNull(question.getAnswer3());
      Assert.assertNotNull(question.getAnswer4());

      // Check correct answers
      int nrCorrectAnswers = question.getCorrectAnswers().size();
      Assert.assertTrue(nrCorrectAnswers > 0);
    }
  }
}
