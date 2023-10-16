package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

/** Class that manages scenes in the app. */
public class SceneManager {

  /** Creates the enums for the possible scenes. */
  public enum AppUi {
    ROOM,
    CHAT,
    START_PAGE,
    FOREST,
    DRAGON_ROOM,
    MATCHING,
    LAB,
    LOSE,
    WIN,
    AICHAT,
    SETTINGS;
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addAppUi(AppUi ui, Parent root) {
    sceneMap.put(ui, root);
  }

  public static Parent getUi(AppUi ui) {
    return sceneMap.get(ui);
  }

  public static void resetUiMap() {
    sceneMap.clear();
  }
}
