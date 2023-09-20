package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

public class AIChatController implements TimeManager.TimeUpdateListener{
  @FXML private Button button_send;
  @FXML private TextField tf_message;
  @FXML private VBox vbox_message;
  @FXML private ScrollPane sp_main;
  @FXML private static ImageView chatBackground;
  @FXML private Pane paneBack;
  @FXML private Button buttonBack;

  private ChatCompletionRequest chatCompletionRequest;

  private int numHints; // number of hints given to the user
  private String gameLevel;

  @FXML 
  private Label timerLblChat;
  private static TimeManager timeManager;

  @FXML
  public void initialize() throws ApiProxyException {
    timeManager = TimeManager.getInstance();
    timeManager.registerListener(this);

    // Set the background image
    chatBackground = new ImageView();
    // Make chatBackground the same size as the pane
    chatBackground.fitWidthProperty().bind(paneBack.widthProperty());
    chatBackground.fitHeightProperty().bind(paneBack.heightProperty());
    // Set the opacity of chatBackground to 0.6
    chatBackground.setOpacity(0.6);
    // Add the background image to the pane
    paneBack.getChildren().add(chatBackground);
    // Move the background image to the back
    chatBackground.toBack();

    // set the number of hints given to 0
    numHints = 0;

    tf_message.setEditable(false);
    // round the corners of scrollpane
    // sp_main.setStyle("-fx-background-radius: 10px;");
    // round the content of scrollpane
    vbox_message.setStyle("-fx-background-radius: 10px;");
    Task<Void> getRiddleTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        chatCompletionRequest = new ChatCompletionRequest()
            .setN(1)
            .setTemperature(0.4)
            .setTopP(0.5)
            .setMaxTokens(100);

        ChatMessage msg = runGpt(
            new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("potion")));
        // Add label for msg
        addLabel(msg.getContent(), vbox_message, sp_main);
        tf_message.clear();
        tf_message.setEditable(true);
        return null;
      }
    };

    Thread riddleThread = new Thread(getRiddleTask, "Riddle Thread");
    riddleThread.start();

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

      textFlow.setStyle("-fx-background-color: #00bfff; -fx-background-radius: 20;");
      textFlow.setPadding(new Insets(5, 5, 10, 10));
      text.setFill(Color.color(0.934, 0.945, 0.996));

      hbox.getChildren().add(textFlow);
      vbox_message.getChildren().add(hbox);
      tf_message.clear();

      Task<Void> sendChatMessageTask =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              ChatMessage lastMsg = runGpt(msg);

              // Call containsHintPhrase to check if the message contains a hint
              if (containsHintPhrase(lastMsg.getContent())) {
  
              numHints++;
                if (GameState.isRiddleResolved == true && GameState.isFishingComplete == false
                    && GameState.isMatchGameWon == true) {
                  lastMsg = runGpt(new ChatMessage("user", GptPromptEngineering.getHintForest()));
                }
                if (GameState.isRiddleResolved == true && GameState.isFishingComplete == true
                    && GameState.isMatchGameWon == false) {
                  lastMsg = runGpt(new ChatMessage("user", GptPromptEngineering.getHintDragon()));
                }
                if (GameState.isRiddleResolved == true && GameState.isFishingComplete == false
                    && GameState.isMatchGameWon == false) {
                  lastMsg = runGpt(new ChatMessage("user", GptPromptEngineering.getHintActivity()));
                }
                if (GameState.isRiddleResolved == true && GameState.isFishingComplete == true
                    && GameState.isMatchGameWon == true) {
                  lastMsg = runGpt(new ChatMessage("user", GptPromptEngineering.getHintPotion()));
                }
                // Check the game level and limit the hints accordingly
                if (gameLevel.equals("medium") && numHints > 5) {
                  // Send a message to the user that they have used up all their hints
                  addLabel("You have used up all your hints", vbox_message, sp_main);
                } else if (gameLevel.equals("hard")) {
                  // Send a message to the user that they can't ask for hints
                  addLabel("You can't ask for hints", vbox_message, sp_main);
                } else {
                  // Call addLabel to add the hint message to the vbox
                  addLabel(lastMsg.getContent(), vbox_message, sp_main);
                }
              } else {
                // Call addLabel to add the message to the vbox
                addLabel(lastMsg.getContent(), vbox_message, sp_main);
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
        "-fx-background-color: #e6e6e6; -fx-background-radius: 10px; -fx-padding: 5px;");
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
        || input.matches("(?i).*\\bhere\\s+is\\s+another\\s+hint\\b.*");
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
}
