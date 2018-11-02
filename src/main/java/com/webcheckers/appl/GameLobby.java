package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.Map;
import java.util.List;

/**
 * Class to construct a new game lobby
 */
public class GameLobby {

  //The red player connected to the game
  private final Player redPlayer;
  //The white player connected to the game
  private final Player whitePlayer;
  //The model board being created for the game logic
  private ModelBoard modelBoard;
  //The check Move class to check if a move is valid or not
  private CheckMove checkMove;

  //Static final (constant) variables
  static final int BOARD_SIZE = 8;
  public static final String RED = "RED";
  public static final String WHITE = "WHITE";

  /**
   * Constructor for the Game Lobby. Establishes all of the boards for the game
   *
   * @param redPlayer the red player connected to the game lobby
   * @param whitePlayer the white player connected to the game lobby
   */
  public GameLobby(Player redPlayer, Player whitePlayer) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    redPlayer.setColor(RED);
    whitePlayer.setColor(WHITE);
    this.modelBoard = new ModelBoard(redPlayer, whitePlayer, BOARD_SIZE);
    this.checkMove = new CheckMove(this.modelBoard);
  }

  public GameLobby(Player redPlayer, Player whitePlayer, List<Piece> pieceList) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    redPlayer.setColor(RED);
    whitePlayer.setColor(WHITE);
    this.modelBoard = new ModelBoard(redPlayer, whitePlayer, BOARD_SIZE, pieceList);
    this.checkMove = new CheckMove(this.modelBoard);
  }

  /**
   * Getter for the PlayerBoardView for the red player (board shown in the rendered HTML page)
   *
   * @return the PlayerBoardView associated to the red player for the active game session
   */
  public PlayerBoardView getRedPlayerBoardView() {
    return modelBoard.getRedPlayerBoardView();
  }

  /**
   * Getter for the PlayerBoardView for the white player (board shown in the rendered HTML page)
   *
   * @return the PlayerBoardView associated to the white player for the active game session
   */
  public PlayerBoardView getWhiteBoadView() {
    return modelBoard.getWhitePlayerBoardView();
  }

  /**
   * Getter for the PlayerBoardView for the specified player (board shown in the rendered HTML page)
   *
   * @param player the player attempting to retrieve their PlayerBoardView
   * @return the PlayerBoardView associated to the specified player for the active game session
   */
  public PlayerBoardView getBoardViewForPlayer(Player player) {
    if (this.redPlayer.equals(player)) {
      return modelBoard.getRedPlayerBoardView();
    } else if (this.whitePlayer.equals(player)){
      return modelBoard.getWhitePlayerBoardView();
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
  public Player getRedPlayer() {
    return this.redPlayer;
  }

  /**
   * Checks to see if a red player can keep playing the game
   *
   * @return true/false if the red player can continue to play
   */
  public boolean redCanPlay() {
    return this.modelBoard.redCanPlay();
  }

  /**
   * Checks to see if a white player can keep playing the game
   *
   * @return true/false if the white player can continue to play
   */
  public boolean whiteCanPlay() {
    return this.modelBoard.whiteCanPlay();
  }

  /**
   * Getter for the Player associated to the white side of the board
   *
   * @return the player associated to the white side of the board
   */
  public Player getWhitePlayer() {
    return this.whitePlayer;
  }

  /**
   * Gets the opponent of a specified player for the game session
   *
   * @param player the player who is trying to find the opponent
   * @return the player that is the specified player's opponent
   */
  public Player getOpponent(Player player) {
    if(redPlayer.equals(player)) {
      return whitePlayer;
    }
    return redPlayer;
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
   * Sets the game to have completed a move
   *
   * @param move the move completed
   */
  public void madeMove(Move move) {
    this.modelBoard.madeMove(move);
  }

  /**
   * Validates the move a player is attempting to make
   *
   * @param start the starting position for the move
   * @param target the ending position for the move
   * @param player the player making the move
   * @return true/false based on if the player can make the move or not
   */
  public Map<Boolean, String> validateMove(Position start, Position target, Player player) {
    return this.checkMove.validateMove(start, target, player);
  }

  /**
   * Submits the pending move for the player and ends the player's turn
   */
  public void submitMove() {
    this.modelBoard.submitMove();
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