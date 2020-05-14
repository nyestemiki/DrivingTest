package controllers;

import models.Timer;
import controllers.enums.GAME_OVER_STATUS;
import exceptions.NrAllowedIncorrectQuestionsExceededException;

/**
 * SINGLETON CLASS
 *
 * Controls the defining features of the match
 * Note: questions are linked to the main controller as it is not relevant to the status of the game
 *  whether there is a question at all
 */
public class GameManager {

  /**
   * Singleton instance of the GameManager
   */
  private static GameManager instance = null;

  /**
   * Static members - define the overall app ( not only a singular test )
   */
  private static int gameCount; // The number of all tests taken since the starting of the app
  private static int nrSuccessfulTests; // The number of all successful test from the starting of the app
  private static int nrTotalQuestions; // Total questions a test contains
  private static int nrIncorrectQuestionsAllowed; // Number of incorrect questions allowed to make
  private static int maxTimeAvailable; // The maximum time the test has to be completed in
  private static int nrStoredQuestions; // The number of stored questions in the repository file

  /**
   * Status of the terminated game
   */
  private GAME_OVER_STATUS game_over_status = null;

  /**
   * Timer thread
   */
  private Timer timer;

  /**
   * Status of the ongoing test
   */
  private int nrCurrentQuestion; // Index of question that is currently being answered
  private int nrCorrectQuestions; // Number of questions answered correctly in the current test
  private int nrIncorrectQuestions; // Number of questions answered incorrectly in the ongoing test

  /**
   * Returns the number of remaining questions
   *
   * @return number of question left to be answered
   */
  int nrRemainingQuestion() {
    return nrTotalQuestions - nrCurrentQuestion;
  }

  /**
   * Constructor initializes both the static ( app related ) and non-static ( test-related )
   * variables of the app
   */
  public GameManager() {
    // App related
    gameCount = 0; // No tests taken at this point
    nrSuccessfulTests = 0; // No tests taken at this point
    nrTotalQuestions = 26; // Default number of questions is 26
    nrIncorrectQuestionsAllowed = 4; // Default number of allowed incorrect questions
    maxTimeAvailable = 30*60; // 30 minutes in seconds (30x60)
    nrStoredQuestions = 50; // There are 50 questions stored in the file

    // Test related
    nrCurrentQuestion = 0; // Starting question index: 0
    nrCorrectQuestions = 0; // At the beginning there are not questions answered
    nrIncorrectQuestions = 0; // At the beginning there are not questions answered

    // Timer
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
    return maxTimeAvailable;
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
  public void incorrectAnswer() throws NrAllowedIncorrectQuestionsExceededException {
    nrIncorrectQuestions++;

    // Check if number of allowed incorrect answers surpassed
    if (nrIncorrectQuestions > nrIncorrectQuestionsAllowed) {
      throw new NrAllowedIncorrectQuestionsExceededException();
    }

    nextQuestion();
  }

  /**
   * Restores initial test-values
   */
  public void newGame() {
    gameCount++; // Number of all tests is incremented upon terminating a test

    if (GameManager.getInstance().getGame_over_status() == GAME_OVER_STATUS.SUCCESSFUL) {
      successfulTest(); // Number of all successful tests is incremented upon terminating a successful test
    }

    restartTimer();
    restartGame();

    game_over_status = null;
  }

  /**
   * Number of all successful tests is incremented
   */
  private void successfulTest() {
    nrSuccessfulTests++;
  }

  /**
   * Returns number of all unsuccessful tests
   *
   * @return number of all unsuccessful tests
   */
  public int nrUnsuccessfulTests() {
    return gameCount - nrSuccessfulTests;
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
  int getNrCorrectQuestions() {
    return nrCorrectQuestions;
  }

  /**
   * Returns the number of incorrectly answered questions in the current test
   *
   * @return number of incorrectly answered questions in the current test
   */
  int getNrIncorrectQuestions() {
    return nrIncorrectQuestions;
  }

  /**
   * Determines whether the current test is finished
   *
   * @return state of current test
   */
  boolean finished() {
    return nrCurrentQuestion > nrTotalQuestions - 1;
  }

  /**
   * Determines if current question is the last question in the set
   *
   * @return state of last question
   */
  boolean lastQuestion() {
    return nrCurrentQuestion == nrTotalQuestions - 1;
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
  int getTimeRemaining() {
    return timer.getCurrentTime();
  }

  /**
   * Returns the number of total questions in the test
   *
   * @return number of total questions in the test
   */
  int getNrTotalQuestions() {
    return nrTotalQuestions;
  }

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
   * Determines if the timer is active
   *
   * @return status of the timer
   */
  public boolean isTimerActive() {
    return timer.isActive();
  }
}
