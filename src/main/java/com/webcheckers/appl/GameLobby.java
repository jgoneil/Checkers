package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.Map;
import java.util.List;

/**
 * Class to construct a new game lobby
 */
public class GameLobby {

  //The red player connected to the game
  private final AbstractPlayer redPlayer;
  //The white player connected to the game
  private final AbstractPlayer whitePlayer;
  //The model board being created for the game logic
  private ModelBoard modelBoard;
  //The check Move class to check if a move is valid or not
  private CheckMove checkMove;
  //The find best move class to find the best potential move for help requests for the red player
  private FindBestMove findBestMoveRed;
  //The find best move class to find the best potential move for help requests for the white player and for the ai player
  private FindBestMove findBestMoveWhite;
  //The best move a player can make
  private Move bestMove;

  //Static final (constant) variables
  private static final int BOARD_SIZE = 8;
  public static final String RED = "RED";
  public static final String WHITE = "WHITE";

  /**
   * Constructor for the Game Lobby. Establishes all of the boards for the game
   *
   * @param redPlayer the red player connected to the game lobby
   * @param whitePlayer the white player connected to the game lobby
   */
  public GameLobby(AbstractPlayer redPlayer, AbstractPlayer whitePlayer) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    redPlayer.setColor(RED);
    whitePlayer.setColor(WHITE);
    this.modelBoard = new ModelBoard(redPlayer, whitePlayer, BOARD_SIZE);
    this.checkMove = new CheckMove(this.modelBoard);
    this.findBestMoveRed = new FindBestMove(modelBoard, redPlayer);
    this.findBestMoveWhite = new FindBestMove(modelBoard, whitePlayer);
    this.bestMove = findBestMoveRed.findMove();
  }

  /**
   * Constructor for the game lobby that specifies the pieces being added to the board
   *  @param redPlayer the red player connected to the game lobby
   * @param whitePlayer the white player connected to the game lobby
   * @param pieceList the list of pieces being added to the board
   */
  public GameLobby(AbstractPlayer redPlayer, AbstractPlayer whitePlayer, List<Piece> pieceList) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    redPlayer.setColor(RED);
    whitePlayer.setColor(WHITE);
    this.modelBoard = new ModelBoard(redPlayer, whitePlayer, BOARD_SIZE, pieceList);
    this.checkMove = new CheckMove(this.modelBoard);
    this.findBestMoveRed = new FindBestMove(modelBoard, redPlayer);
    this.findBestMoveWhite = new FindBestMove(modelBoard, whitePlayer);
    this.bestMove = findBestMoveRed.findMove();
  }

  /**
   * Getter for the PlayerBoard for the red player (board shown in the rendered HTML page)
   *
   * @return the PlayerBoard associated to the red player for the active game session
   */
  public PlayerBoard getRedPlayerBoard() {
    return modelBoard.getRedPlayerBoard();
  }

  /**
   * Getter for the PlayerBoard for the white player (board shown in the rendered HTML page)
   *
   * @return the PlayerBoard associated to the white player for the active game session
   */
  public PlayerBoard getWhiteBoard() {
    return modelBoard.getWhitePlayerBoard();
  }

  /**
   * Getter for the PlayerBoard for the specified player (board shown in the rendered HTML page)
   *
   * @param player the player attempting to retrieve their PlayerBoard
   * @return the PlayerBoard associated to the specified player for the active game session
   */
  public PlayerBoard getBoardForPlayer(AbstractPlayer player) {
    if (this.redPlayer.equals(player)) {
      return modelBoard.getRedPlayerBoard();
    } else if (this.whitePlayer.equals(player)){
      return modelBoard.getWhitePlayerBoard();
    } else {
      return null;
    }
  }

  /**
   * Checks to see if a current player is in the game session or not
   *
   * @param username the username of the potential player
   * @return true/false based on if the username entered matches the red or white player's username
   */
  public boolean verifyInGame(String username) {
    if (username.equals(redPlayer.getName())) {
      return redPlayer.inGame();
    }
    else if( username.equals(whitePlayer.getName())) {
      return whitePlayer.inGame();
    }
    return false;
  }

  /**
   * Getter for the Player associated to the red side of the board
   *
   * @return the player associated to the red side of the board
   */
  public AbstractPlayer getRedPlayer() {
    return this.redPlayer;
  }

  /**
   * Checks to see if a red player can keep playing the game
   *
   * @return true/false if the red player can continue to play
   */
  public boolean redCanPlay() {
    if (this.redPlayer.getColor() == null) {
      return false;
    }
    if (this.checkMove.moveAvailable(redPlayer)) {
      return this.modelBoard.redCanPlay();
    }
    return false;
  }

  /**
   * Checks to see if a white player can keep playing the game
   *
   * @return true/false if the white player can continue to play
   */
  public boolean whiteCanPlay() {
    if (this.whitePlayer.getColor() == null) {
      return false;
    }
    if (this.checkMove.moveAvailable(whitePlayer)) {
      return this.modelBoard.redCanPlay();
    }
    return false;
  }

  /**
   * Getter for the Player associated to the white side of the board
   *
   * @return the player associated to the white side of the board
   */
  public AbstractPlayer getWhitePlayer() {
    return this.whitePlayer;
  }

  /**
   * Gets the opponent of a specified player for the game session
   *
   * @param player the player who is trying to find the opponent
   * @return the player that is the specified player's opponent
   */
  public AbstractPlayer getOpponent(AbstractPlayer player) {
    if(redPlayer.equals(player)) {
      return whitePlayer;
    }
    return redPlayer;
  }

  /**
   * Changes the turn for the game
   */
  public void changeTurn() {
    this.modelBoard.changeTurn();
  }

  /**
   * Gets the best move possible for a player to make
   *
   * @return the move that would be the best for the player to make
   */
  public Move getBestMove() {
    return this.bestMove;
  }

  /**
   * Checks to see if there is only one move possible for a player to make
   *
   * @return true/false based on if there is only one move possible for the player to make
   */
  public boolean onlyOne() {
    if (this.modelBoard.checkRedTurn()) {
      return findBestMoveRed.onlyOneMove();
    } else {
      return findBestMoveWhite.onlyOneMove();
    }
  }

  /**
   * Checks to see if a specified player is the red player for the game session
   *
   * @param player the player being checked
   * @return true/false if the red player is the specified player
   */
  public boolean checkRedPlayer(String player) {
    return player.equals(redPlayer.getName());
  }

  /**
   * Getter for who's turn it currently is
   *
   * @return the string representing who's turn it currently is in the game
   */
  public String getTurn() {
    if (modelBoard.checkRedTurn()) {
      return RED;
    }
    return WHITE;
  }

  /**
   * Checks to see if the turn is currently the red player's turn or not
   *
   * @return true/false based on if the turn is the red player's or not
   */
  public boolean checkRedTurn() {
    return modelBoard.checkRedTurn();
  }

  /**
   * Checks to see if a move has been made on the board or not
   *
   * @return true/false based on if a move has been submitted or not
   */
  public boolean checkMadeMove() {
    return this.modelBoard.checkMadeMove();
  }

  /**
   * Sets the pendingMove variable to an inputted value
   *
   * @param movePending true/false depending on what the pendingMove variable should be set to
   */
  public void setPendingMove(boolean movePending) {
    this.modelBoard.setPendingMove(movePending);
  }

  /**
   * Clears the pendingMoves on the model board
   */
  public void clearPendingMove() {
    this.modelBoard.clearPendingMove();
  }

  /**
   * Checks to see if a move has happened but is pending or not
   *
   * @return true/false based on if a move is pending
   */
  public boolean checkPendingMove() {
    return this.modelBoard.checkPendingMove();
  }

  /**
   * Sets a move to be pending for the board til the player submits or backs up the move
   *
   * @param move the move that is waiting to be backed up or submitted
   */
  public void pendingMove(Move move) {
    this.modelBoard.pendingMove(move);
  }

  /**
   * Checks to see if a move can or cannot be submitted
   *
   * @return true/false if a move can or cannot be submitted
   */
  public boolean canSubmit() {
    return this.modelBoard.canSubmit();
  }

  /**
   * Sets if a move can be submitted or not
   *
   * @param submit true/false based on if a move can or cannot be submitted
   */
  public void setCanSubmit(boolean submit) {
    this.modelBoard.setSubmit(submit);
  }

  /**
   * Validates the move a player is attempting to make
   *
   * @param start the starting position for the move
   * @param target the ending position for the move
   * @param player the player making the move
   * @return true/false based on if the player can make the move or not
   */
  public Map<Boolean, String> validateMove(Position start, Position target, AbstractPlayer player) {
    return this.checkMove.validateMove(start, target, player);
  }

  /**
   * Submits the pending move for the player and ends the player's turn
   */
  public void submitMove() {
    this.modelBoard.submitMove();
    if (this.modelBoard.checkRedTurn()) {
      this.bestMove = findBestMoveRed.findMove();
    } else if (whitePlayer.isAI()) {
      this.bestMove = findBestMoveWhite.findMove();
      if (bestMove != null) {
        Position start = new Position(bestMove.getStartRow(), bestMove.getStartCell());
        Position end = new Position(bestMove.getEndRow(), bestMove.getEndCell());
        List<Position> moves = findBestMoveWhite.findMiddle(modelBoard.getSpace(7 - start.getRow(), 7 - start.getCell()),
                modelBoard.getSpace(7 - end.getRow(), 7 - end.getCell()));
        if (moves != null) {
          for (int i = 0; i < moves.size() - 1; i++) {
            Position startingPosition = moves.get(i);
            Position movingPosition = moves.get(i + 1);
            Position fixedStartingPosition = new Position(7 - startingPosition.getRow(), 7 - startingPosition.getCell());
            Position fixedEndingPosition = new Position(7 - movingPosition.getRow(), 7 - movingPosition.getCell());
            checkMove.validateMove(fixedStartingPosition, fixedEndingPosition, whitePlayer);
            modelBoard.pendingMove(new Move(fixedStartingPosition, fixedEndingPosition));
          }
        } else {
          checkMove.validateMove(start, end, whitePlayer);
          modelBoard.pendingMove(bestMove);
        }
        this.submitMove();
        this.bestMove = findBestMoveRed.findMove();
      }
    } else {
      this.bestMove = findBestMoveWhite.findMove();
    }
  }

  /**
   * Revers the pending move on the board
   */
  public void backupMove() {
    this.modelBoard.backupMove();
  }

  /**
   * Sets a move to true/false based on the parameter
   *
   * @param moved true/false based on if a move has occurred or not
   */
  public void setMove(boolean moved) {
    modelBoard.setMove(moved);
  }
}
