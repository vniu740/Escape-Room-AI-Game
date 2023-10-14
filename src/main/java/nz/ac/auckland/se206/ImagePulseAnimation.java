package nz.ac.auckland.se206;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/** Class to handle the pulse image animation. */
public class ImagePulseAnimation {
  // Create a new ScaleTransition object
  private ScaleTransition scaleTransition;

  /**
   * Constructor for the class.
   *
   * @param imageview imageview that the pulse animation will be applied to
   */
  public ImagePulseAnimation(ImageView imageview) {
    scaleTransition = new ScaleTransition(Duration.seconds(1.0), imageview);
    scaleTransition.setFromX(1.0);
    scaleTransition.setFromY(1.0);
    scaleTransition.setToX(1.2);
    scaleTransition.setToY(1.2);
    scaleTransition.setCycleCount(Animation.INDEFINITE);
    scaleTransition.setAutoReverse(true);
  }

  /** Method that plays the animation. */
  public void playAnimation() {
    System.out.println("playAnimation() called");
    scaleTransition.play();
  }
}
