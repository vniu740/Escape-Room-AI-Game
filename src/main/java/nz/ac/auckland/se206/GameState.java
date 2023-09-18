package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Indicates whether the lever is pulled */
  public static boolean isLeverPulled = false;

  /** Indicates how many matches have occured in the dragon match game */
  public static int dragonMatches = 0;

  /** Indicates whether the dragon match game is won */
  public static boolean isMatchGameWon = false;

  /** Indicates whether the user is matching tiles */
  public static boolean matching = false;

  /** Indicates whether the user has collected the dragon scale */
  public static boolean isScaleCollected = false;

  /** Indicates whether the user has fished up the correct ingredient */
  public static boolean isFishingComplete = false;

   /** Indicates whether the user has collected the correct ingredient for the forest Room */
  public static boolean isForestCollected = false;

  /** Indicates whether the user has collected the correct ingredient for the lab Room */
  public static boolean isLabCollected = false;
}
