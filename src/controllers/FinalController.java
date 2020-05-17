package controllers;

import models.Question;
import utils.Export;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import animatefx.animation.Pulse;

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

  private List<Question> completed; // Questions and answers from the latest test

  /**
   * Adjusts final scene to successful/failed prepares new test
   */
  @FXML
  public void initialize() {
    completed = MainController.getCurrentQuestionSet();

    GameManager.getInstance().stopTimer();

    loadSuccessfulOrFailed();

    GameManager.getInstance().newGame();

    new Pulse(finalPane).play();
  }

  /**
   * Adjust the scene to the status of the test
   */
  private void loadSuccessfulOrFailed() {
    switch (GameManager.getInstance().getGame_over_status()) {
      case SUCCESSFUL:
        finalPane.getStyleClass().add("successful");
        title.setText("Erfolgreiches Versuch");
        info.setText(remainingTimeString() + "\n" + correctOverTotal());
        break;
      case TIME_UP:
        finalPane.getStyleClass().add("failed");
        title.setText("Erfolgloses Versuch");
        info.setText("Kein mehr Zeit\n" + correctOverAnswered());
        break;
      case FAILED_ANSWERS:
        finalPane.getStyleClass().add("failed");
        title.setText("Erfolgloses Versuch");
        info.setText("Zu viele schlechte Antworten\nGute Antworten: " + GameManager.getNrCorrectQuestions());
        break;
    }
  }

  /**
   * Returns the time mm:ss remaining
   *
   * @return the remaining time
   */
  public String remainingTimeString() {
    int timeRemaining = GameManager.getTimeRemaining();
    int minutes = timeRemaining / 60;
    int seconds = timeRemaining % 60;

    // Display 0x for minutes and seconds smaller than 10
    String minutesString = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);
    String secondsString = (seconds < 10) ? "0" + seconds : String.valueOf(seconds);

    return "Zeit übrig: " + minutesString + ":" + secondsString;
  }

  /**
   * Returns the correct/total answers quota
   *
   * @return success rate
   */
  public String correctOverTotal() {
    return "Gute Antworten: " + GameManager.getNrCorrectQuestions() + "/" + GameManager.getNrTotalQuestions();
  }

  /**
   * Returns the correct/answered quota
   *
   * @return success rate
   */
  public String correctOverAnswered() {
    return "Gute Antworten: " + GameManager.getNrCorrectQuestions() + "/" +
            GameManager.getNrCorrectQuestions() + GameManager.getNrIncorrectQuestions();
  }

  /**
   * Loads the new test scene
   */
  @FXML
  public void newTest() {
    try {
      Stage stage = (Stage)finalPane.getScene().getWindow();
      Pane questionPane = FXMLLoader.load(getClass().getResource("/scenes/question.fxml"));
      stage.setScene(new Scene(questionPane));

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Exits the application window
   */
  @FXML
  public void exit() {
    System.exit(0);
  }

  /**
   * Opens the a new window to set preferences
   */
  @FXML
  public void setPreferences() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/scenes/preferences_menu.fxml"));
      Stage stage = new Stage();
      stage.setTitle("Präferenzen");
      stage.setScene(new Scene(root));
      stage.setResizable(false);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates a report (html)
   */
  public void export() {
    new Export(completed).generate();
  }
}
