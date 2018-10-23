package com.webcheckers.model;

import com.webcheckers.appl.Piece.Type;
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
  //Holds all of the pieces for the redPlayer in the game
  private ArrayList<Piece> redPieces;
  //Holds all of the pieces for the whitePlayer in the game
  private ArrayList<Piece> whitePieces;
  //Checks if a Piece is being Kinged in a given move
  private boolean isKinging;

  /**
   * Constructor for the model version of the board
   *
   * @param redPlayer the player associated to the color red for the game
   * @param whitePlayer the player associated to the color white for the game
   * @param length the length of the sides of the board (assuming its a square)
   */
  public ModelBoard(Player redPlayer, Player whitePlayer, int length) {
    //Setting constants
    this.board = new Space[length][length];
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.redTurn = true;
    this.redPieces = new ArrayList<>();
    this.whitePieces = new ArrayList<>();
    this.isKinging = true;
    //Preforming a loop to generate all of the spaces for the rows and columns of the board
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        //Checking to see if the space should be white or not
        if ((i + j) % 2 == 0) {
          board[i][j] = new Space(i, j, Space.Color.WHITE);
        } else {
          Space space = new Space(i, j, Space.Color.BLACK);
          board[i][j] = space;
          //completing a check to see if a piece should be added to the space (space must be black for this to happen)
          if (i >= 0 && i <= 2) {
            Piece piece = new Piece("white", space);
            whitePieces.add(piece);
            space.occupy(piece);
          } else if (i >= 5 && i <= 7) {
            Piece piece = new Piece("red", space);
            redPieces.add(piece);
            space.occupy(piece);
          }
        }
      }
    }
  }

  /**
   * Constructor for the model version of the board
   *
   * @param redPlayer the player associated to the color red for the game
   * @param whitePlayer the player associated to the color white for the game
   * @param length the length of the sides of the board (assuming square)
   * @param pieces the pieces being added to the board
   */
  public ModelBoard(Player redPlayer, Player whitePlayer, int length, List<Piece> pieces) {
    this.board = new Space[length][length];
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.redTurn = true;
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
    Space endingSpace;
    if (redTurn) {
      endingSpace = board[move.getEnd().getRow()][move.getEnd().getCell()];
    } else {
      endingSpace = board[7 - move.getEnd().getRow()][7 - move.getEnd().getCell()];
    }
    isKinging = isBecomingKing(endingSpace.getPiece(), move.getEnd().getRow());
    if (isKinging){
      endingSpace.getPiece().King();
    }
    BoardView redBoardView = redPlayer.getBoardView();
    BoardView whiteBoardView = whitePlayer.getBoardView();
    Move reverseMove = new Move(
        new Position(7 - move.getStart().getRow(), 7 - move.getStart().getCell()),
        new Position(7 - move.getEnd().getRow(), 7 - move.getEnd().getCell()));
    if (this.redTurn) {
      redBoardView.makeMove(move, isKinging);
      whiteBoardView.makeMove(reverseMove, isKinging);
    } else {
      redBoardView.makeMove(reverseMove, isKinging);
      whiteBoardView.makeMove(move, isKinging);
    }
    this.redTurn = !redTurn;
    this.madeMove = false;
    this.isKinging = false;
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
   * Checks to see if a redPlayer can still play the game or not
   *
   * @return true/false based on if the amount of pieces a red player has equals 0
   */
  public boolean redCanPlay() {
    if (this.redPieces.size() == 0) {
      return false;
    } 
    return true;
  }

  /**
   * Checks to see if a whitePlayer can still play the game or not
   *
   * @return true/false based on if the aount of pieces a white player has equals 0
   */
  public boolean whiteCanPlay() {
    if (this.whitePieces.size() == 0) {
      return false;
    }
    return true;
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
   * Backup a move made by the player before submitting.
   *
   * Returns the game to the state before choosing a move.
   */
  public void backupMove() {
    if (madeMove) {
      Space startingSpace;
      Space endingSpace;
      if (redTurn) {
        startingSpace = board[move.getStart().getRow()][move.getStart().getCell()];
        endingSpace = board[move.getEnd().getRow()][move.getEnd().getCell()];
      } else {
        startingSpace = board[7 - move.getStart().getRow()][7 - move.getStart().getCell()];
        endingSpace = board[7 - move.getEnd().getRow()][7 - move.getEnd().getCell()];
      }
      Piece movingPiece = endingSpace.getPiece();
      endingSpace.unoccupy();
      startingSpace.occupy(movingPiece);
      madeMove = false;
      move = null;
    }
  }

  /**
   * Tests to see if a piece is able to King a piece
   * @param piece - Piece to be kinged
   * @param row - Row of the Piece being checked
   * @return - if the King is able to be Kinged
   */
  public boolean isBecomingKing(Piece piece, int row) {
    if (piece.getType().equals(Type.SINGLE)) {
      return row == 0;
    }
    return false;
  }
}
