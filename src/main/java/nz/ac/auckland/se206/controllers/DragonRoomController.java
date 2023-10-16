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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.ImagePulseAnimation;
import nz.ac.auckland.se206.PotionManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * Controller class for the fxml file dragonRoom.fxml. Attribution: All images have been generated
 * through OpenArt Creative 2023, created by the developers, or falls into CC0 unless otherwise
 * stated below.
 *
 * <p>imageLock: <a
 * href="https://www.freepik.com/free-vector/set-lockpad-icon_4150202.htm#query=cartoon%20lock&position=16&from_view=keyword&track=ais">Image
 * by brgfx</a> on Freepik.
 */
public class DragonRoomController implements TimeManager.TimeUpdateListener {

  private static TimeManager timeManager;
  @FXML static Label hintCounter;

  public static TimeManager getTimeManager() {
    return timeManager;
  }

  // FXML elements
  @FXML private Button btnSpeechExit;
  @FXML private ImageView imageLock;
  @FXML private ImageView imageScale;
  @FXML private ImageView imgViewIconScroll;
  @FXML private ImageView imgViewWizard;
  @FXML private ImageView imgViewLeftArrow;
  @FXML private HBox scrollBox;
  @FXML private Label timerLblDragon;
  @FXML private Pane pnScroll;
  @FXML private Pane pnSpeech;
  @FXML private Pane dragonPane;
  @FXML private Text txtSpeech;

  /**
   * Initialises the forest scene when called.
   *
   * @throws IOException exception
   */
  @FXML
  public void initialize() throws IOException {
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

    // Get the correct ingredient of all possible dragon images instantiated in LabController
    List<Image> dragonImageList = PotionManager.getDragonObjectList();
    Image correctIngredient = dragonImageList.get(0);
    // Set image to correct ingredient
    imageScale.setImage(correctIngredient);

    Tooltip lockTooltip = new Tooltip("Lock");
    lockTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imageLock, lockTooltip);

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

    // Add hintCounter
    hintCounter = new Label();
    // set the text colour to #ad1cad
    hintCounter.setTextFill(Color.web("#ad1cad"));
    // set styles
    hintCounter.setStyle(
        "-fx-font-size: 18px; "
            + "-fx-font-weight: bold; "
            + "-fx-font-family: 'lucida calligraphy'; "
            + "-fx-font-style: italic; "
            + "-fx-underline: true;");
    // set the layout
    hintCounter.setLayoutX(130);
    hintCounter.setLayoutY(56);
    // add the hintCounter to the dragonPane
    dragonPane.getChildren().add(hintCounter);

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

  @FXML
  private void onLockClicked() {
    // Remove the wizard speech bubble
    pnSpeech.setVisible(false);
    //Change the scene to the matchGame
    GameState.currentRoom = "matchGame";
    App.setUi(AppUi.MATCHING);

    if (GameState.isTextToSpeechEnabled == true) {
      // Read out the instructions for the match game
      Thread speachThread =
          new Thread(
              () -> {
                TextToSpeech textToSpeech = new TextToSpeech();
                textToSpeech.speak("match 3 items to unlock the shelf");
              });
      speachThread.start();
    }
  }

  @FXML
  private void onScaleClicked() {
    if (GameState.isMatchGameWon) {
      // If the game is won play the textToSpeech
      GameState.isScaleCollected = true;
      GameState.itemsCollected++;
      // Remove the wizard speech bubble and remove the item
      imageScale.setVisible(false);
      pnSpeech.setVisible(false);
    } else {
      pnSpeech.setVisible(true);
      if (GameState.isTextToSpeechEnabled == true) {
        Thread speachThread =
            new Thread(
                () -> {
                  TextToSpeech textToSpeech = new TextToSpeech();
                  textToSpeech.speak("You can't pick it up! Unlock the shelf first.");
                });
        speachThread.start();
      }
    }
  }



  /** Helper method that sets the ingredient images of the potion recipe. */
  private void setPotionRecipeImages() {
    int listCounter = 0;
    List<Image> imgScrollList = PotionManager.getImgScrollList();

    // Set each of the images to the imageViews in the HBox of the Pane pnScroll
    for (Node child : scrollBox.getChildren()) {
      if (child instanceof ImageView) {
        ImageView childImageView = (ImageView) child;
        childImageView.setImage(imgScrollList.get(listCounter));
      }
      listCounter++;
    }
  }



  /**
   * Handles the MouseEvent 'on Mouse Exited' for the imageView imgViewScrollIcon.
   *
   * @param event mouse event
   */
  @FXML
  private void onExitIconScroll(MouseEvent event) {
    pnScroll.setVisible(false);
  }

  /**
   * Handles the ActionEvent on the Button btnSpeechExit.
   *
   * @param event mouse event
   */
  @FXML
  private void onSpeechExit(ActionEvent event) {
    pnSpeech.setVisible(false);
  }

  @FXML
  private void onWizardClicked() {
    ChatController.setBackground();
    App.setUi(AppUi.AICHAT);
  }

    /**
   * Handles the MouseEvent 'on Mouse Entered' for the imageView imgViewScrollIcon.
   *
   * @param event mouse event
   */
  @FXML
  private void onEnterIconScroll(MouseEvent event) {
    pnScroll.setVisible(true);
  }

    /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewLeftArrow.
   *
   * @param event mouseEvent 'on Mouse Clicked'
   */
  @FXML
  private void onLeftArrowClicked(MouseEvent event) {
    // Switch to the lab
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.LAB));
    GameState.currentRoom = "lab";
  }
  
  /**
   * Handles the Mouse Event 'on Mouse Click' for the ImageView imgViewSettings.
   *
   * @param event mouse event
   */
  @FXML
  private void onSettingsClicked(MouseEvent event) {
    App.setUi(AppUi.SETTINGS);
  }
}
