package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Preferences;

public class PreferencesController {
    @FXML public Slider timeSlider;
    @FXML public Slider questionCountSlider;
    @FXML public Slider falseCountSlider;
    @FXML public Pane preferencesPane;
    @FXML public Label timeLabel;
    @FXML public Label questionCountLabel;
    @FXML public Label falseCountLabel;

    @FXML
    public void initialize() {
        timeChanged();
        questionCountChanged();
        falseCountChanged();
    }

    @FXML
    public void cancel() {
        ((Stage)preferencesPane.getScene().getWindow()).close();
    }

    @FXML
    public void save() {
        Preferences preference = new Preferences((int)timeSlider.getValue(), (int)questionCountSlider.getValue(),
                java.lang.Math.min((int)falseCountSlider.getValue(), (int)questionCountSlider.getValue()));

        GameManager.getInstance().setPreferences((preference));

        System.out.println(preference);

        ((Stage)preferencesPane.getScene().getWindow()).close();
    }

    @FXML
    public void timeChanged() {
        timeLabel.setText(String.valueOf((int)timeSlider.getValue() / 60));
    }

    @FXML
    public void questionCountChanged() {
        questionCountLabel.setText(String.valueOf((int)questionCountSlider.getValue()));
    }

    @FXML
    public void falseCountChanged() {
        falseCountLabel.setText(String.valueOf((int)falseCountSlider.getValue()));
    }
}
