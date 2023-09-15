package nz.ac.auckland.se206;

public class Tile {
  private int value;
  private boolean faceUp;
  private boolean matched;

  public Tile(int value) {
    this.value = value;
    this.faceUp = false;
    this.matched = false;
  }

  public int getValue() {
    return value;
  }

  public boolean isFaceUp() {
    return faceUp;
  }

  public void flip() {
    faceUp = !faceUp;
  }

  public boolean isMatched() {
    return matched;
  }

  public void setMatched(boolean matched) {
    this.matched = matched;
  }
}
