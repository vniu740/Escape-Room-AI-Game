package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ImagePulseAnimation;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class DragonRoomController {
  @FXML private ImageView imageShelf;
  @FXML private ImageView imageFire;

  @FXML
  public void initialize() throws IOException {

    // Create a new thread for the animation
    Thread animationThread =
        new Thread(
            () -> {
              ImagePulseAnimation imageAnimation = new ImagePulseAnimation(imageShelf);
              ImagePulseAnimation image2Animation = new ImagePulseAnimation(imageFire);
              imageAnimation.playAnimation();
              image2Animation.playAnimation();
            });

    // Start the animation thread
    animationThread.start();
  }

  @FXML
  private void onLockClicked() {
    App.setUi(AppUi.MATCHING);
  }
}
