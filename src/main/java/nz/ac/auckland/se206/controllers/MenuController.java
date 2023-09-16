package nz.ac.auckland.se206.controllers;

import java.util.Timer;

import javafx.animation.TranslateTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {
  @FXML Button btnEasyMode;
  @FXML Button btnMediumMode;
  @FXML Button btnHardMode;
  @FXML Button btnPlay;
  @FXML ToggleButton toggleBtnTwoMins;
  @FXML ToggleButton toggleBtnFourMins;
  @FXML ToggleButton toggleBtnSixMins;
  @FXML Pane pnDifficultyMenu;
  @FXML ImageView imgViewDifficultyMenu;
  @FXML Image imgEasy;
  @FXML Image imgMedium;
  @FXML Image imgHard;

  private boolean isHidden;
  private boolean isEasyHidden;
  private boolean isMediumHidden;
  private boolean isHardHidden;

  private boolean isSelected;
  private boolean isTwoMinsSelected;
  private boolean isFourMinsSelected;
  private boolean isSixMinsSelected;

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Set all the boolean variables for the buttons
    isHidden = true;
    isEasyHidden = true;
    isMediumHidden = true;
    isHardHidden = true;
    isSelected = false;
    isTwoMinsSelected = false;
    isFourMinsSelected = false;
    isSixMinsSelected = false;

    // Move the difficulty menu so it cannot be seen when opening
    pnDifficultyMenu.setLayoutY(500);

    // Create difficulty images
    imgEasy = new Image("images/easy.png");
    imgMedium = new Image("images/medium.png");
    imgHard = new Image("images/hard.png");
  }

  /**
   * Handles the ActionEvent on the button btnEasyMode.
   * 
   * @param event
   */
  @FXML
  private void onEasyClick(ActionEvent event) {
    if (isMediumHidden == false) {
      // If the medium menu is visible, transition it down, and translate the easy menu up
      translateDown();
      isMediumHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgEasy));
      delay(200, () -> translateUp());
      isEasyHidden = false;
    } else if (isHardHidden == false) {
      // If the hard menu is visible, transition it down, and translate the easy menu up
      translateDown();
      isHardHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgEasy));
      delay(200, () -> translateUp());
      isEasyHidden = false;
    } else if (isHidden == true) {
      // If no menu is visible, translate the easy menu up
      imgViewDifficultyMenu.setImage(imgEasy);
      translateUp();
      isHidden = false;
      isEasyHidden = false;
    } else {
      // If the easy menu is visible, translate it down
      translateDown();
      isHidden = true;
    }
  }
  /**
   * Handles the ActionEvent on the button btnMediumMode.
   * 
   * @param event
   */
  @FXML private void onMediumClick(ActionEvent event) {
    if (isEasyHidden == false) {
      // If the easy menu is visible, transition it down, and translate the medium menu up
      translateDown();
      isEasyHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgMedium));
      delay(200, () -> translateUp());
      isMediumHidden = false;
    } else if (isHardHidden == false) {
      // If the hard menu is visible, transition it down, and translate the medium menu up
      translateDown();
      isHardHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgMedium));
      delay(200, () -> translateUp());
      isMediumHidden = false;
    } else if (isHidden == true) {
      // If no menu is visible, translate the medium menu up
      imgViewDifficultyMenu.setImage(imgMedium);
      translateUp();
      isHidden = false;
      isMediumHidden = false;
    } else {
      // If the medium menu is visible, translate it down
      translateDown();
      isHidden = true;
    }
  }

  /**
   * Handles the ActionEvent on the button btnHardMode.
   * 
   * @param event
   */
  @FXML private void onHardClick(ActionEvent event) {
    if (isEasyHidden == false) {
      // If the easy menu is visible, transition it down, and translate the hard menu up
      translateDown();
      isEasyHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgHard));
      delay(200, () -> translateUp());
      isHardHidden = false;
    } else if (isMediumHidden == false) {
      // If the medium menu is visible, transition it down, and translate the hard menu up
      translateDown();
      isMediumHidden = true;
      delay(300, () -> imgViewDifficultyMenu.setImage(imgHard));
      delay(200, () -> translateUp());
      isHardHidden = false;
    } else if (isHidden == true) {
      // If no menu is visible, translate the hard menu up
      imgViewDifficultyMenu.setImage(imgHard);
      translateUp();
      isHidden = false;
      isHardHidden = false;
    } else {
      // If the hard menu is visible, translate it down
      translateDown();
      isHidden = true;
    }
  }
  /**
   * Handles the ActionEvent on the toggle button toggleBtnTwoMins.
   * 
   * @param event
   */
  @FXML
  private void onTwoMinsClick(ActionEvent event) {
    //get instance of time manager for forest room and start timer
    ForestRoomController.getTimeManager().startTimer(121);
    LabController.getTimeManager().startTimer(121);
    DragonRoomController.getTimeManager().startTimer(121);  
    
    if (isFourMinsSelected == true) {
      // If the four mins button is selected, deselect it, and select the two mins button
      toggleBtnFourMins.setSelected(false);
      toggleBtnTwoMins.setSelected(true);
      isFourMinsSelected = false;
      isTwoMinsSelected = true;
      //start timer of timer manager by getting the instance 
      //TimeManager.getInstance().startTimer();
     

      
    } else if (isSixMinsSelected == true) {
      // If the six mins button is selected, deselect it, and select the two mins button
      toggleBtnSixMins.setSelected(false);
      toggleBtnTwoMins.setSelected(true);
      isSixMinsSelected = false;
      isTwoMinsSelected = true;
    } else if (isSelected == false) {
      // If no button is selected, select the two mins button
      toggleBtnTwoMins.setSelected(true);
      isSelected = true;
      isTwoMinsSelected = true;
      // Enable the play button
      btnPlay.setDisable(false);
    } else {
      // If the two mins button is selected, deselect it
      isTwoMinsSelected = false;
      toggleBtnTwoMins.setSelected(false);
      isSelected = false;
      // Disable the play button
      btnPlay.setDisable(true);
    }
  }

  /**
   * Handles the ActionEvent on the toggle button toggleBtnFourMins.
   * 
   * @param event
   */
  @FXML
  private void onFourMinsClick(ActionEvent event) {
    ForestRoomController.getTimeManager().startTimer(241);
    LabController.getTimeManager().startTimer(241);
    DragonRoomController.getTimeManager().startTimer(241);
    if (isTwoMinsSelected == true) {
      // If the two mins button is selected, deselect it, and select four two mins button
      toggleBtnFourMins.setSelected(true);
      toggleBtnTwoMins.setSelected(false);
      isTwoMinsSelected = false;
      isFourMinsSelected = true;
    } else if (isSixMinsSelected == true) {
      // If the six mins button is selected, deselect it, and select four two mins button
      toggleBtnSixMins.setSelected(false);
      toggleBtnFourMins.setSelected(true);
      isSixMinsSelected = false;
      isFourMinsSelected = true;
    } else if (isSelected == false) {
      // If no button is selected, select the four mins button
      toggleBtnFourMins.setSelected(true);
      isSelected = true;
      isFourMinsSelected = true;
      // Enable the play button
      btnPlay.setDisable(false);
    } else {
      // If the four mins button is selected, deselect it
      isFourMinsSelected = false;
      toggleBtnFourMins.setSelected(false);
      isSelected = false;
      // Disable the play button
      btnPlay.setDisable(true);
    }
  }

  /**
   * Handles the ActionEvent on the toggle button toggleBtnFourMins.
   * 
   * @param event
   */
  @FXML
  private void onSixMinsClick(ActionEvent event) {
    ForestRoomController.getTimeManager().startTimer(361);
    LabController.getTimeManager().startTimer(361);
    DragonRoomController.getTimeManager().startTimer(361);
    if (isTwoMinsSelected == true) {
      // If the two mins button is selected, deselect it, and select six two mins button
      toggleBtnTwoMins.setSelected(false);
      toggleBtnSixMins.setSelected(true);
      isTwoMinsSelected = false;
      isSixMinsSelected = true;
    } else if (isFourMinsSelected == true) {
      // If the four mins button is selected, deselect it, and select six two mins button
      toggleBtnFourMins.setSelected(false);
      toggleBtnSixMins.setSelected(true);
      isFourMinsSelected = false;
      isSixMinsSelected = true;
    } else if (isSelected == false) {
      // If no button is selected, select the six mins button
      toggleBtnSixMins.setSelected(true);
      isSelected = true;
      isSixMinsSelected = true;
      // Enable the play button
      btnPlay.setDisable(false);
    } else {
      // If the six mins button is selected, deselect it
      isSixMinsSelected = false;
      toggleBtnSixMins.setSelected(false);
      isSelected = false;
      // Disable the play button
      btnPlay.setDisable(true);
    }
  }

  @FXML
  private void onPlayClick(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUi(AppUi.LAB));
  }

  /**
   * Helper method that translates the pane pnDifficultyMenu upwards.
   * 
   */
  private void translateUp() {
    // Create and play a transition that will move the difficulty menu pane upwards
    TranslateTransition transition =
        new TranslateTransition(Duration.seconds(0.3), pnDifficultyMenu);
    transition.setToY(-370);
    transition.play();
  }

  /**
   * Helper method that translates the pane pnDifficultyMenu downwards.
   * 
   */
  private void translateDown() {
    // Create and play a transition that will move the difficulty menu pane downwards
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
