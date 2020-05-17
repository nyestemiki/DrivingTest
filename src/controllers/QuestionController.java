package controllers;

import controllers.enums.GAME_OVER_STATUS;
import models.Question;
import exceptions.NrAllowedIncorrectQuestionsExceededException;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import animatefx.animation.FadeIn;

public class QuestionController {

  /**
   * State variables
   */
  private boolean answer1State; // True if answer 1 is selected
  private boolean answer2State; // True if answer 2 is selected
  private boolean answer3State; // True if answer 3 is selected
  private boolean answer4State; // True if answer 4 is selected

  /**
   * Panes
   */
  public Pane question;
  public Pane questionContainer;

  /**
   * Dom elements
   */
  //Media
  @FXML
  public ImageView picture;
  // Labels
  @FXML
  private Label timeRemaining;
  @FXML
  private Label nrQuestionsRemaining;
  @FXML
  private Label nrCorrectQuestions;
  @FXML
  private Label nrIncorrectQuestions;
  @FXML
  private Label displayedQuestion;
  // Buttons
  @FXML
  private Button answer1;
  @FXML
  private Button answer2;
  @FXML
  private Button answer3;
  @FXML
  private Button answer4;
  @FXML
  private Button nextQuestion;

  /**
   * Initializes answers as not selected
   */
  public QuestionController() {
    answer1State = false;
    answer2State = false;
    answer3State = false;
    answer4State = false;
  }

  /**
   * Loads the currently displayed question
   */
  @FXML
  public void initialize() {
    GameManager.getInstance().startTest(); // Starts new test with new timer
    updateTimer(); // Remaining time updater thread
    loadCurrentQuestion(); // loads the first question
  }

  /**
   * Updates time remaining
   */
  private void updateTimer() {
    Thread thread = new Thread(() -> {
      Runnable updater = () -> {
        int remainingTimeInSecondsInt = GameManager.getTimeRemaining();
        timeRemaining.setText(formatTime(remainingTimeInSecondsInt));

        // Time is up
        if (remainingTimeInSecondsInt == 0) {
            setFinalScene();
        }
      };

      while (true) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        Platform.runLater(updater); // UI update is run on the Application thread
      }
    });

    thread.setDaemon(true); // don't let thread prevent JVM shutdown
    thread.start();
  }

  /**
   * Returns a formatted string that represents time as mm:ss
   *
   * @param time time in seconds to be formatted
   * @return formatted string of time
   */
  private String formatTime(int time) {
    int minutes = time / 60;
    int seconds = time % 60;

    // Display 0x for minutes and seconds smaller than 10
    String minutesString = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);
    String secondsString = (seconds < 10) ? "0" + seconds : String.valueOf(seconds);

    return minutesString + ":" + secondsString;
  }

  /**
   * Sets the values of the new question
   */
  private void loadCurrentQuestion() {
    updateHeader();
    List<Long> correctAnswers = updateQuestion();

    new FadeIn(questionContainer).play();

    System.out.println(correctAnswers);
  }

  /**
   * Sets the data in the header
   */
  private void updateHeader() {
    timeRemaining.setText(formatTime(GameManager.getTimeRemaining()));
    nrQuestionsRemaining.setText(GameManager.nrRemainingQuestion() + " Fragen geblieben");
    nrCorrectQuestions.setText(GameManager.getNrCorrectQuestions() + " wahre");
    nrIncorrectQuestions.setText(GameManager.getNrIncorrectQuestions() + " falsche");
  }

  /**
   * Sets the new question
   *
   * @return an array of the correct answers
   */
  private List<Long> updateQuestion() {
    int currentQuestionIndex = GameManager.getInstance().currentQuestionIndex();
    Question currentQuestion = MainController.getCurrentQuestionSet().get(currentQuestionIndex);

    displayedQuestion.setText(currentQuestion.getQuestion());
    answer1.setText(currentQuestion.getAnswer1());
    answer2.setText(currentQuestion.getAnswer2());
    answer3.setText(currentQuestion.getAnswer3());
    answer4.setText(currentQuestion.getAnswer4());
    picture.setImage(new Image("/resources/images/" + currentQuestion.getImageId() + ".png"));

    return currentQuestion.getCorrectAnswers();
  }

  /**
   * Handles the leap towards the next question
   */
  @FXML
  public void nextQuestion() {
    // Check if number of incorrect questions is valid
    try {
      // Check if submitted answer is valid
      if (checkAnswer()) {
        // Answers were correct
        GameManager.getInstance().correctAnswer();
      } else {
        // Answers were incorrect
        GameManager.incorrectAnswer();
      }

      // Check if game is over ( last question answered )
      if (GameManager.finished()) {
        testFinished();
        return;
      }

      if (GameManager.lastQuestion()) {
        nextQuestion.setText("Fertig");
      }

      // Clearing the previous answers
      clearSelectedAnswers();

      if (GameManager.getInstance().getGame_over_status() == GAME_OVER_STATUS.TIME_UP) {
        setFinalScene();
      }

      // Load the prepared question
      loadCurrentQuestion();

    } catch (NrAllowedIncorrectQuestionsExceededException e) {
      testFailed();
    }
  }

  /**
   * Loads the test failed scene
   */
  private void testFailed() {
    // Sets the status of the terminated test
    GameManager.getInstance().gameOver_failed_answers();

    setFinalScene();
  }

  /**
   * Loads the successfully finished test scene
   */
  private void testFinished() {
    // Sets the status of the terminated test to successful
    GameManager.getInstance().gameOver_successful();

    setFinalScene();
  }

  /**
   * Sets the final scene
   */
  private void setFinalScene() {
    try {
      Stage stage = (Stage)question.getScene().getWindow();
      Pane finalPane = FXMLLoader.load(getClass().getResource("/scenes/final_scene.fxml"));
      stage.setScene(new Scene(finalPane));

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Restores unselected answers
   */
  public void clearSelectedAnswers() {
    clearAnswers();
    clearSelection();
  }

  /**
   * No answer selected
   */
  private void clearAnswers() {
    answer1State = false;
    answer2State = false;
    answer3State = false;
    answer4State = false;
  }

  /**
   * No answer button selected
   */
  private void clearSelection() {
    answer1.getStyleClass().remove("selected");
    answer2.getStyleClass().remove("selected");
    answer3.getStyleClass().remove("selected");
    answer4.getStyleClass().remove("selected");
  }

  /**
   * Changes state of answer 1 to the opposite value and makes a visual mark
   */
  @FXML
  public void toggleAnswer1() {
    answer1State = !answer1State;

    if (answer1.getStyleClass().contains("selected")) {
      answer1.getStyleClass().remove("selected");
    } else {
      answer1.getStyleClass().add("selected");
    }
  }

  /**
   * Changes state of answer 2 to the opposite value and makes a visual mark
   */
  @FXML
  public void toggleAnswer2() {
    answer2State = !answer2State;

    if (answer2.getStyleClass().contains("selected")) {
      answer2.getStyleClass().remove("selected");
    } else {
      answer2.getStyleClass().add("selected");
    }
  }

  /**
   * Changes state of answer 3 to the opposite value and makes a visual mark
   */
  @FXML
  public void toggleAnswer3() {
    answer3State = !answer3State;

    if (answer3.getStyleClass().contains("selected")) {
      answer3.getStyleClass().remove("selected");
    } else {
      answer3.getStyleClass().add("selected");
    }
  }

  /**
   * Changes state of answer 4 to the opposite value and makes a visual mark
   */
  @FXML
  public void toggleAnswer4() {
    answer4State = !answer4State;

    if (answer4.getStyleClass().contains("selected")) {
      answer4.getStyleClass().remove("selected");
    } else {
      answer4.getStyleClass().add("selected");
    }
  }

  /**
   * Validates the answers
   *
   * @return state of selected answer
   */
  public boolean checkAnswer() {
    int currentQuestionIndex = GameManager.getInstance().currentQuestionIndex();
    Question currentQuestion = MainController.getCurrentQuestionSet().get(currentQuestionIndex);

    List<Long> correctAnswers = currentQuestion.getCorrectAnswers();

    // All correct answers selected, and only the correct answers selected
    if ((correctAnswers.contains(0L) && !answer1State) || (!correctAnswers.contains(0L) && answer1State)) {
      return false;
    }
    if ((correctAnswers.contains(1L) && !answer2State) || (!correctAnswers.contains(1L) && answer2State)) {
      return false;
    }
    if ((correctAnswers.contains(2L) && !answer3State) || (!correctAnswers.contains(2L) && answer3State)) {
      return false;
    }
    if ((correctAnswers.contains(3L) && !answer4State) || (!correctAnswers.contains(3L) && answer4State)) {
      return false;
    }

    // Correct answer
    MainController.setIndexCorrect(currentQuestionIndex);

    return true;
  }
}
