package com.webcheckers.appl;

import com.webcheckers.model.ModelBoard;

/**
 * Class that controls specific information connected to each player logged into the game.
 */
public class Player {

  //The username that has been validated by the system after being inputted by the user
  private String username;
  //The color on the game board that the player is
  private String color;
  //The Application board that the player sees (oriented towards them)
  private BoardView boardView;
  //The Model board that holds the logic for the game (oriented towards the red player)
  private ModelBoard modelBoard;
  //Keeps track if a player made a move on their turn or not
  private boolean hasMoved;

  /**
   * Constructor for player class
   *
   * @param username the String value entered by the user upon signin for recognition in the game
   */
  public Player(String username) {
    this.username = username;
    this.hasMoved = false;
  }

  /**
   * Getter for the player's username
   *
   * @Return String the username of the player
   */
  public String getName() {
    return this.username;
  }

  /**
   * Boolean condition based on if the player is in a game or not
   *
   * @return True/False based on if the player is in a game or not
   */
  public boolean inGame() {
    if (color == null) {
      return false;
    }
    return true;
  }

  /**
   * Sets the color for the player for the game they are in
   *
   * @param color the color the player is associated to in the game
   * @param boardView the boardView the player is playing on
   */
  public void setColor(String color, BoardView boardView) {
    this.color = color;
    this.boardView = boardView;
  }

  /**
   * The getter for the color the player is associated to
   *
   * @return the color the player is associated to or null if the player is not in the game
   */
  public String getColor() {
    return this.color;
  }

  /**
   * The getter for the boardView the player is using for the game they are in
   *
   * @return the boardView the player is using for the game
   */
  public BoardView getBoardView() {
    return this.boardView;
  }

  /**
   * Setter for the model boardView for the player
   *
   * @param modelBoard the model boardView for the game
   */
  public void addModelBoard(ModelBoard modelBoard) {
    this.modelBoard = modelBoard;
  }

  /**
   * Getter for the model boardView
   *
   * @return the boardView for the model class
   */
  public ModelBoard getModelBoard() {
    return this.modelBoard;
  }


  /**
   * Setter for when a player has or hasn't made a move on the turn
   * @param hasMoved Boolean for new move status
   */
   */
  public void setHasMoved(boolean moveStatus) {
    this.hasMoved = moveStatus;
  }

  /**
   * Getter for if a player has made a move on their current turn or not
   *
   * @return a boolean condition based on if the player has made a move or not
   */
  public boolean getHasMoved() {
    return this.hasMoved;
  }

  /**
   * Removes all game elements upon a player win/loss or player forfeit
   */
  public void gameEnd() {
    this.boardView = null;
    this.modelBoard = null;
    this.color = null;
    this.hasMoved = false;
  }

  /**
   * Equals method for Player
   *
   * @param obj Player to compare to
   * @return Boolean corresponding to equality
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player) {
      Player p = (Player) obj;
      return this.username.equals(p.username);
    }
    return false;
  }
}
