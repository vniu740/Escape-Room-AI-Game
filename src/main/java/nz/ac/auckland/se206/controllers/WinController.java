package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class WinController {

  @FXML private Button buttonRetryWin;
  @FXML private Button buttonCloseWin;
  @FXML private Label winTitle;
  @FXML private ProgressIndicator progressIndicator;

  @FXML
  public void initialize() {
    // Create a fade in transition for the title
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), winTitle);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  @FXML
  private void onCloseClicked() {
    System.exit(0);
  }

  @FXML
  private void onRetryClicked() throws IOException {
    //stop music 
    MenuController.stopMusic();
    progressIndicator.setVisible(true);
       // another thread to restart the game
    Thread restartApp =
      new Thread(
        new Runnable() {
          @Override
            public void run() {
             try {
             // Thread.sleep(1000);
               App.restartGame();
                } catch (IOException e) {
               // TODO Auto-generated catch block
                e.printStackTrace();
                   }
                 }
               });
       restartApp.start();
   
   }
  }

