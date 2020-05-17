package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Preferences;

public class PreferencesController {
    @FXML
    public Slider timeSlider;
    @FXML
    public Slider questionCountSlider;
    @FXML
    public Slider falseCountSlider;
    @FXML
    public Pane preferencesPane;
    @FXML
    public Label timeLabel;
    @FXML
    public Label questionCountLabel;
    @FXML
    public Label falseCountLabel;

    @FXML
    public void initialize() {
        timeChanged();
        questionCountChanged();
        falseCountChanged();
    }

    /**
     * Closes the preferences window without modifying them
     */
    @FXML
    public void cancel() {
        ((Stage)preferencesPane.getScene().getWindow()).close();
    }

    /**
     * Sets the new preferences and closes the preferences window
     */
    @FXML
    public void save() {
        Preferences preference = new Preferences((int)timeSlider.getValue(), (int)questionCountSlider.getValue(),
                java.lang.Math.min((int)falseCountSlider.getValue(), (int)questionCountSlider.getValue()));

        GameManager.getInstance().setPreferences((preference));

        ((Stage)preferencesPane.getScene().getWindow()).close();
    }

    /**
     * Updates the label with the new value
     */
    @FXML
    public void timeChanged() {
        timeLabel.setText(String.valueOf((int)timeSlider.getValue() / 60));
    }

    /**
     * Updates the label with the new value
     */
    @FXML
    public void questionCountChanged() {
        questionCountLabel.setText(String.valueOf((int)questionCountSlider.getValue()));
    }

    /**
     * Updates the label with the new value
     */
    @FXML
    public void falseCountChanged() {
        falseCountLabel.setText(String.valueOf((int)falseCountSlider.getValue()));
    }
}
