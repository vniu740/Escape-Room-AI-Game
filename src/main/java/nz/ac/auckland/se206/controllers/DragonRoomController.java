package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.ImagePulseAnimation;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TimeManager;

public class DragonRoomController implements TimeManager.TimeUpdateListener {
  @FXML private ImageView imageLock;
  @FXML private ImageView imageScale;
  @FXML
  private ImageView imgViewLeftArrow;
  @FXML
  private Label timerLblDragon;
  private static TimeManager timeManager;

  @FXML
  public void initialize() throws IOException {
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

    // Create a new thread for the animation
    Thread animationThread = new Thread(
        () -> {
          ImagePulseAnimation imageAnimation = new ImagePulseAnimation(imageLock);
          ImagePulseAnimation image2Animation = new ImagePulseAnimation(imageScale);
          ImagePulseAnimation leftArrowAnimation = new ImagePulseAnimation(imgViewLeftArrow);
          imageAnimation.playAnimation();
          image2Animation.playAnimation();
          leftArrowAnimation.playAnimation();
        });

    // Start the animation thread
    animationThread.start();
  }

  // .
  /**
  * Updates timer label according to the current time that has passed.
  *
  * @param formattedTime the formatted time to display
  */
  @Override
  public void onTimerUpdate(String formattedTime) {
    Platform.runLater(() -> timerLblDragon.setText(formattedTime));
    //when time is up, show an alert that they have lost 
    if (formattedTime.equals("00:00")) {
      //Platform.runLater(() -> showDialog("Game Over", "You have run out of time!", "You have ran out of time!"));
      timerLblDragon.setText("00:00");
    }
  }

  public static TimeManager getTimeManager() {
    return timeManager;
  }

  @FXML
  private void onLockClicked() {
    App.setUi(AppUi.MATCHING);
  }

  @FXML
  private void onScaleClicked() {
    if (GameState.isMatchGameWon) {
      GameState.isScaleCollected = true;
      imageScale.setVisible(false);
    }
  }

  /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewLeftArrow.
   * 
   * @param event
   */
    @FXML private void onLeftArrowClicked(MouseEvent event) {
    // Switch to the lab
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.LAB));
  }

}
/**
 * Attribution: imageLock: <a
 * href="https://www.freepik.com/free-vector/set-lockpad-icon_4150202.htm#query=cartoon%20lock&position=16&from_view=keyword&track=ais">Image
 * by brgfx</a> on Freepik
 */
