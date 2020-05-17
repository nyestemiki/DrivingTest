package controllers;

import models.Preferences;
import models.Timer;
import controllers.enums.GAME_OVER_STATUS;
import exceptions.NrAllowedIncorrectQuestionsExceededException;

/**
 * SINGLETON CLASS
 *
 * Controls the defining features of the match
 */
public class GameManager {
  private static GameManager instance = null;

  private static int nrStoredQuestions; // The number of stored questions in the repository file
  private static Preferences preferences;

  private GAME_OVER_STATUS game_over_status = null;
  private Timer timer;

  /**
   * Status of the ongoing test
   */
  private int nrCurrentQuestion; // Index of question that is currently being answered
  private int nrCorrectQuestions; // Number of questions answered correctly in the current test
  private int nrIncorrectQuestions; // Number of questions answered incorrectly in the ongoing test

  /**
   * Constructor initializes both the static ( app related ) and non-static ( test-related )
   * variables of the app
   */
  public GameManager() {
    // App related
    nrStoredQuestions = 50; // There are 50 questions stored in the file

    // Test related
    nrCurrentQuestion = 0; // Starting question index: 0
    nrCorrectQuestions = 0; // At the beginning there are not questions answered
    nrIncorrectQuestions = 0; // At the beginning there are not questions answered

    if (preferences == null) {
      preferences = new Preferences(30*60, 26, 4);
    }

    timer = new Timer();
  }

  /**
   * Responsible for the singleton principle
   *
   * @return singleton GameManager instance
   */
  public static GameManager getInstance() {
    if (instance == null) {
      instance = new GameManager(); // First init of the singleton object
    }

    return instance; // Singleton object already initialized
  }

  // GAME related

  /**
   * Restores initial values
   */
  public void newGame() {
    restartTimer();
    restartGame();

    game_over_status = null;
  }

  /**
   * Restores the initial test-related variables
   */
  private void restartGame()  {
    MainController.getInstance().newQuestionSet(); // New set of questions is loaded

    nrCurrentQuestion = 0; // New test starts at index 0
    nrCorrectQuestions = 0; // Restoring number of correctly answered questions
    nrIncorrectQuestions = 0; // Restoring number of incorrectly answered questions
  }

  /**
   * Takes the test to the next question
   */
  private void nextQuestion() {
    nrCurrentQuestion++;
  }

  /**
   * Question answered correctly ( -> next question )
   */
  public void correctAnswer() {
    nrCorrectQuestions++;

    nextQuestion();
  }

  /**
   * Question answered incorrectly ( -> next question )
   *
   * @throws NrAllowedIncorrectQuestionsExceededException if more questions were answered incorrectly than the limit
   */
  public static void incorrectAnswer() throws NrAllowedIncorrectQuestionsExceededException {
    GameManager.getInstance().nrIncorrectQuestions++;

    // Check if number of allowed incorrect answers surpassed
    if (GameManager.getInstance().nrIncorrectQuestions > preferences.getNrIncorrectQuestionsAllowed()) {
      throw new NrAllowedIncorrectQuestionsExceededException();
    }

    GameManager.getInstance().nextQuestion();
  }

  /**
   * Starts a new test with a new timer
   */
  void startTest() {
    startTimer();
  }

  /**
   * Terminates the test ( time is up )
   */
  public void time_up() {
    gameOver_time_up();
  }

  /**
   * Determines whether the current test is finished
   *
   * @return state of current test
   */
  public static boolean finished() {
    return GameManager.getInstance().nrCurrentQuestion > preferences.getNrTotalQuestions() - 1;
  }

  /**
   * Determines if current question is the last question in the set
   *
   * @return state of last question
   */
  public static boolean lastQuestion() {
    return GameManager.getInstance().nrCurrentQuestion == preferences.getNrTotalQuestions() - 1;
  }

  /**
   * Returns the number of remaining questions
   *
   * @return number of question left to be answered
   */
  public static int nrRemainingQuestion() {
    return GameManager.preferences.getNrTotalQuestions() - GameManager.getInstance().nrCurrentQuestion;
  }

  /**
   * Returns the number of stored questions
   *
   * @return number of stored questions
   */
  public static int getNrStoredQuestions() {
    return nrStoredQuestions;
  }

  /**
   * Sets the number of stored questions
   *
   * @param nrQuestions number of questions stored in the repository file
   */
  public static void setNrStoredQuestions(int nrQuestions) {
    nrStoredQuestions = nrQuestions;
  }

  /**
   * Returns the maximum time available to complete the test
   *
   * @return the maximum time to complete the test
   */
  public static int getMaxTimeAvailable() {
    return preferences.getMaxTimeAvailable();
  }

  /**
   * Returns the current question index
   *
   * @return index of current question
   */
  public int currentQuestionIndex() {
    return nrCurrentQuestion;
  }

  /**
   * Returns the number of correctly answered questions in the current test
   *
   * @return number of correctly answered questions in the current test
   */
  public static int getNrCorrectQuestions() {
    return GameManager.getInstance().nrCorrectQuestions;
  }

  /**
   * Returns the number of incorrectly answered questions in the current test
   *
   * @return number of incorrectly answered questions in the current test
   */
  public static int getNrIncorrectQuestions() {
    return GameManager.getInstance().nrIncorrectQuestions;
  }

  /**
   * Returns the state of the terminated test
   *
   * @return state of the terminated test
   */
  public GAME_OVER_STATUS getGame_over_status() {
    return game_over_status;
  }

  /**
   * Returns the remaining time in seconds
   *
   * @return remaining time in seconds
   */
  public static int getTimeRemaining() {
    return GameManager.getInstance().timer.getCurrentTime();
  }

  /**
   * Returns the number of total questions in the test
   *
   * @return number of total questions in the test
   */
  public static int getNrTotalQuestions() { return preferences.getNrTotalQuestions(); }

  public void setPreferences(Preferences preferences) {
    GameManager.preferences = preferences;
  }

  // Game over status manipulation

  /**
   * Sets the status of the current test to successful
   */
  public void gameOver_successful() {
    game_over_status = GAME_OVER_STATUS.SUCCESSFUL;
  }

  /**
   * Sets the status of the current test to failed as time is up
   */
  private void gameOver_time_up() {
    game_over_status = GAME_OVER_STATUS.TIME_UP;
  }

  /**
   * Sets the status of the current test to failed as too many answers were incorrect
   */
  public void gameOver_failed_answers() {
    game_over_status = GAME_OVER_STATUS.FAILED_ANSWERS;
  }

  // TIMER related

  /**
   *  Starts the timer for the test
   */
  private void startTimer() {
    // Check if the timer thread exists
    if (timer.isAlive()) {
      restartTimer(); // Timer exists but its data is deprecated
    } else {
      // Create timer if not exists
      timer = new Timer();
      timer.start();
    }

    timer.setActive();
  }

  /**
   * Restarts the timer
   */
  private void restartTimer() {
    timer.restart();
  }

  /**
   * Stops the timer
   */
  public void stopTimer() {
    timer.stopTimer();
  }
}
