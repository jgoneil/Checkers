package com.webcheckers.model;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Piece.Color;
import com.webcheckers.model.Piece.Type;

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
  //Holds all red Pieces
  private List<Piece> redPieces = new ArrayList<>();
  //Holds all white Pieces
  private List<Piece> whitePieces = new ArrayList<>();
  //Checks if a Piece is being Kinged in a given move
  private boolean isKinging;
  private PlayerBoardView redPlayerBoardView;
  //Holds the information about the whitePlayerBoardView
  private PlayerBoardView whitePlayerBoardView;
  //If a player has found a valid move to make
  private boolean pendingMove;
  //Checks if a jump action is being made
  private boolean isJumping;

  /**
   * Constructor for the model version of the board
   *
   * @param redPlayer the player associated to the color red for the game
   * @param whitePlayer the player associated to the color white for the game
   * @param length the length of the sides of the board (assuming its a square)
   */
  public ModelBoard(Player redPlayer, Player whitePlayer, int length) {
    //Setting constants
    this.redPlayerBoardView = new PlayerBoardView(redPlayer, whitePlayer, length, GameLobby.RED);
    this.whitePlayerBoardView = new PlayerBoardView(redPlayer, whitePlayer, length, GameLobby.WHITE);
    this.board = new Space[length][length];
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.redTurn = true;
    this.isKinging = true;
    this.pendingMove = false;
    this.isJumping = false;
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
            Piece whitePiece = new Piece(GameLobby.WHITE, space);
            space.occupy(whitePiece);
            whitePieces.add(whitePiece);
          } else if (i >= 5 && i <= 7) {
            Piece redPiece = new Piece(GameLobby.RED, space);
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
   * Getter for the player board view for the red player
   *
   * @return the playerBoardView for the red player
   */
  public PlayerBoardView getRedPlayerBoardView() {
    return this.redPlayerBoardView;
  }

  /**
   * Getter for the player board view for the white player
   *
   * @return the playerBoardVire for the white player
   */
  public PlayerBoardView getWhitePlayerBoardView() {
    return this.whitePlayerBoardView;
  }

  /**
   * Sets the system to reflect a move has been validated but not submitted
   */
  public void pendingMove(Move move) {
    this.pendingMove = true;
    this.move = move;
  }

  /**
   * Sets the pendingMove boolean to the parameter value (used for testing)
   *
   * @param madeMove true/false based on what pendingMove should be set to
   */
  public void setPendingMove(boolean madeMove) {
    this.pendingMove = madeMove;
  }

  /**
   * Sets the system to state a move was made
   *
   * @param move the move that was made
   */
  public void madeMove(Move move) {
    this.madeMove = true;
    this.move = move;
  }

  /**
   * Sets the system to show if a move is an attempted jump or not
   *
   * @param jumping true/false based on if the player is attempting to jump a piece or not
   */
  public void isJumping(boolean jumping) {
    this.isJumping = jumping;
  }

  /**
   * Checks to see if a move is currently pending
   *
   * @return true/false based on if a player has a pending move
   */
  public boolean checkPendingMove() {
    return this.pendingMove;
  }

  /**
   * Submits a move for the game and change the active player
   */
  public void submitMove() {
    Space current;
    Space endingSpace;
    if (redTurn) {
      current = board[move.getStart().getRow()][move.getStart().getCell()];
      endingSpace = board[move.getEnd().getRow()][move.getEnd().getCell()];
    } else {
      current = board[7 - move.getStart().getRow()][7 - move.getStart().getCell()];
      endingSpace = board[7 - move.getEnd().getRow()][7 - move.getEnd().getCell()];
    }
    isKinging = isBecomingKing(current.getPiece(), move.getEnd().getRow());
    if (isKinging){
      current.getPiece().King();
    }
    if (isJumping) {
      addPieceToSpace(current.getPiece(), endingSpace);
      current.unoccupy();
      if (endingSpace.getPiece().getColor() == Color.RED) {
        Space middle = getSpace((current.getxCoordinate() + endingSpace.getxCoordinate()) / 2,
                (current.getCellIdx() + endingSpace.getCellIdx()) / 2);
        eatPiece(middle.getPiece());
      } else {
        Space middle = getSpace((current.getxCoordinate() + endingSpace.getxCoordinate()) / 2,
                (current.getCellIdx() + endingSpace.getCellIdx()) / 2);
        eatPiece(middle.getPiece());
      }
    } else {
      addPieceToSpace(current.getPiece(), endingSpace);
      current.unoccupy();
    }
    Move reverseMove = new Move(
        new Position(7 - move.getStart().getRow(), 7 - move.getStart().getCell()),
        new Position(7 - move.getEnd().getRow(), 7 - move.getEnd().getCell()));
    if (this.redTurn) {
      redPlayerBoardView.makeMove(move, isKinging);
      whitePlayerBoardView.makeMove(reverseMove, isKinging);
    } else {
      redPlayerBoardView.makeMove(reverseMove, isKinging);
      whitePlayerBoardView.makeMove(move, isKinging);
    }
    this.redTurn = !redTurn;
    this.madeMove = false;
    this.isKinging = false;
    this.pendingMove = false;
    this.move = null;
    this.isJumping = false;
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
    Space goalSpace = this.board[space.getxCoordinate()][space.getCellIdx()];
    piece.move(goalSpace);
    goalSpace.occupy(piece);
  }

  /**
   * Backup a move made by the player before submitting.
   *
   * Returns the game to the state before choosing a move.
   */
  public void backupMove() {
    if (pendingMove) {
      madeMove = false;
      move = null;
      pendingMove = false;
    }
  }

  /**
   * Gets a list of all red pieces
   *
   * @return A list of red pieces
   */
  public List<Piece> getRedPieces(){
    return redPieces;
  }

  /**
   * Gets a list of all white pieces
   *
   * @return A list of white pieces
   */
  public List<Piece> getWhitePieces() {
    return whitePieces;
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

  /**
   * Removes a piece from all 3 boards after a jump occurs
   *
   * @param piece The piece that is being jumped/ate
   */
  public void eatPiece(Piece piece){
    if (piece.getColor().equals(Color.RED)){
      redPieces.remove(piece);
    } else {
      whitePieces.remove(piece);
    }
    piece.getSpace().unoccupy();

    redPlayerBoardView.eatPiece(piece.getSpace().getxCoordinate(), piece.getSpace().getCellIdx());
    whitePlayerBoardView.eatPiece(7-piece.getSpace().getxCoordinate(), 7-piece.getSpace().getCellIdx());
  }
}
