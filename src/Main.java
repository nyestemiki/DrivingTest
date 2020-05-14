import animatefx.animation.FadeIn;
import controllers.GameManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Loads the main menu scene
     *
     * @param primaryStage is the stage that is being set
     * @throws Exception if stage could not be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loading the main menu scene
        Parent root = FXMLLoader.load(getClass().getResource("scenes/main_menu.fxml"));
        primaryStage.setTitle("Führerschein Theorieprüfung");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Animate the stage
        new FadeIn(root).play();
    }

    /**
     * Stop any ongoing timer if app was interrupted
     */
    @Override
    public void stop() {
        GameManager.getInstance().stopTimer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
