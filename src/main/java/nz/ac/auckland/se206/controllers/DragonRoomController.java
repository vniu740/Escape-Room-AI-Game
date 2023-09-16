package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.ImagePulseAnimation;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.potionRecipeManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class DragonRoomController {
  @FXML private ImageView imageLock;
  @FXML private ImageView imageScale;
  @FXML private ImageView imgViewLeftArrow;
  @FXML private Pane pnScroll;
  @FXML private HBox hBoxScroll;
  @FXML private ImageView imgViewIconScroll;

  @FXML
  public void initialize() throws IOException {

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
  @FXML
  private void onLeftArrowClicked(MouseEvent event) {
    // Switch to the lab
    ImageView imgView = (ImageView) event.getSource();
    Scene sceneImageViewIsIn = imgView.getScene();
    sceneImageViewIsIn.setRoot(SceneManager.getUi(AppUi.LAB));
  }

      /** Helper method that sets the ingredient images of the potion recipe. */
  private void setPotionRecipeImages() {
    int listCounter = 0;
    List<Image> imgScrollList = potionRecipeManager.getImgScrollList();

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
}
/**
 * Attribution: imageLock: <a
 * href="https://www.freepik.com/free-vector/set-lockpad-icon_4150202.htm#query=cartoon%20lock&position=16&from_view=keyword&track=ais">Image
 * by brgfx</a> on Freepik
 */
