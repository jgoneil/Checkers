package com.webcheckers.model;

import com.webcheckers.appl.Space;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Piece;

/**
 * Model class that holds the main board for model configurations
 */
public class ModelBoard {

  private Space[][] board;
  private Player redPlayer;
  private Player whitePlayer;

  /**
   * Constructor for the model version of the board
   *
   * @param redPlayer the player associated to the color red for the game
   * @param whitePlayer the player associated to the color white for the game
   * @param length the lenght of the sides of the board (assuming its a square)
   */
  public ModelBoard(Player redPlayer, Player whitePlayer, int length) {
    //Setting constants
    this.board = new Space[length][length];
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    //Preforming a loop to generate all of the spaces for the rows and columns of the board
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        //Checking to see if the space should be white or not
        if (i + j % 2 == 0) {
          board[i][j] = new Space(i, j, Space.Color.WHITE);
        } else {
          Space space = new Space(i, j, Space.Color.BLACK);
          board[i][j] = space;
          //completing a check to see if a piece should be added to the space (space must be black for this to happen)
          if (i >= 0 && i <= 2) {
            space.occupy(new Piece("white", space));
          } else if (i >= 5 && i <= 7) {
            space.occupy(new Piece("red", space));
          }
        }
      }
    }
  }

  /**
   * Getter for a specific space on the board
   *
   * @param xCoordinate the x-position for the space
   * @param yCoordinate the y-position for the space
   * @return the space located at the location provided by the coordinates
   */
  public Space getSpace(int xCoordinate, int yCoordinate) {
    return board[xCoordinate][yCoordinate];
  }

  /**
   * Changes occupancy for a space on the board to a specified piece
   *
   * @param piece the piece now occupying the board
   * @param space the space from the appl that the piece is moving to
   */
  public void addPieceToSpace(Piece piece, Space space) {
    Space goalSpace = board[space.getxCoordinate()][space.getCellIdx()];
    goalSpace.occupy(piece);
  }
}
