package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class potionRecipeManager {

  private static List<Image> imgScrollList = new ArrayList<Image>();

  public static List<Image> getImgScrollList() {
    return imgScrollList;
  }

  public static void setImageScrollList(List<Image> list) {
    imgScrollList = list;
  }
}
