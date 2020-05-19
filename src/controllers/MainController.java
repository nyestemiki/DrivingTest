package controllers;

import models.Question;
import repositories.Repository;
import repositories.interfaces.IRepositoty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controls the whole game
 */
public class MainController {
  private IRepositoty repository;
  private List<Question> currentQuestionSet;

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
   * Loads the repository and the starting set of questions
   */
  public MainController() {
    // Loading in the questions from the repository file
    try {
      repository = new Repository(new File("").getAbsolutePath() + "/src/resources/questions.json");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Generating the starting set of questions
    newQuestionSet();
  }

  /**
   * Returns the specified number of random questions (with no repetition)
   *
   * @return Array-list of a set of question instances
   */
  private List<Question> questionSet() {
    List<Question> randomQuestionSet = new ArrayList<>();
    List<Integer> randomIds = new ArrayList<>();

    int randomId; // Currently checked id

    for (int i = 0; i < GameManager.getNrTotalQuestions(); i++) {
      // Looking for the first random id that is not already stored
      do {
        randomId = (new Random()).nextInt(GameManager.getNrStoredQuestions());
      } while(randomIds.indexOf(randomId) != -1);

      randomIds.add(randomId);
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
  public static List<Question> getCurrentQuestionSet() {
    return getInstance().currentQuestionSet;
  }

  /**
   * Marks the question by the specified index as correctly answered
   *
   * @param index of the question to be marked as correctly answered
   */
  public static void setIndexCorrect(int index) {
    getInstance().currentQuestionSet.get(index).setAnswer(true);
  }
}
