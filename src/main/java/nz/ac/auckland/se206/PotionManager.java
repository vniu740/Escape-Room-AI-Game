package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class PotionManager {
  private static List<Image> imgScrollList = new ArrayList<Image>();
  private static List<Image> forestObjectList = new ArrayList<Image>();
  private static List<Image> dragonObjectList = new ArrayList<Image>();

  public static List<Image> getImgScrollList() {
    return imgScrollList;
  }

  public static void setImageScrollList(List<Image> list) {
    imgScrollList = list;
  }

  public static void setForestObjectList(List<Image> list) {
    forestObjectList = list;
  }

  public static List<Image> getForestObjectList() {
    return forestObjectList;
  }

    public static void setDragonObjectList(List<Image> list) {
    dragonObjectList = list;
  }

  public static List<Image> getDragonObjectList() {
    return dragonObjectList;
  }
}
