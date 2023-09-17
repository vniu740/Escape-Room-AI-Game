package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.ImagePulseAnimation;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TimeManager;

public class LabController implements TimeManager.TimeUpdateListener{

  private List<Image> imgScrollList = new ArrayList<Image>();
  private List<String> stringScrollListOrder = new ArrayList<String>();
  private List<String> imgCauldronList = new ArrayList<String>();
  private ImagePulseAnimation leverAnimation;
  private ImagePulseAnimation jewelleryAnimation;
  private int imagesDropped = 0;
  private Thread animationJewelleryThread;

  private @FXML HBox hBoxScroll;
  private @FXML ImageView imgViewOne;
  private @FXML ImageView imgViewTwo;
  private @FXML ImageView imgViewThree;
  private @FXML ImageView imgViewWindow;
  private @FXML ImageView imgViewJewellery;
  private @FXML ImageView imgViewLever;
  private @FXML ImageView imgViewCauldron;
  private @FXML ImageView imgViewScrollIcon;
  private @FXML ImageView imgViewCauldronFrog;
  private @FXML ImageView imgViewCauldronCrystal;
  private @FXML ImageView imgViewCauldronScale;
  private @FXML ImageView imgViewCauldronBubbles;
  private @FXML ImageView imgViewLeftArrow;
  private @FXML ImageView imgViewRightArrow;
  private @FXML Image imgFrog;
  private @FXML Image imgCrystal;
  private @FXML Image imgScale;
  private @FXML Pane pnCauldron;
  private @FXML Pane pnCauldronOpacity;
  private @FXML Pane pnScroll;
  private @FXML Text txtTryAgain;
  private @FXML Text txtCorrect;
  private @FXML Button btnCauldronExit;
  private @FXML Label timerLblLab;
  private static TimeManager timeManagerlab;

  /**
   * Initialises the lab scene when called.
   *
   * @throws URISyntaxException
   */
  @FXML
  public void initialize() throws URISyntaxException {
    timeManagerlab = TimeManager.getInstance();
    timeManagerlab.registerListener(this);
    setPotionRecipe();
    setCauldronOrder();

    // Set specific nodes visibility to false
    imgViewJewellery.setVisible(false);
    txtTryAgain.setVisible(false);
    txtCorrect.setVisible(false);

    // Create tooltips for specific clickable objects
    Tooltip windowTooltip = new Tooltip("Window");
    windowTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewWindow, windowTooltip);

    Tooltip jewelleryTooltip = new Tooltip("Jewellery Box");
    jewelleryTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewJewellery, jewelleryTooltip);

    Tooltip leverTooltip = new Tooltip("Magical Lever");
    leverTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewLever, leverTooltip);

    Tooltip cauldronTooltip = new Tooltip("Cauldron");
    cauldronTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewCauldron, cauldronTooltip);

    // Create animation task for clickkable objects
    Task<Void> animationTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        // The method ImagePulseAnimation is from its own class
        leverAnimation = new ImagePulseAnimation(imgViewLever);
        ImagePulseAnimation cauldronAnimation = new ImagePulseAnimation(imgViewCauldron);
        ImagePulseAnimation windowAnimation = new ImagePulseAnimation(imgViewWindow);
        ImagePulseAnimation leftArrowAnimation = new ImagePulseAnimation(imgViewLeftArrow);
        ImagePulseAnimation rightArrowAnimation = new ImagePulseAnimation(imgViewRightArrow);
        windowAnimation.playAnimation();
        cauldronAnimation.playAnimation();
        leverAnimation.playAnimation();
        leftArrowAnimation.playAnimation();
        rightArrowAnimation.playAnimation();
        return null;
      }
    };

    Task<Void> animationJewelleryTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        jewelleryAnimation = new ImagePulseAnimation(imgViewJewellery);
        jewelleryAnimation.playAnimation();
        return null;
      }
    };
    // Create threads that run each animation task. Start the main animation thread
    Thread animationThread = new Thread(animationTask, "Animation Thread");
    animationThread.start();
    animationJewelleryThread = new Thread(animationJewelleryTask, "Animation Thread");
  }
  // .
  /**
  * Updates timer label according to the current time that has passed.
  *
  * @param formattedTime the formatted time to display
  */
  @Override
  public void onTimerUpdate(String formattedTime) {
    Platform.runLater(() -> timerLblLab.setText(formattedTime));
    //when time is up, show an alert that they have lost 
    if (formattedTime.equals("00:01")) {
      //Platform.runLater(() -> showDialog("Game Over", "You have run out of time!", "You have ran out of time!"));
      timerLblLab.setText("00:00");
    }
  }

  public static TimeManager getTimeManager() {
    return timeManagerlab;
  }


  /** Helper method that randomises and sets the order of the ingredients of the potion recipe. */
  private void setPotionRecipe() {
    int listCounter = 0;
    // Initialise each image
    imgFrog = new Image("images/frog.png");
    imgCrystal = new Image("images/crystal.png");
    imgScale = new Image("images/Scale.png");

    // Add all images to the ArrayList imgScrollList
    Collections.addAll(imgScrollList, imgFrog, imgCrystal, imgScale);
    // Shuffle the ArrayList to randomise the order of ingredients
    Collections.shuffle(imgScrollList);

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
   * Helper method that sets the order that ingredients must be added to the cauldron to create the
   * correct potion.
   */
  private void setCauldronOrder() {
    // Loop through the ArrayList imgScrollList and add the fxId of the imageView to the ArrayList
    // stringScrollListOrder
    for (Image image : imgScrollList) {
      if (image == imgFrog) {
        stringScrollListOrder.add("imgViewCauldronFrog");
      } else if (image == imgCrystal) {
        stringScrollListOrder.add("imgViewCauldronCrystal");
      } else if (image == imgScale) {
        stringScrollListOrder.add("imgViewCauldronScale");
      }
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
   * Handles the MouseEvent 'on Mouse Click' for the imageView imgViewJewellery.
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void onJewelleryClick(MouseEvent event) throws IOException {
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.CHAT));
  }

  /**
   * Handles the MouseEvent 'on Mouse Click' for the imageView imgViewLever.
   *
   * @param event
   */
  @FXML
  private void onLeverClick(MouseEvent event) {
    GameState.isLeverPulled = true;
    imgViewJewellery.setVisible(true);
    animationJewelleryThread.start();
    imgViewLever.setVisible(false);
  }

  /**
   * Handles the MouseEvent 'on Mouse Click' for the imageView imgViewCauldron.
   *
   * @param event
   */
  @FXML
  private void onCauldronClick(MouseEvent event) {
    if (GameState.isRiddleResolved) {
      imgViewCauldronCrystal.setEffect(null);
    }
    pnCauldron.setVisible(true);
    pnCauldronOpacity.setVisible(true);
  }

  /**
   * Helper method that fades in an ImageView of the GUI.
   *
   * @param anchorPane
   */
  public void fadeIn(ImageView imageView) {
    // Create a fade transtion that starts from opacity 0 and ends at opacity 1 that runs for 0.3
    // seconds
    FadeTransition transition = new FadeTransition(Duration.seconds(0.3), imageView);
    transition.setFromValue(0);
    transition.setToValue(0.6);
    transition.play();
  }

  /**
   * Helper method that fades out an ImageView of the GUI.
   *
   * @param anchorPane
   */
  public void fadeOut(ImageView imageView) {
    // Create a fade transtion that starts from opacity 1 and ends at opacity 0 that runs for 0.3
    // seconds
    FadeTransition transition = new FadeTransition(Duration.seconds(0.3), imageView);
    transition.setFromValue(0.6);
    transition.setToValue(0);
    transition.play();
  }

  /**
   * Handles the MouseEvent 'on Drag Deteced' for the multiple imageViews imgViewCauldronFrog.
   *
   * @param event
   */
  @FXML
  private void onDragDetectionSourceFrog(MouseEvent event) {
    if (GameState.isRiddleResolved == false) {
      return;
    }
    dragDetection(event);
  }

  /**
   * Handles the MouseEvent 'on Drag Deteced' for the multiple imageViews imgViewCauldronCrystal.
   *
   * @param event
   */
  @FXML
  private void onDragDetectionSourceCrystal(MouseEvent event) {
    if (GameState.isRiddleResolved == false) {
      return;
    }

    dragDetection(event);
  }

  /**
   * Handles the MouseEvent 'on Drag Deteced' for the multiple imageViews imgViewCauldronScale.
   *
   * @param event
   */
  @FXML
  private void onDragDetectionSourceScale(MouseEvent event) {
    if (GameState.isRiddleResolved == false) {
      return;
    }

    dragDetection(event);
  }

  /**
   * Helper method that is called in the handling of the MouseEvent 'on Drag Detetected'.
   *
   * @param event
   */
  private void dragDetection(MouseEvent event) {
    // If a drag is detected remove cauldron bubbles
    imgViewCauldronBubbles.setVisible(false);

    // Get the imageView and image of the image that is being dragged
    ImageView imageViewSource = (ImageView) event.getSource();
    Image originalImage = imageViewSource.getImage();

    // Shrink the dragged image so when it is dragged the icon appears smaller
    double desiredWidth = 50; // Set the desired width
    double desiredHeight = 50; // Set the desired height
    Image resizedImage = new Image(originalImage.getUrl(), desiredWidth, desiredHeight, true, true);

    // Add the image to the dragboard and clipboard
    Dragboard db = imageViewSource.startDragAndDrop(TransferMode.ANY);
    db.setDragView(resizedImage);
    ClipboardContent cb = new ClipboardContent();
    cb.putImage(imageViewSource.getImage());
    db.setContent(cb);
    event.consume();
  }

  /**
   * Handles the DragEvent 'on Drag Over' for the ImageView imgViewDestination.
   *
   * @param event
   */
  @FXML
  private void onDragOverDestination(DragEvent event) {
    if (event.getDragboard().hasImage()) {
      event.acceptTransferModes(TransferMode.ANY);
    }
  }

  /**
   * Handles the DragEvent 'on Drag Dropped' for the ImageView imgViewDestination.
   *
   * @param event
   */
  @FXML
  private void onDragDroppedDestination(DragEvent event) {
    // Make the image of the bubbles appear
    imgViewCauldronBubbles.setVisible(true);

    // Remove the try again text
    txtTryAgain.setVisible(false);

    // Get the fxId of the image that was dropped and add it to the ArrayList
    ImageView imageView = (ImageView) event.getGestureSource();
    imgCauldronList.add(imageView.getId());

    // Increase the count of images dropped
    imagesDropped++;

    // If 3 images have been dropped (i.e user tried to complete potion order)
    if (imagesDropped == 3) {
      boolean isCorrectOrder = true;
      // Loop 3 times
      for (int i = 0; i < 3; i++) {
        // If the fxId of the original potion recipe in the ith position is not equal to the fxId of
        // the dropped images in the ith position then the order is not correct
        if (!stringScrollListOrder.get(i).equals(imgCauldronList.get(i))) {
          isCorrectOrder = false;
          break;
        }
      }
      // Display corresponding image
      if (isCorrectOrder) {
        txtCorrect.setVisible(true);
      } else {
        txtTryAgain.setVisible(true);
      }

      // Reset so the user can try to make the potion again
      imagesDropped = 0;
      imgCauldronList.clear();
    }
  }

  /**
   * Handles the ActionEvent on the Button btnCauldronExit
   *
   * @param event
   */
  @FXML
  private void onCauldronExit(ActionEvent event) {
    pnCauldron.setVisible(false);
    pnCauldronOpacity.setVisible(false);
    imgViewCauldronBubbles.setVisible(false);
    txtTryAgain.setVisible(false);
  }

  /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewLeftArrow.
   *
   * @param event
   */
  @FXML
  private void onLeftArrowClicked(MouseEvent event) {
    // Switch to the forest
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.FOREST));
  }

  /**
   * Handles the MouseEvent 'on Mouse Clicked' for the imageView imgViewRightArrow.
   *
   * @param event
   */
  @FXML
  private void onRightArrowClicked(MouseEvent event) {
    // Switch to the dragon room
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.DRAGON_ROOM));
  }
}
