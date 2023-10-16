package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the fxml file win.fxml. Attribution: All images have been generated through
 * OpenArt Creative 2023, created by the developers, or falls into CC0 unless otherwise stated
 * below.
 */
public class WinController {

  /**
   * Handles when the retry button is clicked.
   *
   * @throws IOException exception for reloading
   */
  private Task<Void> restartTask =
      new Task<>() {
        @Override
        protected Void call() throws Exception {
          App.restartGame();
          return null;
        }
      };

  @FXML private Button buttonRetryWin;
  @FXML private Button buttonCloseWin;
  @FXML private Label winTitle;
  @FXML private ProgressIndicator progressIndicator;

  /** Initialises the win scene when called. */
  @FXML
  public void initialize() {
    // Create a fade in transition for the title
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), winTitle);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  /** Handles when the close button is clicked. */
  @FXML
  private void onCloseClicked() {
    System.exit(0);
  }

  /**
   * Handles when the retry button is clicked.
   *
   * @throws IOException exception for reloading
   */
  @FXML
  private void onRetryClicked() throws IOException {

    // Disable retry button
    buttonRetryWin.setDisable(true);

    progressIndicator.setVisible(true);
    Thread th = new Thread(restartTask);

    th.setDaemon(true);

    th.start();
  }
}
