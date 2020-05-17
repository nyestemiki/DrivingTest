package controllers;

import models.Question;
import repositories.Repository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controls the whole game
 */
public class MainController {

  /**
   * Singleton main controller instance
   */
  private static MainController instance = null;

  /**
   * GameManager is a singleton object
   *
   * @return the only GameManager instance
   */
  public static MainController getInstance() {
    if (instance == null) {
      instance = new MainController();
    }

    return instance;
  }

  /**
   * Other object references
   */
  private Repository repository;
  private List<Question> currentQuestionSet;

  /**
   * Loads the repository, GameManager and the starting set of questions
   */
  public MainController() {
    // Absolute path to project
    String filePath = new File("").getAbsolutePath();

    // Loading in the questions from the repository file
    try {
      repository = new Repository(filePath + "/src/resources/questions.json");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    // Generating the starting set of questions
    newQuestionSet();
  }

  /**
   * Returns 26 random questions (with no repetition) from the repository
   *
   * @return Array-list of 26 non-repeating question instances
   */
  private List<Question> questionSet() {
    // Will contain the set of questions to be returned
    List<Question> randomQuestionSet = new ArrayList<>();

    // Will contain the ids of the questions to be returned ( used to omit duplicates )
    List<Integer> randomIds = new ArrayList<>();

    int randomId; // Currently checked id

    for (int i = 0; i < GameManager.getNrTotalQuestions(); i++) {
      // Looking for the first random id that is not already stored
      do {
        randomId = (new Random()).nextInt(GameManager.getNrStoredQuestions());
      } while(randomIds.indexOf(randomId) != -1);

      // id added to the used ids list
      randomIds.add(randomId);

      // question added to the list of questions to be returned
      randomQuestionSet.add(repository.getQuestions().get(randomId));
    }

    return randomQuestionSet;
  }

  /**
   * Generates a new set of questions
   */
  void newQuestionSet() {
    currentQuestionSet = questionSet();
  }

  /**
   * Returns the current set of questions
   *
   * @return an array-list of the current set of questions
   */
  public List<Question> getCurrentQuestionSet() {
    return currentQuestionSet;
  }
}
