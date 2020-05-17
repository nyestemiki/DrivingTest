package controllers;

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
   */
  @FXML
  public void startApp() {
    try {
      Stage stage = (Stage)startPane.getScene().getWindow();
      Pane questionPane = FXMLLoader.load(getClass().getResource("/scenes/question.fxml"));
      stage.setScene(new Scene(questionPane));

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Opens a new window to set preferences
   */
  public void setPreferences() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/scenes/preferences_menu.fxml"));
      Stage stage = new Stage();
      stage.setScene(new Scene(root));

      stage.setTitle("Präferenzen");
      stage.setResizable(false);

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
