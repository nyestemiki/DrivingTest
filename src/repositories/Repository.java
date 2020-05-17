package repositories;

import repositories.interfaces.IRepositoty;
import models.Question;
import controllers.GameManager;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Managing the data
 */
public class Repository implements IRepositoty {
  private List<Question> questions = new ArrayList<>();

  /**
   * The constructor reads and builds the questions from the given input file
   *
   * @param filename is the path to the storage file
   */
  public Repository(String filename) {
    JSONObject data = null;

    try {
       data = (JSONObject) new JSONParser().parse(new FileReader(filename));
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < GameManager.getNrStoredQuestions(); i++) {
      assert data != null;
      JSONObject currentQuestion = (JSONObject) data.get(String.valueOf(i));

      Long id = (Long) currentQuestion.get("id");
      String questionStr = (String) currentQuestion.get("question");

      List<Long> correctAnswers = new ArrayList<>();
      JSONArray correctAnswersJsonArray = (JSONArray) currentQuestion.get("correctAnswers");
      for (Object o : correctAnswersJsonArray) {
        if (o != null) {
          correctAnswers.add((Long) o);
        }
      }

      String answer1 = (String) currentQuestion.get("answer1");
      String answer2 = (String) currentQuestion.get("answer2");
      String answer3 = (String) currentQuestion.get("answer3");
      String answer4 = (String) currentQuestion.get("answer4");

      Long imageId = (Long) currentQuestion.get("imageId");

      // Building the question instance
      Question question = new Question(
          id,
          questionStr,
          correctAnswers,
          answer1,
          answer2,
          answer3,
          answer4,
          imageId
      );

      // Saving the question instance
      questions.add(question);
    }
  }

  /**
   * Returns all the stored questions
   *
   * @return array-list of stored questions
   */
  public List<Question> getQuestions() {
    return questions;
  }
}
