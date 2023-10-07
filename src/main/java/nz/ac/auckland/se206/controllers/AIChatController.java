package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class AIChatController implements TimeManager.TimeUpdateListener {

  private static TimeManager timeManager;
  static int numHints;
  @FXML private static ImageView chatBackground;
  @FXML private static Label hintCounter;

  public static TimeManager getTimeManager() {
    return timeManager;
  }

  public static void addLabel(String messageFromClient, VBox vbox, ScrollPane scrollPaneMain) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_RIGHT); // Align to the right
    hbox.setPadding(new Insets(5, 10, 5, 10));

    Text text = new Text(messageFromClient);
    TextFlow textFlow = new TextFlow(text);
    textFlow.setStyle(
        "-fx-background-color: #ffb25b; -fx-background-radius: 10px; -fx-padding: 5px;");
    textFlow.setPadding(new Insets(5, 10, 5, 10));

    hbox.getChildren().add(textFlow);

    // Platform to update the VBox later
    Platform.runLater(
        () -> {
          vbox.getChildren().add(hbox);
        });

    // Scroll to the bottom of the ScrollPane to show the latest message
    scrollPaneMain.setVvalue(1.0);
  }

  public static void setBackground() {
    // Set the background image according to the current room
    if (GameState.currentRoom.equals("dragon")) {
      chatBackground.setImage(new Image("/images/roomDragon.jpg"));
    } else if (GameState.currentRoom.equals("lab")) {
      chatBackground.setImage(new Image("/images/lab.jpg"));
    } else if (GameState.currentRoom.equals("forest")) {
      chatBackground.setImage(new Image("/images/forest.jpg"));
    } else if (GameState.currentRoom.equals("matchGame")) {
      chatBackground.setImage(new Image("/images/candlewall.jpg"));
    }
  }

  public static void setHintCounter() {
    if (GameState.level.equals("medium")) {
      // Update the hint counter for all suitable rooms.
      DragonRoomController.hintCounter.setText(Integer.toString(5 - numHints));
      LabController.hintCounter.setText(Integer.toString(5 - numHints));
      ForestRoomController.hintCounter.setText(Integer.toString(5 - numHints));
      hintCounter.setText(Integer.toString(5 - numHints));
    } else if (GameState.level.equals("hard")) {
      // Update the hint counter for all suitable rooms.
      DragonRoomController.hintCounter.setText(Integer.toString(0));
      LabController.hintCounter.setText(Integer.toString(0));
      ForestRoomController.hintCounter.setText(Integer.toString(0));
      hintCounter.setText("0");
    } else {
      // Update the hint counter for all suitable rooms.
      DragonRoomController.hintCounter.setText("Unlimited");
      LabController.hintCounter.setText("Unlimited");
      ForestRoomController.hintCounter.setText("Unlimited");
      hintCounter.setText("Unlimited");
    }
  }

  private ChatCompletionRequest chatCompletionRequest;
  private ChatCompletionRequest chatCompletionRequestChat;
  private MediaPlayer mediaPlayerHint;
  private String gameLevel;
  private Timeline timeline =
      new Timeline(
          new KeyFrame(
              Duration.millis(15),
              new EventHandler<ActionEvent>() {
                private double x = 2;
                private double y = 2;

                @Override
                public void handle(ActionEvent event) {
                  double initialScaleX;
                  circle.setLayoutX(circle.getLayoutX() + x);
                  circle.setLayoutY(circle.getLayoutY() + y);

                  double sceneMinX = paneBack.getLayoutX();
                  double sceneMinY = paneBack.getLayoutY();
                  double sceneMaxX = paneBack.getLayoutX() + paneBack.getWidth();
                  double sceneMaxY = paneBack.getLayoutY() + paneBack.getHeight();

                  boolean leftBorder = circle.getLayoutX() >= (sceneMaxX - circle.getRadius());
                  boolean rightBorder = circle.getLayoutX() <= (sceneMinX + circle.getRadius());
                  boolean bottomBorder = circle.getLayoutY() >= (sceneMaxY - circle.getRadius());
                  boolean topBorder = circle.getLayoutY() <= (sceneMinY + circle.getRadius());

                  if (rightBorder || leftBorder) {
                    x *= -1;
                    // Flip the image horizontally based on the direction of motion
                    if (x > 0) {
                      initialScaleX = 1.0; // Image faces right
                    } else {
                      initialScaleX = -1.0; // Flip the image horizontally when moving left
                    }

                    // Apply the scale transformation to the circle's image
                    circle.getTransforms().clear();
                    Scale scale = new Scale(initialScaleX, 1.0);
                    circle.getTransforms().add(scale);
                  }
                  if (bottomBorder || topBorder) {
                    y *= -1;
                  }
                }
              }));

  @FXML private Button btnSend;
  @FXML private Button buttonBack;
  @FXML private Circle circle;
  @FXML private ImageView imgViewWizard;
  @FXML private ImageView imgViewWizardCast;
  @FXML private Label timerLblChat;
  @FXML private Pane paneBack;
  @FXML private ScrollPane scrollPaneMain;
  @FXML private TextField txtFieldMessage;
  @FXML private Text txtSpeak;
  @FXML private VBox messageBox;

  @FXML
  public void initialize() throws ApiProxyException, URISyntaxException {

    // Add a key pressed event handler to the text field
    txtFieldMessage.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            // Trigger the button's action when Enter is pressed
            btnSend.fire();
          }
        });

    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);
    createHintMediaPlayer();

    // Set the background image
    chatBackground = new ImageView();
    // Make chatBackground the same size as the paneBack
    chatBackground.fitWidthProperty().bind(paneBack.widthProperty());
    chatBackground.fitHeightProperty().bind(paneBack.heightProperty());
    // Set the opacity of chatBackground to 0.6
    chatBackground.setOpacity(0.6);
    // Add the background image to the paneBack
    paneBack.getChildren().add(chatBackground);
    // Move the background image to the back
    chatBackground.toBack();

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
    hintCounter.setLayoutX(140);
    hintCounter.setLayoutY(0);
    // add the hintCounter to the paneBack
    paneBack.getChildren().add(hintCounter);

    // set the number of hints given to 0
    numHints = 0;

    txtFieldMessage.setEditable(false);
    // round the corners of scrollpaneBack
    // scrollPaneMain.setStyle("-fx-background-radius: 10px;");
    // round the content of scrollpaneBack
    messageBox.setStyle("-fx-background-radius: 10px;");
    ArrayList<String> riddleAnswers = new ArrayList<String>();
    riddleAnswers.add("potion");
    riddleAnswers.add("Wizard");
    riddleAnswers.add("wand");
    riddleAnswers.add("spell");
    riddleAnswers.add("magic");
    // choose one of the answers randomly and set it as the correct answer
    GameState.correctAnswer = riddleAnswers.get((int) (Math.random() * riddleAnswers.size()));
    Task<Void> getRiddleTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Creating a new instance of ChatCompletionRequest and setting the parameters
            chatCompletionRequest =
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.4)
                    .setTopP(0.5)
                    .setMaxTokens(130);

            ChatMessage msg =
                runGpt(
                    new ChatMessage(
                        "user",
                        GptPromptEngineering.getRiddleWithGivenWord(GameState.correctAnswer)));
            // Add label for msg
            addLabel(msg.getContent(), messageBox, scrollPaneMain);
            Platform.runLater(() -> stopAnimation());
            txtFieldMessage.clear();
            txtFieldMessage.setEditable(true);

            chatCompletionRequestChat =
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.4)
                    .setTopP(0.5)
                    .setMaxTokens(100);
            runGptChat(new ChatMessage("user", GptPromptEngineering.getContext()));
            return null;
          }
        };

    Thread riddleThread = new Thread(getRiddleTask, "Riddle Thread");
    riddleThread.start();
    circle.setFill(new ImagePattern(new Image("/Images/soot.png")));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    messageBox
        .heightProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              scrollPaneMain.setVvalue((Double) newValue);
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
    Platform.runLater(() -> timerLblChat.setText(formattedTime));
    // when time is up, show an alert that they have lost
    if (formattedTime.equals("00:01")) {

      LoseController.setItemCounter();

      timerLblChat.setText("00:00");
    }
  }

  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    // Add the message to the request
    chatCompletionRequest.addMessage(msg);
    try {
      // Execute the request
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      System.out.println("Problem calling API: " + e.getMessage());
      return null;
    }
  }

  private ChatMessage runGptChat(ChatMessage msg) throws ApiProxyException {
    // Add the message to the request
    chatCompletionRequestChat.addMessage(msg);
    try {
      // Execute the request
      ChatCompletionResult chatCompletionResult = chatCompletionRequestChat.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequestChat.addMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      System.out.println("Problem calling API: " + e.getMessage());
      return null;
    }
  }

  @FXML
  private void onSend() throws ApiProxyException {
    // Disable the text field
    txtFieldMessage.setEditable(false);

    // get the game level
    gameLevel = GameState.getLevel();

    String messageToSend = txtFieldMessage.getText();
    // Message to give to GPT
    ChatMessage msg = new ChatMessage("user", messageToSend);
    if (!messageToSend.isEmpty()) {
      HBox hbox = new HBox();
      hbox.setAlignment(Pos.CENTER_LEFT);
      hbox.setPadding(new Insets(5, 5, 5, 10));

      Text text = new Text(messageToSend);
      TextFlow textFlow = new TextFlow(text);

      // textFlow.setStyle("-fx-background-color: #55cfff; -fx-background-radius: 20;");
      textFlow.setStyle("-fx-background-color: #9c42b4; -fx-background-radius: 20;");
      textFlow.setPadding(new Insets(5, 5, 10, 10));

      hbox.getChildren().add(textFlow);
      messageBox.getChildren().add(hbox);
      txtFieldMessage.clear();

      txtSpeak.setText("CATCH THAT SPRITE!");
      txtSpeak.setVisible(true);
      imgViewWizard.setVisible(false);
      imgViewWizardCast.setVisible(true);
      circle.setVisible(true);
      circle.setFill(new ImagePattern(new Image("/Images/soot.png")));
      timeline.play();

      Task<Void> sendChatMessageTask =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              ChatMessage lastMsg;
              if (!GameState.isRiddleResolved) {
                lastMsg = runGpt(msg);
              } else {
                lastMsg = runGptChat(msg);
              }
              System.out.println(lastMsg.getContent());
              // Call containsHintPhrase to check if the message contains a hint
              if (containsHintPhrase(lastMsg.getContent())) {
                numHints++;

                // Check the game level and limit the hints accordingly
                if (gameLevel.equals("easy") || (gameLevel.equals("medium") && numHints <= 5)) {

                  if (GameState.isRiddleResolved == false && GameState.level.equals("easy")) {
                    addLabel(lastMsg.getContent(), messageBox, scrollPaneMain);
                    Platform.runLater(() -> stopAnimation());
                    txtFieldMessage.setEditable(true);
                    checkIfRiddleCorrect(lastMsg);
                    return null;
                  }
                  if (GameState.isLabCollected == false && GameState.isRiddleResolved == true) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintGem()));
                  }

                  if (GameState.isLabCollected == true
                      && GameState.isForestCollected == false
                      && GameState.isScaleCollected == true) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintForest()));
                  }
                  if (GameState.isLabCollected == true
                      && GameState.isForestCollected == true
                      && GameState.isScaleCollected == false) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintDragon()));
                  }
                  if (GameState.isLabCollected == true
                      && GameState.isForestCollected == false
                      && GameState.isScaleCollected == false) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintActivity()));
                  }
                  if (GameState.isLabCollected == true
                      && GameState.isForestCollected == true
                      && GameState.isScaleCollected == true
                      && GameState.isPotionComplete == false) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintPotion()));
                  }
                  if (GameState.isPotionComplete == true) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintEscape()));
                  }
                  if (gameLevel.equals("medium")) {
                    // Update the hint counter
                    Platform.runLater(() -> setHintCounter());

                    // Play the hint sound effect
                    Platform.runLater(() -> mediaPlayerHint.seek(mediaPlayerHint.getStartTime()));
                    Platform.runLater(() -> mediaPlayerHint.play());

                    addLabel(lastMsg.getContent(), messageBox, scrollPaneMain);
                    Platform.runLater(() -> stopAnimation());
                  }
                  if (gameLevel.equals("easy")) {
                    addLabel(lastMsg.getContent(), messageBox, scrollPaneMain);
                    Platform.runLater(() -> stopAnimation());
                  }

                } else if (gameLevel.equals("medium") && numHints > 5) {
                  // Send a message to the user that they have used up all their hints
                  addLabel("You have used up all your hints", messageBox, scrollPaneMain);
                  Platform.runLater(() -> stopAnimation());
                } else if (gameLevel.equals("hard")) {
                  // Send a message to the user that they can't ask for hints
                  addLabel("You can't ask for hints", messageBox, scrollPaneMain);
                  Platform.runLater(() -> stopAnimation());
                } else {
                  // Call addLabel to add the hint message to the vbox
                  addLabel(lastMsg.getContent(), messageBox, scrollPaneMain);
                  Platform.runLater(() -> stopAnimation());
                }
              } else {
                // Call addLabel to add the message to the vbox
                addLabel(lastMsg.getContent(), messageBox, scrollPaneMain);
                Platform.runLater(() -> stopAnimation());
              }

              txtFieldMessage.setEditable(true);
              // check if the answer to the riddle is correct
              checkIfRiddleCorrect(lastMsg);

              return null;
            }
          };

      // Create a new thread that will run this task and tell it to start
      Thread sendChatMessageThread = new Thread(sendChatMessageTask, "Send Chat Message Thread");
      sendChatMessageThread.start();
    }
  }

  @FXML
  public void onBackClicked() {
    // Set the background image according to the current room
    if (GameState.currentRoom.equals("dragon")) {
      App.setUi(AppUi.DRAGON_ROOM);
    } else if (GameState.currentRoom.equals("lab")) {
      App.setUi(AppUi.LAB);
    } else if (GameState.currentRoom.equals("forest")) {
      App.setUi(AppUi.FOREST);
    } else if (GameState.currentRoom.equals("matchGame")) {
      App.setUi(AppUi.MATCHING);
    }
  }

  public boolean containsHintPhrase(String input) {
    // Use a case-insensitive regular expression to check for either phrase using regex
    return input.matches("(?i).*\\bhere\\s+is\\s+a\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere\\s+is\\s+another\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere's\\s+a\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere's\\s+another\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere\\s+is\\s+a\\s+hint:\\b.*")
        || input.matches("(?i).*\\bhere\\s+is\\s+another\\s+hint:\\b.*")
        || input.matches("(?i).*\\bhere's\\s+a\\s+hint:\\b.*")
        || input.matches("(?i).*\\bhere's\\s+another\\s+hint:\\b.*")
        || input.matches("(?i).*\\bwill\\s+provide\\s+you\\s+with\\s+another\\s+hint:\\b.*")
        || input.matches("(?i).*\\bwill\\s+provide\\s+you\\s+with\\s+another\\s+hint\\.\\b.*")
        || input.matches("(?i).*\\bwill\\s+provide\\s+you\\s+with\\s+a\\s+hint\\.\\b.*")
        // Use a case-insensitive regular expression to check for either phrase using contains for
        // more flexibility
        || input.toLowerCase().contains("here is a hint")
        || input.toLowerCase().contains("here is another hint")
        || input.toLowerCase().contains("here's a hint")
        || input.toLowerCase().contains("here's another hint")
        || input.toLowerCase().contains("here is a hint:")
        || input.toLowerCase().contains("here is another hint:")
        || input.toLowerCase().contains("here's a hint:")
        || input.toLowerCase().contains("here's another hint:")
        || input.toLowerCase().contains("will provide you with another hint")
        || input.toLowerCase().contains("will provide you with a hint")
        || input.toLowerCase().contains("a final hint");
  }

  @FXML
  private void onSpriteClick(MouseEvent event) {
    txtSpeak.setText("GOT HIM!");
    circle.setFill(new ImagePattern(new Image("/Images/explosion.png")));
    delay(400, () -> circle.setVisible(false));
  }

  /**
   * Helper method that delays the call of a runnable.
   *
   * @param time How long the delay will be
   * @param continuation the runnable that will be called after the delay
   */
  private void delay(int time, Runnable continuation) {

    // Create a new task that uses a thread to simulate a delay
    Task<Void> sleep =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              Thread.sleep(time);
            } catch (InterruptedException e) {
              return null;
            }
            return null;
          }
        };
    // Run the input code after the given time passed
    sleep.setOnSucceeded(event -> continuation.run());
    Thread sleepThread = new Thread(sleep, "Sleep Thread");
    // Start the thread
    sleepThread.start();
  }

  private void stopAnimation() {
    imgViewWizardCast.setVisible(false);
    imgViewWizard.setVisible(true);
    timeline.pause();
    circle.setVisible(false);
    txtSpeak.setVisible(false);
  }

  private void checkIfRiddleCorrect(ChatMessage lastMsg) {
    // If the user's input is correct, update the gameState
    if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")) {
      GameState.isRiddleResolved = true;
    }
  }

  public void createHintMediaPlayer() throws URISyntaxException {
    // Create the media player that runs the sound bubbles.mp3
    String path = getClass().getResource("/sounds/hintSound.mp3").toURI().toString();
    Media mediaBubbles = new Media(path);
    mediaPlayerHint = new MediaPlayer(mediaBubbles);
    mediaPlayerHint.setVolume(0.15);
  }
}

/**
 * Attribution:
 *
 * <p>hintSound.mp3 has been originally created by us.
 *
 * <p>All images have been generated through OpenArt Creative 2023 unless otherwise stated below.
 */
