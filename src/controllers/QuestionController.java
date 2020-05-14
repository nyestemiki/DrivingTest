package controllers;

import controllers.enums.GAME_OVER_STATUS;
import models.Question;
import java.io.IOException;
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
   * Initializes answers with 'unselected' (false)
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
        int remainingTimeInSecondsInt = GameManager.getInstance().getTimeRemaining();
        timeRemaining.setText(formatTime(remainingTimeInSecondsInt));

        if (remainingTimeInSecondsInt == 0) {
          try {
            setFinalScene();
          } catch (IOException ignored) {}
        }
      };

      while (true) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

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
    // Retrieve current question from the GameManager instance
    int currentQuestionIndex = GameManager.getInstance().currentQuestionIndex();
    Question currentQuestion = MainController.getInstance().getCurrentQuestionSet().get(currentQuestionIndex);

    // Set the header info-display

    // Remaining time
    int timeRemainingInt = GameManager.getInstance().getTimeRemaining();
    timeRemaining.setText(formatTime(timeRemainingInt));

    // Number of remaining questions
    int nrRemainingQuestions = GameManager.getInstance().nrRemainingQuestion();
    nrQuestionsRemaining.setText(nrRemainingQuestions + " Fragen geblieben");

    // Number of correctly answered questions
    int nrCorrectQuestionsInt = GameManager.getInstance().getNrCorrectQuestions();
    nrCorrectQuestions.setText(nrCorrectQuestionsInt + " wahre");

    // Number of incorrectly answered questions
    int nrIncorrectQuestionsInt = GameManager.getInstance().getNrIncorrectQuestions();
    nrIncorrectQuestions.setText(nrIncorrectQuestionsInt + " falsche");

    // Set the displayed question and answers
    displayedQuestion.setText(currentQuestion.getQuestion());
    answer1.setText(currentQuestion.getAnswer1());
    answer2.setText(currentQuestion.getAnswer2());
    answer3.setText(currentQuestion.getAnswer3());
    answer4.setText(currentQuestion.getAnswer4());

    // Load the image
    String imageUrl = "/resources/images/" + currentQuestion.getImageId() + ".png";
    Image image = new Image(imageUrl);
    picture.setImage(image);

    // Animation
    new FadeIn(questionContainer).play();

    // DEVELOPMENT
    System.out.println(currentQuestion.getCorrectAnswers());
  }

  /**
   * Handles the leap towards the next question
   */
  @FXML
  public void nextQuestion() throws Exception {
    // Check if number of incorrect questions is valid
    try {
      // Check if submitted answer is valid
      if (checkAnswer()) {
        // Answers were correct
        GameManager.getInstance().correctAnswer();
      } else {
        // Answers were incorrect
        GameManager.getInstance().incorrectAnswer();
      }

      // Check if game is over ( last question answered )
      if (GameManager.getInstance().finished()) {
        testFinished();

        return;
      }

      // Check if current question is the last one in the current set of questions
      if (GameManager.getInstance().lastQuestion()) {
        // Button at the last questions shows finish instead of next question
        nextQuestion.setText("Fertig");
      }

      // Clearing the previously selected answers
      clearSelectedAnswers();

      if (GameManager.getInstance().getGame_over_status() == GAME_OVER_STATUS.TIME_UP) {
        setFinalScene();
      }

      // Finally load the current question
      loadCurrentQuestion();

    } catch (NrAllowedIncorrectQuestionsExceededException e) {
      testFailed(); // Too many incorrect answers were chosen
    }
  }

  /**
   * Loads the test failed scene
   *
   * @throws IOException if scene could not be loaded
   */
  private void testFailed() throws IOException {
    // Sets the status of the terminated test
    GameManager.getInstance().gameOver_failed_answers();

    setFinalScene();
  }

  /**
   * Loads the successfully finished test scene
   */
  private void testFinished() throws IOException {
    // Sets the status of the terminated test to successful
    GameManager.getInstance().gameOver_successful();

    setFinalScene();
  }

  /**
   * Sets the final scene
   *
   * @throws IOException if scene could not be loaded
   */
  private void setFinalScene() throws IOException {
    // Current stage
    Stage stage = (Stage)question.getScene().getWindow();
    // Wanted pane
    Pane finalPane = FXMLLoader.load(getClass().getResource("/scenes/final_scene.fxml"));
    // Wanted scene
    Scene finalScene = new Scene(finalPane);
    // Setting new stage
    stage.setScene(finalScene);
    // Display the final scene
    stage.show();
  }

  /**
   * Restores unselected answers
   */
  public void clearSelectedAnswers() {
    // No answer is selected
    answer1State = false;
    answer2State = false;
    answer3State = false;
    answer4State = false;

    try {
      // Removing the visual selection
      answer1.getStyleClass().remove("selected");
      answer2.getStyleClass().remove("selected");
      answer3.getStyleClass().remove("selected");
      answer4.getStyleClass().remove("selected");
    } catch(Exception e) {
      System.out.println("Could not deselect answers visually");
    }
  }

  /**
   * Changes state of answer 1 to the opposite value
   * and also makes a visual mark
   */
  @FXML
  public void toggleAnswer1() {
    answer1State = !answer1State;

    try {
      // Visually selecting an answer
      if (answer1.getStyleClass().contains("selected")) {
        answer1.getStyleClass().remove("selected");
      } else {
        answer1.getStyleClass().add("selected");
      }
    } catch(Exception e) {
      System.out.println("Could not select answer visually");
    }
  }

  /**
   * Changes state of answer 2 to the opposite value
   * and also makes a visual mark
   */
  @FXML
  public void toggleAnswer2() {
    answer2State = !answer2State;

    try {
      // Visually selecting an answer
      if (answer2.getStyleClass().contains("selected")) {
        answer2.getStyleClass().remove("selected");
      } else {
        answer2.getStyleClass().add("selected");
      }
    } catch(Exception e) {
      System.out.println("Could not select answer visually");
    }
  }

  /**
   * Changes state of answer 3 to the opposite value
   * and also makes a visual mark
   */
  @FXML
  public void toggleAnswer3() {
    answer3State = !answer3State;

    try {
      // Visually selecting an answer
      if (answer3.getStyleClass().contains("selected")) {
        answer3.getStyleClass().remove("selected");
      } else {
        answer3.getStyleClass().add("selected");
      }
    } catch(Exception e) {
      System.out.println("Could not select answer visually");
    }
  }

  /**
   * Changes state of answer 4 to the opposite value
   * and also makes a visual mark
   */
  @FXML
  public void toggleAnswer4() {
    answer4State = !answer4State;

    try {
      // Visually selecting an answer
      if (answer4.getStyleClass().contains("selected")) {
        answer4.getStyleClass().remove("selected");
      } else {
        answer4.getStyleClass().add("selected");
      }
    } catch(Exception e) {
      System.out.println("Could not select answer visually");
    }
  }

  /**
   * Checks if the selected answer was valid
   *
   * @return state of selected answer
   */
  public boolean checkAnswer() {
    // Current question
    int currentQuestionIndex = GameManager.getInstance().currentQuestionIndex();
    Question currentQuestion = MainController.getInstance().getCurrentQuestionSet().get(currentQuestionIndex);
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

    return true;
  }
}
