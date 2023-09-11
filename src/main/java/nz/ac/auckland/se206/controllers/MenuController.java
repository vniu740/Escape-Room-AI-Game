package nz.ac.auckland.se206.controllers;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MenuController {
  @FXML Button btnEasyMode;
  @FXML Button btnMediumMode;
  @FXML Button btnHardMode;
  @FXML Button btnPlay;
  @FXML ToggleButton toggleButtonTwoMins;
  @FXML ToggleButton toggleButtonFourMins;
  @FXML ToggleButton toggleButtonSixMins;
  @FXML Pane pnDifficultyMenu;
  @FXML ImageView imgViewDifficultyMenu;
  @FXML Image imgEasy;
  @FXML Image imgMedium;
  @FXML Image imgHard;

  private boolean isHidden;
  private boolean isEasyHidden;
  private boolean isMediumHidden;
  private boolean isHardHidden;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    isHidden = true;
    isEasyHidden = true;
    isMediumHidden = true;
    isHardHidden = true;
    pnDifficultyMenu.setLayoutY(500);
    imgEasy = new Image("images/easy.png");
    imgMedium = new Image("images/medium.png");
    imgHard = new Image("images/hard.png");
  }

  @FXML
  private void onEasyClick(ActionEvent event) {
    if (isMediumHidden == false) {
      translateDown();
      isMediumHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgEasy));
      delay(200, () -> translateUp());
      isEasyHidden = false;
    } else if (isHardHidden == false) {
      translateDown();
      isHardHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgEasy));
      delay(200, () -> translateUp());
      isEasyHidden = false;
    } else if (isHidden == true) {
      imgViewDifficultyMenu.setImage(imgEasy);
      translateUp();
      isHidden = false;
      isEasyHidden = false;
    } else {
      translateDown();
      isHidden = true;
    }
  }

  @FXML
  private void onMediumClick(ActionEvent event) {
    if (isEasyHidden == false) {
      translateDown();
      isEasyHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgMedium));
      delay(200, () -> translateUp());
      isMediumHidden = false;
    } else if (isHardHidden == false) {
      translateDown();
      isHardHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgMedium));
      delay(200, () -> translateUp());
      isMediumHidden = false;
    } else if (isHidden == true) {
      imgViewDifficultyMenu.setImage(imgMedium);
      translateUp();
      isHidden = false;
      isMediumHidden = false;
    } else {
      translateDown();
      isHidden = true;
    }
  }

  @FXML
  private void onHardClick(ActionEvent event) {
     if (isEasyHidden == false) {
      translateDown();
      isEasyHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgHard));
      delay(200, () -> translateUp());
      isHardHidden = false;
    } else if (isMediumHidden == false) {
      translateDown();
      isMediumHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgHard));
      delay(200, () -> translateUp());
      isHardHidden = false;
    } else if (isHidden == true) {
      imgViewDifficultyMenu.setImage(imgHard);
      translateUp();
      isHidden = false;
      isHardHidden = false;
    } else {
      translateDown();
      isHidden = true;
    }
  }

  @FXML
  private void onPlayClick(ActionEvent event) {}

  @FXML
  private void onTwoMinsClick(ActionEvent event) {}

  @FXML
  private void onFourMinsClick(ActionEvent event) {}

  @FXML
  private void onSixMinsClick(ActionEvent event) {}

  private void translateUp() {
    TranslateTransition transition =
        new TranslateTransition(Duration.seconds(0.3), pnDifficultyMenu);
    transition.setToY(-320);
    transition.play();
  }

  private void translateDown() {
    TranslateTransition transition =
        new TranslateTransition(Duration.seconds(0.3), pnDifficultyMenu);
    transition.setToY(100);
    transition.play();
  }

  /**
   * Helper method that delays the call of a runnable.
   *
   * @param time How long the delay will be
   * @param continuation the runnable that will be called after the delay
   */
  private void delay(int time, Runnable continuation) {
    Task<Void> sleep =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            try {
              Thread.sleep(time);
            } catch (InterruptedException e) {
              return null;
            }
            return null;
          }
        };
    sleep.setOnSucceeded(event -> continuation.run());
    Thread sleepThread = new Thread(sleep, "Sleep Thread");
    sleepThread.start();
  }
}
