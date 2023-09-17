package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class LoseController {

  @FXML private Button buttonRetry;
  @FXML private Button buttonClose;
  @FXML private Label itemCounter;
  @FXML private Label topTitle;
  @FXML private Label botTitle;

  @FXML
  public void initialize() {
    itemCounter.setText(Integer.toString(GameState.itemsCollected));

    // Create a fade in transition for the title
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), topTitle);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();

    FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(3.5), botTitle);
    fadeIn2.setFromValue(0.0);
    fadeIn2.setToValue(1.0);
    fadeIn2.play();
  }

  @FXML
  public void onCloseClicked() {
    System.exit(0);
  }

  @FXML
  public void onRetryClicked() throws IOException {
    // Reset the game state
    App.restartGame();
  }
}
