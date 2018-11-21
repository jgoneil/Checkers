package com.webcheckers.model;

import com.webcheckers.appl.GameLobby;

/**
 * Class that controls specific information connected to each player logged into the game.
 */
public class Player {

  //The username that has been validated by the system after being inputted by the user
  private String username;
  //The color on the game board that the player is
  private String color;
  //Whether the player is an AI or a USER
  private Type type;

  private enum Type{
    AI, USER
  }

  /**
   * Constructor for player class
   *
   * @param username the String value entered by the user upon signin for recognition in the game
   */
  public Player(String username) {
    this.username = username;
    this.type = Type.USER;
  }

  /**
   * Alternate constructor for player in case of AI player
   */
  public Player(){
    this.username = "F@ke";
    this.type = Type.AI;
  }
  /**
   * Getter for the player's username
   *
   * @return String the username of the player
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
    return color != null;
  }

  /**
   * Sets the color for the player for the game they are in
   *
   * @param color the color the player is associated to in the game
   */
  public void setColor(String color) {
    this.color = color;
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
   * Removes all game elements upon a player win/loss or player forfeit
   */
  public void gameEnd() {
    this.color = null;
  }

  /**
   * Checks to see if a player's color is red
   *
   * @return true/false if a player is the red player or not
   */
  public boolean isRed() {
    return this.color.equals(GameLobby.RED);
  }

  /**
   * Checks to see if a player's color is white
   *
   * @return true/false if a player is the white player or not
   */
  public boolean isWhite() {
    return this.color.equals(GameLobby.WHITE);
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
