package com.webcheckers.model;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Piece.Type;

import java.util.*;

/**
 * Model class that holds the main board for model configurations
 */
public class ModelBoard {

  //The 2-D array holding the spaces on the board
  private Space[][] board;
  //If a move has recently been made
  private boolean madeMove;
  //Holds if the redPlayer has the active move or not
  private boolean redTurn;
  //Holds all red Pieces
  private List<Piece> redPieces;
  //Holds all white Pieces
  private List<Piece> whitePieces;
  //Checks if a Piece is being Kinged in a given move
  private boolean isKinging;
  //Holds the information about the redPlayerBoard
  private final PlayerBoard redPlayerBoard;
  //Holds the information about the whitePlayerBoard
  private final PlayerBoard whitePlayerBoard;
  //If a player has found a valid move to make
  private boolean pendingMove;
  //Checks if a jump action is being made
  private boolean isJumping;
  //Checks if submission can happen
  private boolean canSubmit = false;
  //Stack of all pending moves waiting to be submitted on a player's turn
  private Stack<Move> pendingMoves;

  /**
   * Constructor for the model version of the board
   *
   * @param redPlayer   the player associated to the color red for the game
   * @param whitePlayer the player associated to the color white for the game
   * @param length      the length of the sides of the board (assuming its a square)
   */
  public ModelBoard(Player redPlayer, Player whitePlayer, int length) {
    //Setting constants
    this.redPlayerBoard = new PlayerBoard(redPlayer, whitePlayer, length, GameLobby.RED);
    this.whitePlayerBoard = new PlayerBoard(redPlayer, whitePlayer, length, GameLobby.WHITE);
    this.board = new Space[length][length];
    this.redTurn = true;
    this.redPieces = new ArrayList<>();
    this.whitePieces = new ArrayList<>();
    this.pendingMoves = new Stack<>();
    this.isKinging = true;
    this.pendingMove = false;
    this.isJumping = false;
    this.pendingMoves = new Stack<>();
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
          if (i <= 2) {
            Piece whitePiece = new Piece(GameLobby.WHITE, space);
            space.occupy(whitePiece);
            whitePieces.add(whitePiece);
          } else if (i >= 5 && i <= 7) {
            Piece redPiece = new Piece(GameLobby.RED, space);
            space.occupy(redPiece);
            redPieces.add(redPiece);
          }
          board[i][j] = space;
        }
      }
    }
  }

  /**
   * Constructor for the model version of the board
   *
   * @param redPlayer   the player associated to the color red for the game
   * @param whitePlayer the player associated to the color white for the game
   * @param length      the length of the sides of the board (assuming square)
   * @param pieces      the pieces being added to the board
   */
  public ModelBoard(Player redPlayer, Player whitePlayer, int length, List<Piece> pieces) {
    //Setting constants
    this.board = new Space[length][length];
    this.redTurn = true;
    this.redPlayerBoard = new PlayerBoard(redPlayer, whitePlayer, length, GameLobby.RED);
    this.whitePlayerBoard = new PlayerBoard(redPlayer, whitePlayer, length, GameLobby.WHITE);
    this.redPieces = new ArrayList<>();
    this.whitePieces = new ArrayList<>();
    this.isKinging = true;
    this.pendingMoves = new Stack<>();
    //Preforming a loop to generate all of the spaces for the board
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < length; j++) {
        if ((i + j) % 2 == 0) {
          board[i][j] = new Space(i, j, Space.Color.WHITE);
        } else {
          board[i][j] = new Space(i, j, Space.Color.BLACK);
        }
      }
    }
    for (Piece p : pieces) {
      Space space = board[p.getXCoordinate()][p.getCellIdx()];
      p.move(space);
      space.occupy(p);
      if (p.isRed()) {
        this.redPieces.add(p);
      } else {
        this.whitePieces.add(p);
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
    if (xCoordinate > 7 || xCoordinate < 0) {
      return null;
    } else if (yCoordinate > 7 || yCoordinate < 0) {
      return null;
    }
    return board[xCoordinate][yCoordinate];
  }

  /**
   * Getter for the player board view for the red player
   *
   * @return the playerBoardView for the red player
   */
  public PlayerBoard getRedPlayerBoard() {
    return this.redPlayerBoard;
  }

  /**
   * Getter for the player board view for the white player
   *
   * @return the playerBoardVire for the white player
   */
  public PlayerBoard getWhitePlayerBoard() {
    return this.whitePlayerBoard;
  }

  /**
   * Sets the system to reflect a move has been validated but not submitted
   *
   * @param move the move that is pending
   */
  public void pendingMove(Move move) {
    this.pendingMove = true;
    this.pendingMoves.push(move);
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
   * Tells the system that a move can or can't be submitted
   *
   * @param submit true/false based on if a move can or cannot be submitted
   */
  public void setSubmit(boolean submit) {
    this.canSubmit = submit;
  }

  /**
   * Checks to see if a move can be submitted
   *
   * @return true/false based on if a move can be submitted
   */
  public boolean canSubmit() {
    return canSubmit;
  }

  /**
   * Clear all of the pending moves from the board
   */
  public void clearPendingMove() {
    this.pendingMove = false;
    this.pendingMoves.clear();
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
   * Change whos turn it is for the game
   */
  public void changeTurn() {
    this.redTurn = !redTurn;
  }

  /**
   * Submits a move for the game and change the active player
   */
  public void submitMove() {
    Space current;
    Space endingSpace;
    while (!this.pendingMoves.empty()) {
      Move firstMove = this.pendingMoves.get(0);
      this.pendingMoves.remove(firstMove);
      if (redTurn) {
        current = board[firstMove.getStart().getRow()][firstMove.getStart().getCell()];
        endingSpace = board[firstMove.getEnd().getRow()][firstMove.getEnd().getCell()];
      } else {
        current = board[7 - firstMove.getStart().getRow()][7 - firstMove.getStart().getCell()];
        endingSpace = board[7 - firstMove.getEnd().getRow()][7 - firstMove.getEnd().getCell()];
      }
      isKinging = isBecomingKing(current.getPiece(), firstMove.getEnd().getRow());
      if (isKinging) {
        current.getPiece().King();
      }
      if (isJumping) {
        addPieceToSpace(current.getPiece(), endingSpace);
        current.unoccupy();
        Space middle = board[(current.getxCoordinate() + endingSpace.getxCoordinate()) / 2][
                (current.getCellIdx() + endingSpace.getCellIdx()) / 2];
        eatPiece(middle.getPiece());
      } else {
        addPieceToSpace(current.getPiece(), endingSpace);
        current.unoccupy();
      }
      Move actualMove = new Move(firstMove.getStart(), firstMove.getEnd());
      Move reverseMove = new Move(
              new Position(7 - firstMove.getStartRow(), 7 - firstMove.getStartCell()),
              new Position(7 - firstMove.getEndRow(), 7 - firstMove.getEndCell()));
      if (this.redTurn) {
        redPlayerBoard.makeMove(actualMove, isKinging);
        whitePlayerBoard.makeMove(reverseMove, isKinging);
      } else {
        redPlayerBoard.makeMove(reverseMove, isKinging);
        whitePlayerBoard.makeMove(actualMove, isKinging);
      }
    }
    //Resetting values and cases
    if(!whitePlayerBoard.isAI()) {
      this.redTurn = !redTurn;
      this.madeMove = false;
      this.isKinging = false;
      this.pendingMove = false;
      this.isJumping = false;
      this.canSubmit = false;
    }
    else {
      AbstractPlayer AI = whitePlayerBoard.getWhitePlayer();
    }
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
   * @return true/false based on if the amount of pieces a white player has equals 0
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
    Space goalSpace = this.board[space.getxCoordinate()][space.getCellIdx()];
    piece.move(goalSpace);
    goalSpace.occupy(piece);
  }

  /**
   * Removes a piece from a space and assigns it to a different space. Reverses an addition of a piece to a space
   *
   * @param currentSpace the space the piece is moving to
   * @param endSpace     the space the piece is on
   */
  public void removePieceFromSpace(Space currentSpace, Space endSpace) {
    Space current = this.board[currentSpace.getxCoordinate()][currentSpace.getCellIdx()];
    Space goal = this.board[endSpace.getxCoordinate()][endSpace.getCellIdx()];
    Piece piece = current.getPiece();
    goal.unoccupy();
    piece.move(current);
  }

  /**
   * Backup a move made by the player before submitting.
   * <p>
   * Returns the game to the state before choosing a move.
   */
  public void backupMove() {
    if (pendingMove) {
      madeMove = false;
      if (pendingMoves.size() == 1) {
        pendingMoves.clear();
        pendingMove = false;
      } else {
        pendingMoves.pop();
        pendingMove = true;
      }
    }
    this.canSubmit = false;
  }

  /**
   * Gets a list of all red pieces
   *
   * @return A list of red pieces
   */
  public List<Piece> getRedPieces() {
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
   *
   * @param piece - Piece to be kinged
   * @param row   - Row of the Piece being checked
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
  public void eatPiece(Piece piece) {
    if (piece.isRed()) {
      redPieces.remove(piece);
    } else {
      whitePieces.remove(piece);
    }
    piece.getSpace().unoccupy();
    redPlayerBoard.eatPiece(piece.getXCoordinate(), piece.getCellIdx());
    whitePlayerBoard.eatPiece(7 - piece.getXCoordinate(), 7 - piece.getCellIdx());
  }

  /**
   * Checks to see what move is currently pending (gets the most recently added pending move)
   *
   * @return the move pending on the system
   */
  public Move getPendingMove() {
    return this.pendingMoves.peek();
  }
}
