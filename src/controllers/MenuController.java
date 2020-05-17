package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    Stage stage = (Stage)startPane.getScene().getWindow();
    Pane questionPane = FXMLLoader.load(getClass().getResource("/scenes/question.fxml"));
    Scene questionScene = new Scene(questionPane);
    stage.setScene(questionScene);
    stage.show();
  }

  public void setPreferences() throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/scenes/preferences_menu.fxml"));
    Stage stage = new Stage();
    stage.setTitle("Präferenzen");
    stage.setScene(new Scene(root));
    stage.setResizable(false);
    stage.show();
  }
}
