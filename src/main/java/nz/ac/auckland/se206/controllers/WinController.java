package nz.ac.auckland.se206.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class WinController {

  @FXML private Button buttonRetryWin;
  @FXML private Button buttonCloseWin;
  @FXML private Label winTitle;

  @FXML
  public void initialize() {
    // Create a fade in transition for the title
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), winTitle);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  @FXML
  public void onCloseClicked() {
    System.exit(0);
  }

  @FXML
  public void onRetryClicked() {
    // Implement code that resets game
  }
}
