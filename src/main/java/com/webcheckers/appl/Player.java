package com.webcheckers.appl;

/*
 * Class that controls specific information connected to each player logged into the game.
 */
public class Player{

  //Username for each user
  private String username;

  /*
   * Constructor for player class
   * @param username the String value entered by the user upon signin for recognition in the game
   */
  public Player(String username){
    this.username = username;
  }

  /*
   * Getter for the player's username
   * @Return String the username of the player
   */
  public String getUsername(){
    return this.username;
  }
}
