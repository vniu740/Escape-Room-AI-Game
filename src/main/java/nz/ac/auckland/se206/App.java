package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Sets the scene to the input UI.
   *
   * @param newUi The UI to set the scene to.
   */
  public static void setUi(AppUi newUi) {
    scene.setRoot(SceneManager.getUi(newUi));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Adding rooms into the scenemanager hashmap
    SceneManager.addAppUi(AppUi.LAB, loadFxml("lab"));
    SceneManager.addAppUi(AppUi.DRAGON_ROOM, loadFxml("dragonRoom"));
    SceneManager.addAppUi(AppUi.MATCHING, loadFxml("matchgame"));
    SceneManager.addAppUi(AppUi.FOREST, loadFxml("forest"));
    SceneManager.addAppUi(AppUi.START_PAGE, loadFxml("menu"));
    SceneManager.addAppUi(AppUi.LOSE, loadFxml("lose"));
    SceneManager.addAppUi(AppUi.WIN, loadFxml("win"));
    SceneManager.addAppUi(AppUi.AICHAT, loadFxml("aichat"));

    // Parent root = SceneManager.getUi(AppUi.START_PAGE);
    Parent root = SceneManager.getUi(AppUi.START_PAGE);
    scene = new Scene(root, 600, 399);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  public static void restartGame() throws IOException {
    SceneManager.resetUiMap();
    GameState.reset();

    // Adding new room instances into the scenemanager hashmap
    SceneManager.addAppUi(AppUi.LAB, loadFxml("lab"));
    SceneManager.addAppUi(AppUi.DRAGON_ROOM, loadFxml("dragonRoom"));
    SceneManager.addAppUi(AppUi.MATCHING, loadFxml("matchgame"));
    SceneManager.addAppUi(AppUi.FOREST, loadFxml("forest"));
    SceneManager.addAppUi(AppUi.START_PAGE, loadFxml("menu"));
    SceneManager.addAppUi(AppUi.LOSE, loadFxml("lose"));
    SceneManager.addAppUi(AppUi.WIN, loadFxml("win"));
    SceneManager.addAppUi(AppUi.AICHAT, loadFxml("aichat"));

    setUi(AppUi.START_PAGE);
  }

}
