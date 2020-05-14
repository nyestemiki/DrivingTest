package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MenuController {

  @FXML
  private Pane startPane;

  /**
   * Initializes the app and loads the question scene
   *
   * @throws IOException if scene file could not be loaded
   */
  @FXML
  public void startApp() throws Exception {
    // Current stage
    Stage stage = (Stage)startPane.getScene().getWindow();
    // Wanted pane
    Pane questionPane = FXMLLoader.load(getClass().getResource("/scenes/question.fxml"));
    // Wanted scene
    Scene questionScene = new Scene(questionPane);
    // Setting new stage
    stage.setScene(questionScene);
    // Display the question scene
    stage.show();
  }
}
