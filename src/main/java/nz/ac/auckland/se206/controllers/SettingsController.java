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

public class SettingsController implements TimeManager.TimeUpdateListener{

  private static TimeManager timeManager;

  public static TimeManager getTimeManager() {
    return timeManager;
  }

  @FXML private Button btnExitGame;
  @FXML private Button btnSounds;
  @FXML private Button btnTextToSpeech;
  @FXML private Text txtBack;
  @FXML private Label timerLblSettings;

   // Initialize the game and set up event handlers
  public void initialize() throws IOException {
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);
  }

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
