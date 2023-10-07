package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {

  private static Media song;

  static {
    try {
      song = new Media(App.class.getResource("/sounds/themeSound.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  static final MediaPlayer player = new MediaPlayer(song);

  // method for stopping the music
  public static void stopMusic() {
    player.stop();
  }

  private boolean isHidden;
  private boolean isEasyHidden;
  private boolean isMediumHidden;
  private boolean isHardHidden;

  private boolean isSelected;
  private boolean isTwoMinsSelected;
  private boolean isFourMinsSelected;
  private boolean isSixMinsSelected;

  private int timeRemaining;

  @FXML private Button btnEasyMode;
  @FXML private Button btnMediumMode;
  @FXML private Button btnHardMode;
  @FXML private Button btnPlay;
  @FXML private ToggleButton toggleBtnTwoMins;
  @FXML private ToggleButton toggleBtnFourMins;
  @FXML private ToggleButton toggleBtnSixMins;
  @FXML private Pane pnDifficultyMenu;
  @FXML private ImageView imgViewDifficultyMenu;
  @FXML private Image imgEasy;
  @FXML private Image imgMedium;
  @FXML private Image imgHard;

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
    try {
      song = new Media(App.class.getResource("/sounds/themeSound.mp3").toURI().toString());
      // final MediaPlayer player = new MediaPlayer(song);
      player.setCycleCount(MediaPlayer.INDEFINITE);
      // set volume
      player.setVolume(0.05);
      player.play();
    } catch (URISyntaxException e) {
    }
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
    // set the game state level to easy
    GameState.setLevel("easy");
    AIChatController.setHintCounter();
  }

  /**
   * Handles the ActionEvent on the button btnMediumMode.
   *
   * @param event
   */
  @FXML
  private void onMediumClick(ActionEvent event) {
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
    // set the game state level to medium
    GameState.setLevel("medium");
    AIChatController.setHintCounter();
  }

  /**
   * Handles the ActionEvent on the button btnHardMode.
   *
   * @param event
   */
  @FXML
  private void onHardClick(ActionEvent event) {
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
    // set the game state level to hard
    GameState.setLevel("hard");
    AIChatController.setHintCounter();

  }

  /**
   * Handles the ActionEvent on the toggle button toggleBtnTwoMins.
   *
   * @param event
   */
  @FXML
  private void onTwoMinsClick(ActionEvent event) {
    timeRemaining = 121;

    if (isFourMinsSelected == true) {
      // If the four mins button is selected, deselect it, and select the two mins button
      toggleBtnFourMins.setSelected(false);
      toggleBtnTwoMins.setSelected(true);
      isFourMinsSelected = false;
      isTwoMinsSelected = true;
      // start timer of timer manager by getting the instance
      // TimeManager.getInstance().startTimer();

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
    timeRemaining = 241;
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
    timeRemaining = 361;
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
    // start timer of timer manager by getting the instance
    ForestRoomController.getTimeManager().startTimer(timeRemaining);
    
  }

  /** Helper method that translates the pane pnDifficultyMenu upwards. */
  private void translateUp() {
    // Create and play a transition that will move the difficulty menu pane upwards
    TranslateTransition transition =
        new TranslateTransition(Duration.seconds(0.3), pnDifficultyMenu);
    transition.setToY(-370);
    transition.play();
  }

  /** Helper method that translates the pane pnDifficultyMenu downwards. */
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
    // Create a task that will sleep for the specified time
    Task<Void> sleep =
        new Task<Void>() {
          // Create a new task that uses a thread to simulate a delay
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
    // Run the input code after the given time passed
    sleep.setOnSucceeded(event -> continuation.run());
    Thread sleepThread = new Thread(sleep, "Sleep Thread");
    // Start the thread
    sleepThread.start();
  }
}

/**
 * Attribution:
 *
 * <p>All images have been generated through OpenArt Creative 2023 unless otherwise stated below.
 */
