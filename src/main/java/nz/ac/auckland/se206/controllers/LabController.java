package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;

public class LabController {

  private @FXML HBox hBoxScroll;
  private @FXML ImageView imgViewOne;
  private @FXML ImageView imgViewTwo;
  private @FXML ImageView imgViewThree;
  private @FXML ImageView imgViewSpiralWindow;
  private @FXML ImageView imgViewSpiralJewellery;
  private @FXML ImageView imgViewSpiralLever;
  private @FXML ImageView imgViewSpiralCauldron;
  private @FXML ImageView imgViewScrollIcon;
  private @FXML ImageView imgViewCauldronFrog;
  private @FXML ImageView imgViewCauldronCrystal;
  private @FXML ImageView imgViewCauldronScale;
  private @FXML ImageView imgViewCauldronBubbles;
  private @FXML Image imgFrog;
  private @FXML Image imgCrystal;
  private @FXML Image imgScale;
  private @FXML Pane pnCauldron;
  private @FXML Pane pnCauldronOpacity;
  private @FXML Pane pnScroll;
  private @FXML Text txtTryAgain;
  private @FXML Text txtCorrect;
  private @FXML Button btnCauldronExit;

  private List<Image> imgScrollList = new ArrayList<Image>();
  private List<String> stringScrollListOrder = new ArrayList<String>();
  private List<String> imgCauldronList = new ArrayList<String>();
  private int imagesDropped = 0;

  @FXML
  public void initialize() throws URISyntaxException {
    setPotionRecipe();
    setCauldronOrder();

    imgViewSpiralJewellery.setVisible(false);
    txtTryAgain.setVisible(false);
    txtCorrect.setVisible(false);

    Tooltip windowTooltip = new Tooltip("Window");
    windowTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewSpiralWindow, windowTooltip);

    Tooltip jewelleryTooltip = new Tooltip("Jewellery Box");
    jewelleryTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewSpiralJewellery, jewelleryTooltip);

    Tooltip leverTooltip = new Tooltip("Magical Lever");
    leverTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewSpiralLever, leverTooltip);

    Tooltip cauldronTooltip = new Tooltip("Cauldron");
    cauldronTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewSpiralCauldron, cauldronTooltip);
  }

  private void setPotionRecipe() {
    int listCounter = 0;
    imgFrog = new Image("images/frog.png");
    imgCrystal = new Image("images/crystal.png");
    imgScale = new Image("images/Scale.png");

    Collections.addAll(imgScrollList, imgFrog, imgCrystal, imgScale);
    Collections.shuffle(imgScrollList);

    for (Node child : hBoxScroll.getChildren()) {
      if (child instanceof ImageView) {
        ImageView childImageView = (ImageView) child;
        childImageView.setImage(imgScrollList.get(listCounter));
      }
      listCounter++;
    }
  }

  private void setCauldronOrder() {
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

  @FXML
  public void onEnterIconScroll() {
    pnScroll.setVisible(true);
  }

  @FXML
  public void onExitIconScroll() {
    pnScroll.setVisible(false);
  }

  @FXML
  public void onEnterSpiralWindow() {
    fadeIn(imgViewSpiralWindow);
  }

  @FXML
  public void onExitSpiralWindow() {
    fadeOut(imgViewSpiralWindow);
  }

  @FXML
  public void onEnterSpiralJewellery() {
    fadeIn(imgViewSpiralJewellery);
  }

  @FXML
  public void onExitSpiralJewellery() {
    fadeOut(imgViewSpiralJewellery);
  }

  @FXML
  public void onJewelleryClick() throws IOException {
    App.setRoot("chat");
  }

  @FXML
  public void onEnterSpiralLever() {
    fadeIn(imgViewSpiralLever);
  }

  @FXML
  public void onExitSpiralLever() {
    fadeOut(imgViewSpiralLever);
  }

  @FXML
  public void onLeverClick() {
    GameState.isLeverPulled = true;
    imgViewSpiralJewellery.setVisible(true);
    imgViewSpiralLever.setVisible(false);
  }

  @FXML
  public void onEnterSpiralCauldron() {
    fadeIn(imgViewSpiralCauldron);
  }

  @FXML
  public void onExitSpiralCauldron() {
    fadeOut(imgViewSpiralCauldron);
  }

  @FXML
  public void onCauldronClick() {
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


  @FXML
  private void onDragDetectionSourceFrog(MouseEvent event) {
    if (GameState.isRiddleResolved == false) {
      return;
    }
    dragDetection(event);
  }

    @FXML
  private void onDragDetectionSourceCrystal(MouseEvent event) {
    if (GameState.isRiddleResolved == false) {
      return;
    }

    dragDetection(event);
  }

    @FXML
  private void onDragDetectionSourceScale(MouseEvent event) {
    if (GameState.isRiddleResolved == false) {
      return;
    }

    dragDetection(event);
  }

  

  private void dragDetection(MouseEvent event) {
    imgViewCauldronBubbles.setVisible(false);
    ImageView imageViewSource = (ImageView) event.getSource();
    Image originalImage = imageViewSource.getImage();

    double desiredWidth = 50; // Set the desired width
    double desiredHeight = 50; // Set the desired height
    Image resizedImage = new Image(originalImage.getUrl(), desiredWidth, desiredHeight, true, true);
    Dragboard db = imageViewSource.startDragAndDrop(TransferMode.ANY);
    db.setDragView(resizedImage);
    ClipboardContent cb = new ClipboardContent();
    cb.putImage(imageViewSource.getImage());
    db.setContent(cb);
    event.consume();
  }

  @FXML
  private void onDragOverDestination(DragEvent event) {
    if (event.getDragboard().hasImage()) {
      event.acceptTransferModes(TransferMode.ANY);
    }
  }

  @FXML
  private void onDragDroppedDestination(DragEvent event) {
    imgViewCauldronBubbles.setVisible(true);
    txtTryAgain.setVisible(false);
    ImageView imageView = (ImageView) event.getGestureSource();
    imgCauldronList.add(imageView.getId());
    imagesDropped++;

    if (imagesDropped == 3) {
      boolean isCorrectOrder = true;
      for (int i = 0; i < 3; i++) {
        if (!stringScrollListOrder.get(i).equals(imgCauldronList.get(i))) {
          isCorrectOrder = false;
          break;
        }
      }
      if (isCorrectOrder) {
        txtCorrect.setVisible(true);
      } else {
        txtTryAgain.setVisible(true);
      }

      imagesDropped = 0;
      imgCauldronList.clear();
    }
  }

  @FXML
  private void onCauldronExit(ActionEvent event) {
    pnCauldron.setVisible(false);
    pnCauldronOpacity.setVisible(false);
    imgViewCauldronBubbles.setVisible(false);
    txtTryAgain.setVisible(false);
  }

}
