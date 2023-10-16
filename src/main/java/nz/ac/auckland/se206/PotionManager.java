package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Class that manages the potion recipe.
 * 
 */
public class PotionManager {
  private static List<Image> imgScrollList = new ArrayList<Image>();
  private static List<Image> forestObjectList = new ArrayList<Image>();
  private static List<Image> dragonObjectList = new ArrayList<Image>();

  /**
   * Getter method for the ArrayList imgScrollList.
   *
   * @return the static imgScrollList
   */
  public static List<Image> getImgScrollList() {
    return imgScrollList;
  }

  /**
   * Setter method for the ArrayList imgScrollList.
   *
   * @param list list that will be set
   */
  public static void setImageScrollList(List<Image> list) {
    imgScrollList = list;
  }

  /**
   * Setter method for the ArrayList forestObjectList.
   *
   * @param list list that will be set
   */
  public static void setForestObjectList(List<Image> list) {
    forestObjectList = list;
  }

  /**
   * Getter method for the ArrayList forestObjectList.
   *
   * @return forestObjectList
   */
  public static List<Image> getForestObjectList() {
    return forestObjectList;
  }

  /**
   * Setter method for the ArrayList dragonObjectList.
   *
   * @param list dragonObjectList
   */
  public static void setDragonObjectList(List<Image> list) {
    dragonObjectList = list;
  }

  /**
   * Getter method for the ArrayList dragonObjectList.
   *
   * @return dragonObjectList
   */
  public static List<Image> getDragonObjectList() {
    return dragonObjectList;
  }
}
