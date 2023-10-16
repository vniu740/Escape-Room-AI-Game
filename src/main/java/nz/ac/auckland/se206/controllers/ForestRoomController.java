package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
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
 * Controller class for the room view. Attribution: ll images have been generated through OpenArt
 * Creative 2023, created by the developers, or falls into CC0 unless otherwise stated below.
 */
public class ForestRoomController implements TimeManager.TimeUpdateListener {

  private static TimeManager timeManager;
  @FXML static Label hintCounter;

  /**
   * Method that returns the time manager instance.
   *
   * @return time manager instance
   */
  public static TimeManager getTimeManager() {
    return timeManager;
  }

  @FXML private Button btnSpeechExit;
  @FXML private Button switchScenes;
  @FXML private Button btnFishingExit;
  @FXML private HBox scrollBox;
  @FXML private Image correctIngredient;
  @FXML private ImageView imgViewSpiralPond;
  @FXML private ImageView imgViewSpiralFrog;
  @FXML private ImageView imgViewMushroom;
  @FXML private ImageView imgViewBug;
  @FXML private ImageView imgViewRightArrow;
  @FXML private ImageView imgViewIconScroll;
  @FXML private ImageView imgViewIngredient;
  @FXML private ImageView imgViewWizard;
  @FXML private ImageView correctImageView;
  @FXML private Label timerLbl;
  @FXML private Line threadOne;
  @FXML private Line threadTwo;
  @FXML private Line threadThree;
  @FXML private Pane pnFishing;
  @FXML private Pane pnFishingOpacity;
  @FXML private Pane sldOneDisablePane;
  @FXML private Pane sldTwoDisablePane;
  @FXML private Pane sldThreeDisablePane;
  @FXML private Pane pnScroll;
  @FXML private Pane pnSpeech;
  @FXML private Pane paneForest;
  @FXML private Rectangle door;
  @FXML private Rectangle window;
  @FXML private Rectangle vase;
  @FXML private Slider sldOne;
  @FXML private Slider sldTwo;
  @FXML private Slider sldThree;
  @FXML private Text txtSpeech;
  private TextToSpeech textToSpeech = new TextToSpeech();

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    // Initialization code goes here
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

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
    paneForest.getChildren().add(hintCounter);

    // Get the list of all possible forest images instantiated in LabController
    List<Image> uniqueImages = PotionManager.getForestObjectList();
    // Get the correct ingredient which was randomised and set to index 0 in LabController and
    // remove it
    correctIngredient = uniqueImages.remove(0);
    // Shuffle the list to randomise the incorrect ingredients
    Collections.shuffle(uniqueImages);
    // Make the list a size of 3 ingredients
    uniqueImages.subList(3, 6).clear();
    // Add the correct ingredient back into the list so that the two wrong ingredients are random
    // and the correct ingredient is in the list
    uniqueImages.set(0, correctIngredient);
    // Shuffle the list again so that the order of the two wrong ingredients and correct ingredient
    // is randomised
    Collections.shuffle(uniqueImages);
    Image[] shuffledImages = uniqueImages.toArray(new Image[0]);

    Tooltip pondTooltip = new Tooltip("Pond");
    pondTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewSpiralPond, pondTooltip);

    Rotate rotate = new Rotate(-45); // Rotate by 45 degrees

    // Apply the transformation to the Slider
    sldOne.getTransforms().add(rotate);
    sldTwo.getTransforms().add(rotate);
    sldThree.getTransforms().add(rotate);

    sldOne
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              // Add the difference between newValue and oldValue to the Y position of the frog
              imgViewSpiralFrog.setY(
                  imgViewSpiralFrog.getY() + (oldValue.doubleValue() - newValue.doubleValue()));
              // update the end point of line to be higher
              threadOne.setEndY(
                  threadOne.getEndY() + (oldValue.doubleValue() - newValue.doubleValue()));

              // if the frog is at the bottom of the slider, change the pic to be the frog with the
              // fishing rod
              if (newValue.doubleValue() == sldOne.getMax()) {
                // get image with picOne
                Image selectedImage = shuffledImages[0];
                imgViewSpiralFrog.setImage(selectedImage);
                if (selectedImage == correctIngredient) {

                  // alert the user that they have found the correct image
                  Platform.runLater(
                      () -> {
                        txtSpeech.setText("You fished up the correct ingredient!");
                        pnSpeech.setVisible(true);
                      });
                  if (GameState.isTextToSpeechEnabled == true) {
                    Thread speachThread =
                        new Thread(
                            () -> {
                              textToSpeech.speak("You fished up the correct ingredient!");
                              ;
                            });
                    speachThread.start();
                  }

                  GameState.isFishingComplete = true;
                  correctImageView = imgViewSpiralFrog;
                }

                // he slider should not move anymore
                sldOne.lookup(".thumb").setPickOnBounds(false);
                sldOneDisablePane.setVisible(true);
              }
            });

    sldTwo
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              // Add the difference between newValue and oldValue to the Y position of the frog
              imgViewMushroom.setY(
                  imgViewMushroom.getY() + (oldValue.doubleValue() - newValue.doubleValue()));
              threadTwo.setEndY(
                  threadTwo.getEndY() + (oldValue.doubleValue() - newValue.doubleValue()));
              if (newValue.doubleValue() == sldTwo.getMax()) {
                // get an image that hasnt been selected yet
                Image selectedImage = shuffledImages[1];
                imgViewMushroom.setImage(selectedImage);
                if (selectedImage == correctIngredient) {

                  // alert the user that they have found the correct image
                  Platform.runLater(
                      () -> {
                        txtSpeech.setText("You fished up the correct ingredient!");
                        pnSpeech.setVisible(true);
                      });
                  GameState.isFishingComplete = true;
                  correctImageView = imgViewMushroom;
                }
                // he slider should not move anymore
                sldTwo.lookup(".thumb").setPickOnBounds(false);
                sldTwoDisablePane.setVisible(true);
              }
            });

    sldThree
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              // Add the difference between newValue and oldValue to the Y position of the frog
              imgViewBug.setY(
                  imgViewBug.getY() + (oldValue.doubleValue() - newValue.doubleValue()));
              threadThree.setEndY(
                  threadThree.getEndY() + (oldValue.doubleValue() - newValue.doubleValue()));

              if (newValue.doubleValue() == sldThree.getMax()) {
                // get an image that hasnt been selected yet
                Image selectedImage = shuffledImages[2];
                imgViewBug.setImage(selectedImage);
                // he slider should not move anymore
                if (selectedImage == correctIngredient) {

                  // alert the user that they have found the correct image
                  Platform.runLater(
                      () -> {
                        txtSpeech.setText("You fished up the correct ingredient!");
                        pnSpeech.setVisible(true);
                      });
                  if (GameState.isTextToSpeechEnabled == true) {
                    Thread speachThread =
                        new Thread(
                            () -> {
                              textToSpeech.speak("You fished up the correct ingredient!");
                              ;
                            });
                    speachThread.start();
                  }
                  GameState.isFishingComplete = true;
                  correctImageView = imgViewBug;
                }
                sldThree.lookup(".thumb").setPickOnBounds(false);
                sldThreeDisablePane.setVisible(true);
              }
            });

    // Create animation task for clickable objects
    Task<Void> animationTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // The method ImagePulseAnimation is from its own class
            ImagePulseAnimation rightArrowAnimation = new ImagePulseAnimation(imgViewRightArrow);
            rightArrowAnimation.playAnimation();
            return null;
          }
        };

    // Create threads that run each animation task. Start the main animation thread
    Thread animationThread = new Thread(animationTask, "Animation Thread");
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
    Platform.runLater(() -> timerLbl.setText(formattedTime));
    // when time is up, show an alert that they have lost
    if (formattedTime.equals("00:01")) {
      LoseController.setItemCounter();
      timerLbl.setText("00:00");
    }
  }

  /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewSpiralPond.
   *
   * @param event mouse event
   * @throws IOException exception for text to speech
   */
  @FXML
  private void onPondClick(MouseEvent event) throws IOException {
    System.out.println("pond clicked");
    // set pnfishing to visible
    pnFishing.setVisible(true);
    pnFishingOpacity.setVisible(true);
    // Remove the option to change room
    imgViewRightArrow.setVisible(false);
    if (GameState.isFishingComplete) {

      txtSpeech.setText("You fished up the correct ingredient!");
      if (GameState.isTextToSpeechEnabled == true) {
        Thread speachThread =
            new Thread(
                () -> {
                  textToSpeech.speak("You fished up the correct ingredient!");
                  ;
                });
        speachThread.start();
      }

      pnSpeech.setVisible(true);
      correctImageView.setVisible(false);
    }
  }

  /**
   * Handles the MouseEvent for the button btnFishingExit.
   *
   * @param event mouse event
   * @throws IOException in case of text to speech
   */
  @FXML
  private void onFishingExit(MouseEvent event) throws IOException {
    // set pnfishing to invisible
    pnFishing.setVisible(false);
    pnFishingOpacity.setVisible(false);
    // Include the ability to move rooms
    imgViewRightArrow.setVisible(true);
    pnSpeech.setVisible(false);

    if (GameState.isFishingComplete == true && GameState.isForestCollected != true) {
      imgViewIngredient.setImage(correctIngredient);
      imgViewIngredient.setVisible(true);
      // Create a new thread for the animation
      Thread animationThread =
          new Thread(
              () -> {
                ImagePulseAnimation imageAnimation = new ImagePulseAnimation(imgViewIngredient);
                imageAnimation.playAnimation();
              });

      // Start the animation thread
      animationThread.start();
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
   * Handles the MouseEvent 'on Mouse Entered' for the imageView imgViewScrollIcon.
   *
   * @param event MouseEvent 'on Mouse Entered'
   */
  @FXML
  private void onEnterIconScroll(MouseEvent event) {
    pnScroll.setVisible(true);
  }



  /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewIngredient.
   *
   * @param event mouse event
   */
  @FXML
  private void onIngredientClicked(MouseEvent event) {
    // Play the textToSpeech using a thread to make sure the app doesnt freeze
    if (GameState.isTextToSpeechEnabled == true) {
      Thread speachThread = new Thread(
          () -> {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak("Item picked up");
          });
      speachThread.start();
    }
    // remove the ingredient and change the GameState
    GameState.isForestCollected = true;
    GameState.itemsCollected++;
    imgViewIngredient.setVisible(false);
  }
    /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewRightArrow.
   *
   * @param event mouse event
   */
  @FXML
  private void onRightArrowClicked(MouseEvent event) {
    // Switch to the Lab
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.LAB));
    GameState.currentRoom = "lab";
  }
    /**
   * Handles the MouseEvent 'on Mouse Exited' for the imageView imgViewScrollIcon.
   *
   * @param event MouseEvent 'on Mouse Exited'
   */
  @FXML
  private void onExitIconScroll(MouseEvent event) {
    pnScroll.setVisible(false);
  }

  /**
   * Handles the ActionEvent on the Button btnSpeechExit.
   *
   * @param event action event
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
   * Handles the Mouse Event 'on Mouse Click' for the ImageView imgViewSettings.
   *
   * @param event Mouse Event 'on Mouse Click'
   */
  @FXML
  private void onSettingsClicked(MouseEvent event) {
    App.setUi(AppUi.SETTINGS);
  }
}
