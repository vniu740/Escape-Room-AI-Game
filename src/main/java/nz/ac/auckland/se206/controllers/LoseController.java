package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class LoseController {

  @FXML private Button buttonRetry;
  @FXML private Button buttonClose;
  @FXML private static Label itemCounter;
  @FXML private Label topTitle;
  @FXML private Label botTitle;
  @FXML
  private Pane loseBackground;
  @FXML
  private ProgressIndicator progressIndicator; 

  @FXML
  public void initialize() {

    // Set the item counter
    itemCounter = new Label();
    // Add itemCounter to loseBackground
    loseBackground.getChildren().add(itemCounter);
    // Set font to Lucida Calligraphy
    itemCounter.setStyle("-fx-font-family: 'Lucida Calligraphy'; -fx-font-size: 80px;");
    // Set the text to 0
    itemCounter.setText(Integer.toString(GameState.itemsCollected));
    // Have itemCounter in the middle of the screen
    itemCounter.setLayoutX(263);
    itemCounter.setLayoutY(200);
    // Set width to 53
    itemCounter.setMinWidth(53);
    // Set height to 123
    itemCounter.setMinHeight(123);

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
    //progressIndictor to be visible 
    progressIndicator.setVisible(true);
    //another thread to restart the game
    Thread restartApp = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          //Thread.sleep(1000);
          App.restartGame();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
    restartApp.start();

    //App.restartGame();
  }

  @FXML
  public static void setItemCounter() {
    itemCounter.setText(Integer.toString(GameState.itemsCollected));
  }
}
