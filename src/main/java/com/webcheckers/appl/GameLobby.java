package com.webcheckers.appl;

import com.webcheckers.model.*;

import java.util.Map;

/**
 * Class to construct a new game lobby
 */
public class GameLobby {

  private Player redPlayer;
  private Player whitePlayer;
  private ModelBoard modelBoard;
  private PlayerBoardView redPlayerBoardView;
  private PlayerBoardView whitePlayerBoadView;
  private CheckMove checkMove;

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
    this.redPlayerBoardView = new PlayerBoardView(redPlayer, whitePlayer, BOARD_SIZE, RED);
    this.whitePlayerBoadView = new PlayerBoardView(redPlayer, whitePlayer, BOARD_SIZE, WHITE);
    this.modelBoard = new ModelBoard(redPlayer, whitePlayer, BOARD_SIZE, redPlayerBoardView, whitePlayerBoadView);
    this.checkMove = new CheckMove(this.modelBoard);
  }

  /**
   * Getter for the PlayerBoardView for the red player (board shown in the rendered HTML page)
   *
   * @return the PlayerBoardView associated to the red player for the active game session
   */
  public PlayerBoardView getRedPlayerBoardView() {
    return this.redPlayerBoardView;
  }

  /**
   * Getter for the PlayerBoardView for the white player (board shown in the rendered HTML page)
   *
   * @return the PlayerBoardView associated to the white player for the active game session
   */
  public PlayerBoardView getWhiteBoadView() {
    return this.whitePlayerBoadView;
  }

  /**
   * Getter for the PlayerBoardView for the specified player (board shown in the rendered HTML page)
   *
   * @param player the player attempting to retrieve their PlayerBoardView
   * @return the PlayerBoardView associated to the specified player for the active game session
   */
  public PlayerBoardView getBoardViewForPlayer(Player player) {
    if (this.redPlayer.equals(player)) {
      return redPlayerBoardView;
    } else if (this.whitePlayer.equals(player)){
      return whitePlayerBoadView;
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

  public boolean checkRedTurn() {
    return modelBoard.checkRedTurn();
  }

  public boolean checkMadeMove() {
    return this.modelBoard.checkMadeMove();
  }

  public void madeMove(Move move) {
    this.modelBoard.madeMove(move);
  }

  public Map<Boolean, String> validateMove(Position start, Position target, Player player) {
    return this.checkMove.validateMove(start, target, player);
  }

  public void submitMove() {
    this.modelBoard.submitMove();
  }

  public void backupMove() {
    this.modelBoard.backupMove();
  }

  public void setMove(boolean moved) {
    modelBoard.setMove(moved);
  }
}
