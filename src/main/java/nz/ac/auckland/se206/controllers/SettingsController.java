package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TimeManager;

/**
 * Controller class for the fxml file settings.fxml. Attribution: All images have been generated
 * through OpenArt Creative 2023, created by the developers, or falls into CC0 unless otherwise
 * stated below. unless otherwise stated below.
 */
public class SettingsController implements TimeManager.TimeUpdateListener {

  private static TimeManager timeManager;

  /**
   * Static method that returns the timer instance.
   *
   * @return timer
   */
  public static TimeManager getTimeManager() {
    return timeManager;
  }

  @FXML private Button btnExitGame;
  @FXML private Button btnSounds;
  @FXML private Button btnTextToSpeech;
  @FXML private Text txtBack;
  @FXML private Label timerLblSettings;

  /**
   * Initialises the setting scene when called.
   *
   * @throws IOException exception
   */
  public void initialize() throws IOException {
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

    if (GameState.isSoundEnabled == false) {
      btnSounds.setText("Off");
    }

    if (GameState.isTextToSpeechEnabled == false) {
      btnTextToSpeech.setText("Off");
    }
  }

  /**
   * Handles the MouseEvent 'on Mouse Clicked' on the text txtBack.
   *
   * @param event mouse event
   */
  @FXML
  private void onGoBack(MouseEvent event) {
    if (GameState.currentRoom.equals("dragon")) {
      App.setUi(AppUi.DRAGON_ROOM);
    } else if (GameState.currentRoom.equals("lab")) {
      App.setUi(AppUi.LAB);
    } else if (GameState.currentRoom.equals("forest")) {
      App.setUi(AppUi.FOREST);
    } else if (GameState.currentRoom.equals("matchGame")) {
      App.setUi(AppUi.MATCHING);
    }
  }

  /**
   * Handles the ActionEvent on the button btnSounds.
   *
   * @param event action event
   */
  @FXML
  private void onSoundClicked(ActionEvent event) {
    if (GameState.isSoundEnabled == true) {
      GameState.isSoundEnabled = false;
      btnSounds.setText("Off");
      MenuController.stopMusic();
    } else {
      GameState.isSoundEnabled = true;
      btnSounds.setText("On");
      MenuController.player.play();
    }
  }

  /**
   * Handles the ActionEvent on the button btnTextToSpeech.
   *
   * @param event action event
   */
  @FXML
  private void onTextToSpeechClick(ActionEvent event) {
    if (GameState.isTextToSpeechEnabled == true) {
      GameState.isTextToSpeechEnabled = false;
      btnTextToSpeech.setText("Off");
    } else {
      GameState.isTextToSpeechEnabled = true;
      btnTextToSpeech.setText("On");
    }
  }

  /**
   * Handles the ActionEvent on the button btnExitGame.
   *
   * @param event action event
   */
  @FXML
  private void onExitGame(ActionEvent event) {
    System.exit(0);
  }

  // .
  /**
   * Updates timer label according to the current time that has passed.
   *
   * @param formattedTime the formatted time to display
   */
  @Override
  public void onTimerUpdate(String formattedTime) {
    Platform.runLater(() -> timerLblSettings.setText(formattedTime));
    // when time is up, show an alert that they have lost
    if (formattedTime.equals("00:01")) {

      LoseController.setItemCounter();

      timerLblSettings.setText("00:00");
    }
  }
}
