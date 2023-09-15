package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

/** Controller class for the chat view. */
public class ChatController {

  @FXML private TextArea chatTextArea;
  @FXML private TextField inputText;
  @FXML private Button btnGoBack;
  @FXML private Button sendButton;
  @FXML
  private ImageView imgView;
 @FXML private Label timerLbl;
  

  private PathTransition pathTransition;
  private ChatCompletionRequest chatCompletionRequest;

  /**
   * Initializes the chat view, loading the riddle.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    inputText.setEditable(false);
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
            btnGoBack.setDisable(true);
                runGpt(
                    new ChatMessage("user", GptPromptEngineering.getRiddleWithGivenWord("potion")));
            inputText.clear();
            inputText.setEditable(true);
            btnGoBack.setDisable(false);
            return null;
          }
        };

    Thread riddleThread = new Thread(getRiddleTask, "Riddle Thread");
    riddleThread.start();
    createPathTransition(imgView);
    pathTransition.play();
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    if (msg.getRole().equals("assistant")) {
      chatTextArea.appendText("Jewellery Box: " + msg.getContent() + "\n\n");

    } else {
      chatTextArea.appendText(msg.getRole() + ": " + msg.getContent() + "\n\n");
    }
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      Platform.runLater(
          () -> {
            pathTransition.pause();
            imgView.setVisible(false);
            appendChatMessage(result.getChatMessage());
          });

      return result.getChatMessage();
    } catch (ApiProxyException e) {
      System.out.println("Problem calling API: " + e.getMessage());
      return null;
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    // if the input is empty, clear the text field and don't run GPT
    String message = inputText.getText();
    if (message.trim().isEmpty()) {
      return;
    }
    inputText.clear();
    // Create a new task that will send the users input to GPT by calling the runGPT method
    imgView.setVisible(true);
    pathTransition.play();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    inputText.clear();
    // Make it so the user cannot edit the text field while their msg is sent to GPT
    inputText.setEditable(false);
    btnGoBack.setDisable(true);
    Task<Void> sendChatMessageTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            ChatMessage lastMsg = runGpt(msg);
            // Save the users input message and make it visible on the text area

            inputText.setEditable(true);
            btnGoBack.setDisable(false);
            // If the users input is correct, update the gameState
            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().startsWith("Correct")) {
              GameState.isRiddleResolved = true;
              inputText.setEditable(false);
            }

            return null;
          }
        };
    // Create a new thread that will run this task and tell it to start
    Thread sendChatMessageThread = new Thread(sendChatMessageTask, "Send Chat Message Thread");
    sendChatMessageThread.start();
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUi(AppUi.LAB));
  }

  private void createPathTransition(ImageView ImageView) {
    Path path = new Path();
    path.getElements().add(new MoveTo(70, 110));
    path.getElements().add(new LineTo(331, 110));
    path.getElements().add(new LineTo(70, 110));
    path.getElements().add(new LineTo(331, 110));
    path.getElements().add(new LineTo(70, 110));

    pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.millis(7000));
    pathTransition.setNode(ImageView);
    pathTransition.setPath(path);
    pathTransition.setOrientation(PathTransition.OrientationType.NONE);
    pathTransition.setAutoReverse(true);
    pathTransition.setCycleCount(PathTransition.INDEFINITE);
  }
}
