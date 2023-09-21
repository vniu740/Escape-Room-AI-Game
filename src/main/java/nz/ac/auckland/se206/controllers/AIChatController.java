package nz.ac.auckland.se206.controllers;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
  @FXML private Button button_send;
  @FXML private TextField tf_message;
  @FXML private VBox vbox_message;
  @FXML private ScrollPane sp_main;
  @FXML private static ImageView chatBackground;
  @FXML private Pane paneBack;
  @FXML private Button buttonBack;
  @FXML private static Label hintCounter;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatCompletionRequest chatCompletionRequestChat;

  private static int numHints; // number of hints given to the user
  private String gameLevel;

  @FXML private Label timerLblChat;
  private static TimeManager timeManager;

  @FXML private ImageView imgViewWizard;
  @FXML private ImageView imgViewWizardCast;
  @FXML private Text txtSpeak;
  @FXML private Circle circle;

  private Timeline timeline =
      new Timeline(
          new KeyFrame(
              Duration.millis(15),
              new EventHandler<ActionEvent>() {
                double X = 2;
                double Y = 2;

                @Override
                public void handle(ActionEvent event) {
                  double initialScaleX = 1.0;
                  circle.setLayoutX(circle.getLayoutX() + X);
                  circle.setLayoutY(circle.getLayoutY() + Y);

                  double sceneMinX = paneBack.getLayoutX();
                  double sceneMinY = paneBack.getLayoutY();
                  double sceneMaxX = paneBack.getLayoutX() + paneBack.getWidth();
                  double sceneMaxY = paneBack.getLayoutY() + paneBack.getHeight();

                  boolean leftBorder = circle.getLayoutX() >= (sceneMaxX - circle.getRadius());
                  boolean rightBorder = circle.getLayoutX() <= (sceneMinX + circle.getRadius());
                  boolean bottomBorder = circle.getLayoutY() >= (sceneMaxY - circle.getRadius());
                  boolean topBorder = circle.getLayoutY() <= (sceneMinY + circle.getRadius());

                  if (rightBorder || leftBorder) {
                    X *= -1;
                    // Flip the image horizontally based on the direction of motion
                    if (X > 0) {
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
                    Y *= -1;
                  }
                }
              }));

  @FXML
  public void initialize() throws ApiProxyException {
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

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
        "-fx-font-size: 23px; "
            + "-fx-font-weight: bold; "
            + "-fx-font-family: 'lucida calligraphy'; "
            + "-fx-font-style: italic; "
            + "-fx-underline: true;");
    // set the layout
    hintCounter.setLayoutX(140);
    hintCounter.setLayoutY(-7);
    // add the hintCounter to the paneBack
    paneBack.getChildren().add(hintCounter);

    // set the number of hints given to 0
    numHints = 0;

    tf_message.setEditable(false);
    // round the corners of scrollpaneBack
    // sp_main.setStyle("-fx-background-radius: 10px;");
    // round the content of scrollpaneBack
    vbox_message.setStyle("-fx-background-radius: 10px;");
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
            chatCompletionRequest =
                new ChatCompletionRequest()
                    .setN(1)
                    .setTemperature(0.4)
                    .setTopP(0.5)
                    .setMaxTokens(100);

            ChatMessage msg =
                runGpt(
                    new ChatMessage(
                        "user",
                        GptPromptEngineering.getRiddleWithGivenWord(GameState.correctAnswer)));
            // Add label for msg
            addLabel(msg.getContent(), vbox_message, sp_main);
            Platform.runLater(() -> stopAnimation());
            tf_message.clear();
            tf_message.setEditable(true);

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

    vbox_message
        .heightProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              sp_main.setVvalue((Double) newValue);
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

      // Platform.runLater(() -> showDialog("Game Over", "You have run out of time!", "You have ran
      // out of time!"));

      LoseController.setItemCounter();

      timerLblChat.setText("00:00");
    }
  }

  public static TimeManager getTimeManager() {
    return timeManager;
  }

  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
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
    chatCompletionRequestChat.addMessage(msg);
    try {
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
  public void onSend() throws ApiProxyException {
    // get the game level
    gameLevel = GameState.getLevel();

    String messageToSend = tf_message.getText();
    // Message to give to GPT
    ChatMessage msg = new ChatMessage("user", messageToSend);
    if (!messageToSend.isEmpty()) {
      HBox hbox = new HBox();
      hbox.setAlignment(Pos.CENTER_LEFT);
      hbox.setPadding(new Insets(5, 5, 5, 10));

      Text text = new Text(messageToSend);
      TextFlow textFlow = new TextFlow(text);

      textFlow.setStyle("-fx-background-color: #55cfff; -fx-background-radius: 20;");
      textFlow.setPadding(new Insets(5, 5, 10, 10));

      hbox.getChildren().add(textFlow);
      vbox_message.getChildren().add(hbox);
      tf_message.clear();

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
                  if (GameState.isRiddleResolved == true
                      && GameState.isForestCollected == false
                      && GameState.isScaleCollected == true) {
                    runGptChat(new ChatMessage("user", GptPromptEngineering.getHintGem()));
                  }

                  if (GameState.isLabCollected == true
                      && GameState.isForestCollected == false
                      && GameState.isScaleCollected == true) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintForest()));
                  }
                  if (GameState.isLabCollected == false
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
                      && GameState.isScaleCollected == true) {
                    lastMsg =
                        runGptChat(new ChatMessage("user", GptPromptEngineering.getHintPotion()));
                  }
                  if (gameLevel.equals("medium")) {
                    // Update the hint counter
                    Platform.runLater(() -> hintCounter.setText(Integer.toString(5 - numHints)));
                    addLabel(lastMsg.getContent(), vbox_message, sp_main);
                    Platform.runLater(() -> stopAnimation());
                  }

                } else if (gameLevel.equals("medium") && numHints > 5) {
                  // Send a message to the user that they have used up all their hints
                  addLabel("You have used up all your hints", vbox_message, sp_main);
                  Platform.runLater(() -> stopAnimation());
                } else if (gameLevel.equals("hard")) {
                  // Send a message to the user that they can't ask for hints
                  addLabel("You can't ask for hints", vbox_message, sp_main);
                  Platform.runLater(() -> stopAnimation());
                } else {
                  // Call addLabel to add the hint message to the vbox
                  addLabel(lastMsg.getContent(), vbox_message, sp_main);
                  Platform.runLater(() -> stopAnimation());
                }
              } else {
                // Call addLabel to add the message to the vbox
                addLabel(lastMsg.getContent(), vbox_message, sp_main);
                Platform.runLater(() -> stopAnimation());
              }

              tf_message.setEditable(true);

              // If the user's input is correct, update the gameState
              if (lastMsg.getRole().equals("assistant")
                  && lastMsg.getContent().startsWith("Correct")) {
                GameState.isRiddleResolved = true;
              }

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

  public static void addLabel(String messageFromClient, VBox vbox, ScrollPane sp_main) {
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
    sp_main.setVvalue(1.0);
  }

  public boolean containsHintPhrase(String input) {
    // Use a case-insensitive regular expression to check for either phrase
    return input.matches("(?i).*\\bhere\\s+is\\s+a\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere\\s+is\\s+another\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere's\\s+a\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere's\\s+another\\s+hint\\b.*")
        || input.matches("(?i).*\\bhere\\s+is\\s+a\\s+hint:\\b.*")
        || input.matches("(?i).*\\bhere\\s+is\\s+another\\s+hint:\\b.*")
        || input.matches("(?i).*\\bhere's\\s+a\\s+hint:\\b.*")
        || input.matches("(?i).*\\bhere's\\s+another\\s+hint:\\b.*");
  }

  public static void setBackground() {
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

  @FXML
  private void onSpriteClick(MouseEvent event) {
    txtSpeak.setText("GOT HIM!");
    circle.setFill(new ImagePattern(new Image("/Images/explosion.png")));
    delay(400, () -> circle.setVisible(false));
    delay(600, () -> circle.setFill(new ImagePattern(new Image("/Images/soot.png"))));
    delay(400, () -> circle.setVisible(true));
    delay(400, () -> txtSpeak.setText("CATCH THAT SPRITE!"));
  }

  /**
   * Helper method that delays the call of a runnable.
   *
   * @param time How long the delay will be
   * @param continuation the runnable that will be called after the delay
   */
  private void delay(int time, Runnable continuation) {
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
    sleep.setOnSucceeded(event -> continuation.run());
    Thread sleepThread = new Thread(sleep, "Sleep Thread");
    sleepThread.start();
  }

  public static void setHintCounter() {
    if (GameState.level.equals("medium")) {
      hintCounter.setText(Integer.toString(5 - numHints));
    } else if (GameState.level.equals("hard")) {
      hintCounter.setText("0");
    } else {
      hintCounter.setText("Unlimited");
    }
  }

  private void stopAnimation() {
    imgViewWizardCast.setVisible(false);
    imgViewWizard.setVisible(true);
    timeline.pause();
    circle.setVisible(false);
    txtSpeak.setVisible(false);
  }
}
