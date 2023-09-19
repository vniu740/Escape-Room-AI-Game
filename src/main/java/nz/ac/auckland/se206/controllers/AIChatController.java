package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class AIChatController {
  @FXML
  private Button button_send;
  @FXML
  private TextField tf_message;
  @FXML
  private VBox vbox_message;
  @FXML
  private ScrollPane sp_main;

  private ChatCompletionRequest chatCompletionRequest;

  // @FXML
  // public void initialize() throws ApiProxyException {

  //   tf_message.setEditable(false);
  //   Task<Void> getRiddleTask = new Task<Void>() {
  //     @Override
  //     protected Void call() throws Exception {
  //       chatCompletionRequest = new ChatCompletionRequest()
  //           .setN(1)
  //           .setTemperature(0.4)
  //           .setTopP(0.5)
  //           .setMaxTokens(100);
  //       //btnGoBack.setDisable(true);
  //       ChatMessage msg = runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("potion")));
  //       //add label for msg 
  //       addLabel(msg.getContent(), vbox_message, sp_main);
  //       tf_message.clear();
  //       tf_message.setEditable(true);
  //       //btnGoBack.setDisable(false);
  //       return null;
  //     }
  //   };

  //   Thread riddleThread = new Thread(getRiddleTask, "Riddle Thread");
  //   riddleThread.start();

  //   vbox_message.heightProperty().addListener((observable, oldValue, newValue) -> {
  //     sp_main.setVvalue((Double) newValue);
  //   });

  //   vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
  //     @Override
  //     public void changed(javafx.beans.value.ObservableValue<? extends Number> observable, Number oldValue,
  //         Number newValue) {
  //       sp_main.setVvalue((Double) newValue);
  //     }

  //   });

  // }
  @FXML
  public void initialize() throws ApiProxyException {

    tf_message.setEditable(false);
    //round the corners of scrollpane
    // sp_main.setStyle("-fx-background-radius: 10px;");
    //round the content of scrollpane
    vbox_message.setStyle("-fx-background-radius: 10px;");
    Task<Void> getRiddleTask = new Task<Void>() {
      @Override
      protected Void call() throws Exception {
        chatCompletionRequest = new ChatCompletionRequest()
            .setN(1)
            .setTemperature(0.4)
            .setTopP(0.5)
            .setMaxTokens(100);

        ChatMessage msg = runGpt(new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("potion")));
        // Add label for msg
        addLabel(msg.getContent(), vbox_message, sp_main);
        tf_message.clear();
        tf_message.setEditable(true);
        return null;
      }
    };

    Thread riddleThread = new Thread(getRiddleTask, "Riddle Thread");
    riddleThread.start();

    vbox_message.heightProperty().addListener((observable, oldValue, newValue) -> {
      sp_main.setVvalue((Double) newValue);
    });
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
    String messageToSend = tf_message.getText();
    //message to give to gpt 
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

      Task<Void> sendChatMessageTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          ChatMessage lastMsg = runGpt(msg);
          // call add label to add the message to the vbox
          addLabel(lastMsg.getContent(), vbox_message, sp_main);
          tf_message.setEditable(true);
          //btnGoBack.setDisable(false);
          // If the users input is correct, update the gameState
          if (lastMsg.getRole().equals("assistant")
              && lastMsg.getContent().startsWith("Correct")) {
            GameState.isRiddleResolved = true;
            tf_message.setEditable(false);
          }

          return null;
        }
      };
      // Create a new thread that will run this task and tell it to start
      Thread sendChatMessageThread = new Thread(sendChatMessageTask, "Send Chat Message Thread");
      sendChatMessageThread.start();

    }

  }

  // public static void addLabel(String messageFromClient, VBox vbox) {
  //   HBox hbox = new HBox();
  //   hbox.setAlignment(Pos.CENTER_RIGHT);
  //   hbox.setPadding(new Insets(5, 10, 5, 10));

  //   Text text = new Text(messageFromClient);
  //   TextFlow textFlow = new TextFlow(text);
  //   textFlow.setStyle("-fx-background-color: #e6e6e6; -fx-background-radius: 10px; -fx-padding: 5px;"); 
  //   textFlow.setPadding(new Insets(5, 10, 5, 10));
  //   hbox.getChildren().add(textFlow);

  //   //platform to update the vbox later 
  //   Platform.runLater(() -> {
  //     vbox.getChildren().add(hbox);
  //   });
  // }
  // public static void addLabel(String messageFromClient, VBox vbox, ScrollPane sp_main) {
  //   HBox hbox = new HBox();
  //   hbox.setAlignment(Pos.CENTER_LEFT);
  //   hbox.setPadding(new Insets(5, 10, 5, 10));
  //   hbox.setMaxWidth(sp_main.getViewportBounds().getWidth() - 20); // Adjust the value as needed

  //   Text text = new Text(messageFromClient);
  //   TextFlow textFlow = new TextFlow(text);
  //   textFlow.setStyle("-fx-background-color: #e6e6e6; -fx-background-radius: 10px; -fx-padding: 5px;");
  //   textFlow.setPadding(new Insets(5, 10, 5, 10));

  //   // Set the preferred width of TextFlow to prevent horizontal scrollingm and have it on the right 
  //   textFlow.setPrefWidth(sp_main.getViewportBounds().getWidth() - 20); // Adjust the value as needed

  //   hbox.getChildren().add(textFlow);

  //   // Platform to update the VBox later
  //   Platform.runLater(() -> {
  //     vbox.getChildren().add(hbox);
  //   });
  // }
  public static void addLabel(String messageFromClient, VBox vbox, ScrollPane sp_main) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_RIGHT); // Align to the right
    hbox.setPadding(new Insets(5, 10, 5, 10));

    Text text = new Text(messageFromClient);
    TextFlow textFlow = new TextFlow(text);
    textFlow.setStyle("-fx-background-color: #e6e6e6; -fx-background-radius: 10px; -fx-padding: 5px;");
    textFlow.setPadding(new Insets(5, 10, 5, 10));

    // Set the preferred width of TextFlow to prevent horizontal scrolling
    textFlow.setPrefWidth(sp_main.getViewportBounds().getWidth() - 20); // Adjust the value as needed

    hbox.getChildren().add(textFlow);

    // Platform to update the VBox later
    Platform.runLater(() -> {
        vbox.getChildren().add(hbox);
    });

    // Scroll to the bottom of the ScrollPane to show the latest message
    sp_main.setVvalue(1.0);
}

}
