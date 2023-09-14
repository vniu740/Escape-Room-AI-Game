package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Tile;

public class MatchingGameController {
  @FXML private VBox gameBox;
  @FXML private GridPane gameGrid;
  @FXML private ImageView tile1;
  @FXML private ImageView tile2;
  @FXML private ImageView tile3;
  @FXML private ImageView tile4;
  @FXML private ImageView tile5;
  @FXML private ImageView tile6;
  @FXML private ImageView tile7;
  @FXML private ImageView tile8;
  @FXML private ImageView tile9;
  @FXML private Button buttonBack;
  @FXML private Label counter;

  private Tile[][] gameBoard; // Represents the game board (3x3 grid)
  private ImageView[] tiles; // Array of tile ImageViews
  private int lastClickedTileIndex = -1; // Index of the last clicked tile
  private int lastrow = -1;
  private int lastcol = -1;

  // Initialize the game and set up event handlers
  public void initialize() throws IOException {

    // Initialize the game board with Tile objects
    gameBoard =
        new Tile[][] {
          {new Tile(1), new Tile(1), new Tile(4)},
          {new Tile(2), new Tile(2), new Tile(5)},
          {new Tile(3), new Tile(3), new Tile(6)}
        };

    // Shuffle the game board
    shuffleGameBoard();

    // Initialize the tile ImageViews
    tiles = new ImageView[] {tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9};

    // Set event handlers for tile clicks
    for (ImageView tile : tiles) {
      ImageView currentTile = tile; // Create a separate variable
      tile.setOnMouseClicked(event -> onTileClicked(event, currentTile));
    }
  }

  // Method to shuffle the positions of the tiles on the game board
  private void shuffleGameBoard() {
    List<Tile> tileList =
        Arrays.stream(gameBoard).flatMap(Arrays::stream).collect(Collectors.toList());
    Collections.shuffle(tileList);

    // Update the game board with the shuffled tiles
    int index = 0;
    for (Tile[] row : gameBoard) {
      for (int col = 0; col < row.length; col++) {
        row[col] = tileList.get(index);
        index++;
      }
    }
  }

  // Implement event handler for tile clicks
  @FXML
  public void onTileClicked(MouseEvent event, ImageView clickedTile) {
    int clickedTileIndex = -1;

    // Find the row and column coordinates of the clicked tile in the GridPane
    int row = GridPane.getRowIndex(clickedTile);
    int col = GridPane.getColumnIndex(clickedTile);

    // Find the index of the clicked tile in the tiles array
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i] == clickedTile) {
        clickedTileIndex = i;
        break;
      }
    }

    // Check if the clicked tile is already face up or if it's the same as the last clicked tile
    if (clickedTileIndex == lastClickedTileIndex || gameBoard[row - 1][col - 1].isFaceUp()) {
      return; // Do nothing
    }

    // Flip the clicked tile
    Tile clickedGameTile = gameBoard[row - 1][col - 1];
    clickedGameTile.flip();
    int tileValue = clickedGameTile.getValue();

    // Set the image based on the tile value when it's flipped up
    String tileImageName = "/images/questionBox.png"; // Default image path
    if (tileValue == 1) {
      tileImageName = "/images/tileVal1.jpg";
    } else if (tileValue == 2) {
      tileImageName = "/images/tileVal2.jpg";
    } else if (tileValue == 3) {
      tileImageName = "/images/tileVal3.jpg";
    } else if (tileValue == 4) {
      tileImageName = "/images/tileVal4.jpg";
    } else if (tileValue == 5) {
      tileImageName = "/images/tileVal5.jpg";
    } else if (tileValue == 6) {
      tileImageName = "/images/tileVal6.jpg";
    }

    Image tileImage = new Image(tileImageName);
    clickedTile.setImage(tileImage);

    // Create a fade in transition for the clicked tile
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), clickedTile);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();

    // print the value of the clicked tile
    System.out.println("Clicked tile value: " + tileValue);

    // Check for a match with the last clicked tile
    if (lastClickedTileIndex != -1) {
      Tile lastTileValue = gameBoard[lastrow - 1][lastcol - 1];
      if (tileValue == lastTileValue.getValue()) {
        // Handle matching tiles (e.g., set them as matched)
        clickedGameTile.setMatched(true);
        lastTileValue.setMatched(true);
        GameState.dragonMatches++;

        // Display how many the user has found
        counter.setText(Integer.toString(GameState.dragonMatches) + " " + "out of 3");
        System.out.println("Matches: " + GameState.dragonMatches);
      } else {

        // Handle non-matching tiles (e.g., flip them back face down)
        clickedGameTile.flip();
        lastTileValue.flip();

        System.out.println("Not matched");

        showDialog(
            "Not a match",
            "Not a match",
            "The tiles you selected are not a match. Please try again.");

        tiles[clickedTileIndex].setImage(new Image("/images/questionBox.png"));
        tiles[lastClickedTileIndex].setImage(new Image("/images/questionBox.png"));
      }
      lastClickedTileIndex = -1; // Reset the last clicked tile index
    } else {
      lastClickedTileIndex = clickedTileIndex; // Set the last clicked tile index
      lastrow = row;
      lastcol = col;
    }
    if (GameState.dragonMatches == 3) {
      GameState.isMatchGameWon = true;
      System.out.println("Game won");
    }
  }

  @FXML
  private void onBackClicked() throws IOException {
    // Go back to the Dragon Room
    App.setUi(AppUi.DRAGON_ROOM);
  }

  private void showDialog(String title, String headerText, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
