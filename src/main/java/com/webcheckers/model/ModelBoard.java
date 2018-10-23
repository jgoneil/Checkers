package com.webcheckers.model;

import com.webcheckers.appl.Space;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class that holds the main board for model configurations
 */
public class ModelBoard {

  //The 2-D array holding the spaces on the board
  private Space[][] board;
  //The red player for the game
  private Player redPlayer;
  //The white player for the game
  private Player whitePlayer;
  //If a move has recently been made
  private boolean madeMove;
  //The move made
  private Move move;
  //Holds if the redPlayer has the active move or not
  private boolean redTurn;
  //Holds all pieces belonging to the red player
  private List<Piece> whitePieces = new ArrayList<>();
  //Holds all pieces belonging to the white player
  private List<Piece> redPieces = new ArrayList<>();

  private boolean tester = false;

  /**
   * Constructor for the model version of the board
   *
   * @param redPlayer   the player associated to the color red for the game
   * @param whitePlayer the player associated to the color white for the game
   * @param length      the lenght of the sides of the board (assuming its a square)
   */
  public ModelBoard(Player redPlayer, Player whitePlayer, int length) {
    //Setting constants
    this.board = new Space[length][length];
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.redTurn = true;
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
            Piece whitePiece = new Piece("white", space);
            space.occupy(whitePiece);
            whitePieces.add(whitePiece);
          } else if (i >= 5 && i <= 7) {
            Piece redPiece = new Piece("red", space);
            space.occupy(redPiece);
            redPieces.add(redPiece);
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
   * Sets the system to reflect a move has been made
   *
   * @param move the move made
   */
  public void madeMove(Move move) {
    this.madeMove = true;
    this.move = move;
  }

  /**
   * Submits a move for the game and change the active player
   */
  public void submitMove() {
    Space startingSpace = board[move.getStart().getRow()][move.getStart().getCell()];
    Space endingSpace = board[move.getEnd().getRow()][move.getEnd().getCell()];
    Piece movingPiece = startingSpace.getPiece();
    startingSpace.unoccupy();
    endingSpace.occupy(movingPiece);
    BoardView redBoardView = redPlayer.getBoardView();
    BoardView whiteBoardView = whitePlayer.getBoardView();
    Move reverseMove = new Move(new Position(7 - move.getStart().getRow(), 7 - move.getStart().getCell()),
            new Position(7 - move.getEnd().getRow(), 7 - move.getEnd().getCell()));
    if (this.redTurn) {
      redBoardView.makeMove(move);
      whiteBoardView.makeMove(reverseMove);
    } else {
      redBoardView.makeMove(reverseMove);
      whiteBoardView.makeMove(move);
    }
    this.redTurn = !redTurn;
    this.madeMove = false;
  }

  /**
   * Checks to see who's turn is currently active
   *
   * @return a boolean condition based on if it is currently the red player's turn or not
   */
  public boolean checkRedTurn() {
    return this.redTurn;
  }

  /**
   * Checks to see if a move has been mad or not
   *
   * @return a boolean condition based on if a move has been made or not
   */
  public boolean checkMadeMove() {
    return this.madeMove;
  }

  /**
   * Updates the system to certify a move has or has not been made (for testing)
   *
   * @param madeMove boolean condition based on if a move has been made or not
   */
  public void setMove(boolean madeMove) {
    this.madeMove = madeMove;
    this.redTurn = !redTurn;
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

  /**
   * Getter for the white players pieces
   *
   * @return An array holding the white pieces
   */
  public List<Piece> getWhitePieces() {
    return whitePieces;
  }

  /**
   * Getter for the red players pieces
   *
   * @return An array holding the red pieces
   */
  public List<Piece> getRedPieces() {
    return redPieces;
  }
}