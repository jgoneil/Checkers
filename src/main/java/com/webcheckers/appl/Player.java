package com.webcheckers.appl;

import com.webcheckers.model.ModelBoard;

/**
 * Class that controls specific information connected to each player logged into the game.
 */
public class Player {

  private String username;
  private String color;
  private Board board;
  private ModelBoard modelBoard;

  /**
   * Constructor for player class
   * @param username the String value entered by the user upon signin for recognition in the game
   */
  public Player(String username) {
    this.username = username;
  }

  /**
   * Getter for the player's username
   * @Return String the username of the player
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Boolean condition based on if the player is in a game or not
   * @return True/False based on if the player is in a game or not
   */
  public boolean inGame() {
    if(color == null) {
      return false;
    }
    return true;
  }

  /**
   * Sets the color for the player for the game they are in
   * @param color the color the player is associated to in the game
   * @param board the board the player is playing on
   */
  public void setColor(String color, Board board) {
    this.color = color;
    this.board = board;
  }

  /**
   * The getter for the color the player is associated to
   * @return the color the player is associated to or null if the player is not in the game
   */
  public String getColor() {
    return this.color;
  }

  /**
   * The getter for the board the player is using for the game they are in
   * @return the board the player is using for the game
   */
  public Board getBoard() {
    return this.board;
  }

  /**
   * Setter for the model board for the player
   * @param modelBoard the model board for the game
   */
  public void addModelBoard(ModelBoard modelBoard) {
    this.modelBoard = modelBoard;
  }

  /**
   * Getter for the model board
   * @return the board for the model class 
   */
  public ModelBoard getModelBoard() {
    return this.modelBoard;
  }

  /**
   * Equals method for Player
   * @param obj Player to compare to
   * @return Boolean corresponding to equality
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player){
      Player p = (Player) obj;
      return this.username.equals(p.username);
    }
    return false;
  }
}
