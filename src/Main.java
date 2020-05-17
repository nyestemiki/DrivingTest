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
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("scenes/main_menu.fxml"));
            primaryStage.setTitle("Führerschein Theorieprüfung");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);

            primaryStage.show();

            new FadeIn(root).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
