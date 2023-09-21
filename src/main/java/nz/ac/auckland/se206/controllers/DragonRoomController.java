package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.ImagePulseAnimation;
import nz.ac.auckland.se206.PotionManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class DragonRoomController implements TimeManager.TimeUpdateListener {
  // FXML elements for the lock and scale
  @FXML private ImageView imageLock;
  @FXML private ImageView imageScale;

  // Timer and time manager
  @FXML private Label timerLblDragon;
  private static TimeManager timeManager;

  // FXML elements for scrolling
  @FXML private ImageView imgViewLeftArrow;
  @FXML private Pane pnScroll;
  @FXML private HBox hBoxScroll;
  @FXML private ImageView imgViewIconScroll;

  // Speech and wizard elements
  @FXML private Button btnSpeechExit;
  @FXML private Pane pnSpeech;
  @FXML private Text txtSpeech;
  @FXML private ImageView imgViewWizard;

  @FXML
  public void initialize() throws IOException {
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

    // Get the correct ingredient of all possible dragon images instantiated in LabController
    List<Image> dragonImageList = PotionManager.getDragonObjectList();
    Image correctIngredient = dragonImageList.get(0);
    // Set image to correct ingredient
    imageScale.setImage(correctIngredient);

    // Create a new thread for the animation
    Thread animationThread =
        new Thread(
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

    setPotionRecipeImages();
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
    // when time is up, show an alert that they have lost
    if (formattedTime.equals("00:01")) {

      LoseController.setItemCounter();
      timerLblDragon.setText("00:00");
    }
  }

  public static TimeManager getTimeManager() {
    return timeManager;
  }

  @FXML
  private void onLockClicked() {
    pnSpeech.setVisible(false);
    GameState.currentRoom = "matchGame";
    App.setUi(AppUi.MATCHING);
  }

  @FXML
  private void onScaleClicked() {
    if (GameState.isMatchGameWon) {
      // If the game is won play the textToSpeech
      Thread speachThread =
          new Thread(
              () -> {
                TextToSpeech textToSpeech = new TextToSpeech();
                textToSpeech.speak("Item picked up");
              });
      speachThread.start();
      GameState.isScaleCollected = true;
      GameState.itemsCollected++;
      // Remove the wizard speech bubble and remove the item
      imageScale.setVisible(false);
      pnSpeech.setVisible(false);
    } else {
      pnSpeech.setVisible(true);
    }
  }

  /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewLeftArrow.
   *
   * @param event
   */
  @FXML
  private void onLeftArrowClicked(MouseEvent event) {
    // Switch to the lab
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.LAB));
    GameState.currentRoom = "lab";
  }

  /** Helper method that sets the ingredient images of the potion recipe. */
  private void setPotionRecipeImages() {
    int listCounter = 0;
    List<Image> imgScrollList = PotionManager.getImgScrollList();

    // Set each of the images to the imageViews in the HBox of the Pane pnScroll
    for (Node child : hBoxScroll.getChildren()) {
      if (child instanceof ImageView) {
        ImageView childImageView = (ImageView) child;
        childImageView.setImage(imgScrollList.get(listCounter));
      }
      listCounter++;
    }
  }

  /**
   * Handles the MouseEvent 'on Mouse Entered' for the imageView imgViewScrollIcon
   *
   * @param event
   */
  @FXML
  private void onEnterIconScroll(MouseEvent event) {
    pnScroll.setVisible(true);
  }

  /**
   * Handles the MouseEvent 'on Mouse Exited' for the imageView imgViewScrollIcon.
   *
   * @param event
   */
  @FXML
  private void onExitIconScroll(MouseEvent event) {
    pnScroll.setVisible(false);
  }

  /**
   * Handles the ActionEvent on the Button btnSpeechExit.
   *
   * @param event
   */
  @FXML
  private void onSpeechExit(ActionEvent event) {
    pnSpeech.setVisible(false);
  }

  @FXML
  private void onWizardClicked() {
    AIChatController.setBackground();
    App.setUi(AppUi.AICHAT);
  }
}
/**
 * Attribution: imageLock: <a
 * href="https://www.freepik.com/free-vector/set-lockpad-icon_4150202.htm#query=cartoon%20lock&position=16&from_view=keyword&track=ais">Image
 * by brgfx</a> on Freepik
 */
