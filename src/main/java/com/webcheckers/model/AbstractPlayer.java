package com.webcheckers.model;

import com.webcheckers.appl.GameLobby;

public abstract class AbstractPlayer {

  //The username that has been validated by the system after being inputted by the user
  private String username;
  //The color on the game board that the player is
  private String color;


  /**
   * Constructor for AbstractPlayer class
   *
   * @param username the String value entered by the user upon signin for recognition in the game
   */
  AbstractPlayer(String username){
    this.username = username;
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
    if (obj instanceof AbstractPlayer) {
      AbstractPlayer p = (AbstractPlayer) obj;
      return this.username.equals(p.username);
    }
    return false;
  }
}

