package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TimeManager;

/** Controller class for the room view. */
public class ForestRoomController implements TimeManager.TimeUpdateListener{

  @FXML private Rectangle door;
  @FXML private Rectangle window;
  @FXML
  private Rectangle vase;
  @FXML private Label timerLbl;
  private TimeManager timeManager;
  @FXML
  private Button switchScenes;
  @FXML
  private ImageView imgViewSpiralPond;

  @FXML
  private Slider sldOne;
  @FXML
  private ImageView imgViewSpiralFrog;
    
  @FXML
  private Pane pnFishing;
  
  @FXML
  private Pane pnFishingOpacity;
  
  @FXML
  private Button btnFishingExit; 



  


  /** Initializes the room view, it is called when the room loads. */
  public void initialize() {
    // Initialization code goes here
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

    Tooltip pondTooltip = new Tooltip("pondimagespiral");
    pondTooltip.setShowDelay(Duration.millis(0));
    Tooltip.install(imgViewSpiralPond, pondTooltip);

  //   sldOne.valueProperty().addListener((observable, oldValue, newValue) -> {
  //     // get the new y value of the slider
  //     double y = sldOne.getValue();
  //     // set the y value of the frog to the difference between new and old y values of the slider
  //     imgViewSpiralFrog.setY(imgViewSpiralFrog.getY() + (newValue.doubleValue() - oldValue.doubleValue()));


  // });
  sldOne.valueProperty().addListener((observable, oldValue, newValue) -> {
    // Add the difference between newValue and oldValue to the Y position of the frog
    imgViewSpiralFrog.setY(imgViewSpiralFrog.getY() + (oldValue.doubleValue() - newValue.doubleValue()));
});
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
    //when time is up, show an alert that they have lost 
    if (formattedTime.equals("00:00")) {
      Platform.runLater(() -> showDialog("Game Over", "You have run out of time!", "You have ran out of time!"));
      timerLbl.setText("00:00");
    }
  }


  @FXML
  public void onEnterSpiralPond() {
    fadeIn(imgViewSpiralPond);
  }

  @FXML
  public void onExitSpiralPond() {
    fadeOut(imgViewSpiralPond);
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
    //set pnfishing to visible
    pnFishing.setVisible(true);
    pnFishingOpacity.setVisible(true);

  }

  @FXML
  private void onFishingExit(MouseEvent event) throws IOException {
    //set pnfishing to invisible
    pnFishing.setVisible(false);
    pnFishingOpacity.setVisible(false);

  }

}
