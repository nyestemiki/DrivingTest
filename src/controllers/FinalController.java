package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import animatefx.animation.Pulse;

import controllers.enums.GAME_OVER_STATUS;

/**
 * Controls the final screen
 */
public class FinalController {

  @FXML
  private Pane finalPane;

  @FXML
  public Label title;

  @FXML
  public Label info;

  /**
   * Adjust final scene to successful/failed status and restores starting values for the new test
   *
   */
  @FXML
  public void initialize() {
    GameManager.getInstance().stopTimer();

    // Successful test
    if (GameManager.getInstance().getGame_over_status() == GAME_OVER_STATUS.SUCCESSFUL) {
      // Set the background to green
      finalPane.getStyleClass().add("successful");
      // Set the title to success message
      title.setText("Erfolgreiches Versuch");

      // Displayed information

      // Time remaining
      int timeRemaining = GameManager.getInstance().getTimeRemaining();
      int minutes = timeRemaining / 60;
      int seconds = timeRemaining % 60;

      // Display 0x for minutes and seconds smaller than 10
      String minutesString = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);
      String secondsString = (seconds < 10) ? "0" + seconds : String.valueOf(seconds);
      String timeRemainingString = minutesString + ":" + secondsString;

      String infoString = "Zeit übrig: " + timeRemainingString + "\n";

      String nrCorrectAnswersString = String.valueOf(GameManager.getInstance().getNrCorrectQuestions());
      String nrTotalQuestionsString = String.valueOf(GameManager.getInstance().getNrTotalQuestions());
      infoString += "Gute Antworten: " + nrCorrectAnswersString + "/" + nrTotalQuestionsString;

      // Set the displayed information
      info.setText(infoString);
    }
    // Test failed ( Time is up )
    else if (GameManager.getInstance().getGame_over_status() == GAME_OVER_STATUS.TIME_UP) {
      finalPane.getStyleClass().add("failed");
      title.setText("Erfolgloses Versuch");
      String infoString = "Kein mehr Zeit\n";

      // Number of correctly answered questions
      int nrCorrectAnswers = GameManager.getInstance().getNrCorrectQuestions();
      String nrCorrectAnswersString = String.valueOf(nrCorrectAnswers);

      // Number of incorrectly answered questions
      int nrIncorrectAnswers = GameManager.getInstance().getNrIncorrectQuestions();

      int nrAnsweredQuestions = nrCorrectAnswers + nrIncorrectAnswers;
      String nrAnsweredQuestionsString = String.valueOf(nrAnsweredQuestions);
      infoString += "Gute Antworten: " + nrCorrectAnswersString + "/" + nrAnsweredQuestionsString;

      info.setText(infoString);
    }
    // Test failed ( Too many incorrect answers )
    else if (GameManager.getInstance().getGame_over_status() == GAME_OVER_STATUS.FAILED_ANSWERS) {
      finalPane.getStyleClass().add("failed");
      title.setText("Erfolgloses Versuch");
      String infoString = "Zu viele schlechte Antworten\n";

      // Number of correctly answered questions
      String nrCorrectAnswersString = String.valueOf(GameManager.getInstance().getNrCorrectQuestions());
      infoString += "Gute Antworten: " + nrCorrectAnswersString;

      info.setText(infoString);
    }

    // restore initial game data in GameManager
    GameManager.getInstance().newGame();

    // Animate final scene
    new Pulse(finalPane).play();
  }

  /**
   * Loads the new test scene
   *
   * @throws IOException if scene was not loaded
   */
  @FXML
  public void newTest() throws IOException {
    // Current stage
    Stage stage = (Stage)finalPane.getScene().getWindow();
    // Wanted pane
    Pane questionPane = FXMLLoader.load(getClass().getResource("/scenes/question.fxml"));
    // Wanted scene
    Scene questionScene = new Scene(questionPane);
    // Setting new stage
    stage.setScene(questionScene);
    // Display the question scene
    stage.show();
  }

  /**
   * Exits the application
   */
  @FXML
  public void exit() {
    System.exit(0);
  }
}
