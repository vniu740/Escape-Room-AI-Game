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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

/** Controller class for the room view. */
public class ForestRoomController implements TimeManager.TimeUpdateListener {

  @FXML private Rectangle door;
  @FXML private Rectangle window;
  @FXML private Rectangle vase;
  @FXML private Label timerLbl;

  private static TimeManager timeManager;
  @FXML private Button switchScenes;
  @FXML private ImageView imgViewSpiralPond;

  @FXML private Slider sldOne;
  @FXML private Slider sldTwo;
  @FXML private Slider sldThree;
  @FXML private ImageView imgViewSpiralFrog;
  @FXML private ImageView imgViewMushroom;
  @FXML private ImageView imgViewBug;
  @FXML private ImageView wizard;

  @FXML private Pane pnFishing;

  @FXML private Pane pnFishingOpacity;

  @FXML private Button btnFishingExit;
  @FXML private Pane sldOneDisablePane;

  @FXML private Pane sldTwoDisablePane;

  @FXML private Pane sldThreeDisablePane;

  @FXML private Line threadOne;

  @FXML private Line threadTwo;

  @FXML private Line threadThree;

  @FXML private ImageView imgViewRightArrow;

  private @FXML Pane pnScroll;
  private @FXML HBox hBoxScroll;
  private @FXML ImageView imgViewIconScroll;
  private @FXML ImageView imgViewIngredient;
  private @FXML Image correctIngredient;

  @FXML private Button btnSpeechExit;
  @FXML private Pane pnSpeech;
  @FXML private Text txtSpeech;
  @FXML private ImageView imgViewWizard;

  String[] images = {"bottle.png", "bottleEyes.png", "BottleM.png"};

  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {

    // Initialization code goes here
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);
    // start the
    // timeManager.startTimer();

    // Kimia's original shuffling code
    // //list of images that we can select from randomly
    // Image[] images = { new Image("/images/bottleBug.png"), new Image("/images/bottleEyes.png"),
    //     new Image("/images/BottleRedMushroom.png"), new Image("/images/bottleBlueMushroom.png"),
    // new Image("/images/bottleSnake.png"), new Image("/images/bottleSeaShell.png"), new
    // Image("/images/bottleGreenLiq.png")};
    //   List<Image> uniqueImages = new ArrayList<>();

    //     // Add unique images to the list
    //     for (Image image : images) {
    //         if (!uniqueImages.contains(image)) {
    //             uniqueImages.add(image);
    //         }
    //     }

    // Shuffle the list
    // Collections.shuffle(uniqueImages);

    // Convert the shuffled list back to an array
    // Image[] shuffledImages = uniqueImages.toArray(new Image[0]);
    // random index to select an image from the list
    // Random rand = new Random();
    // int randomIndex = rand.nextInt(3);
    // int randomIndex = 0; //for testing purposes

    // Get the list of all possible forest images instantiated in LabController
    List<Image> uniqueImages = PotionManager.getForestObjectList();
    // Get the correct ingredient which was randomised and set to index 0 in LabController and
    // remove it
    correctIngredient = uniqueImages.remove(0);
    // Shuffle the list to randomise the incorrect ingredients
    Collections.shuffle(uniqueImages);
    // Make the list a size of 3 ingredients
    uniqueImages.subList(3, 6).clear();
    ;
    // Add the correct ingredient back into the list so that the two wrong ingredients are random
    // and the correct ingredient is in the list
    uniqueImages.set(0, correctIngredient);
    // Shuffle the list again so that the order of the two wrong ingredients and correct ingredient
    // is randomised
    Collections.shuffle(uniqueImages);
    Image[] shuffledImages = uniqueImages.toArray(new Image[0]);

    Tooltip pondTooltip = new Tooltip("pondimagespiral");
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
                  // Platform.runLater(
                  //     () ->
                  //         showDialog(
                  //             "Congratulations!",
                  //             "You have found the correct ingredient in this room!",
                  //             "You have found the correct ingredient!"));

                  Platform.runLater(
                      () -> {
                        txtSpeech.setText("You fished up the correct ingredient!");
                        pnSpeech.setVisible(true);
                      });
                  GameState.isFishingComplete = true;
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
                  // Platform.runLater(
                  //     () ->
                  //         showDialog(
                  //             "Congratulations!",
                  //             "You have found the correct ingredient in this room!",
                  //             "You have found the correct ingredient!"));
                  Platform.runLater(
                      () -> {
                        txtSpeech.setText("You fished up the correct ingredient!");
                        pnSpeech.setVisible(true);
                      });
                  GameState.isFishingComplete = true;
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
                  // Platform.runLater(
                  //     () ->
                  //         showDialog(
                  //             "Congratulations!",
                  //             "You have found the correct ingredient in this room!",
                  //             "You have found the correct ingredient!"));
                  Platform.runLater(
                      () -> {
                        txtSpeech.setText("You fished up the correct ingredient!");
                        pnSpeech.setVisible(true);
                      });
                  GameState.isFishingComplete = true;
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

  public static TimeManager getTimeManager() {
    return timeManager;
  }

  /**
   * Displays a dialog box with the given title, header text, and message.
   *
   * @param title the title of the dialog box
   * @param headerText the header text of the dialog box
   * @param message the message content of the dialog box
   */
  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }

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
      pnSpeech.setVisible(true);
    }
  }

  @FXML
  private void onFishingExit(MouseEvent event) throws IOException {
    // set pnfishing to invisible
    pnFishing.setVisible(false);
    pnFishingOpacity.setVisible(false);
    // Include the ability to move rooms
    imgViewRightArrow.setVisible(true);
    pnSpeech.setVisible(false);

    if (GameState.isFishingComplete == true) {
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

  /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewRightArrow.
   *
   * @param event
   */
  @FXML
  private void onRightArrowClicked(MouseEvent event) {
    // Switch to the Lab
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
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewIngredient
   *
   * @param event
   */
  @FXML
  private void onIngredientClicked(MouseEvent event) {
    // Play the textToSpeech using a thread to make sure the app doesnt freeze
    Thread speachThread =
        new Thread(
            () -> {
              TextToSpeech textToSpeech = new TextToSpeech();
              textToSpeech.speak("Item picked up");
            });
    speachThread.start();
    // remove the ingredient and change the GameState
    GameState.isForestCollected = true;
    GameState.itemsCollected++;
    imgViewIngredient.setVisible(false);
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
