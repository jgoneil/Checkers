package com.webcheckers.appl;

import java.util.*;
import com.webcheckers.model.CheckSignin;
import com.webcheckers.appl.Player;

/*
 * Class that stores and controls all users signed into the game
 */
public class Users {

  private CheckSignin checkSignin;
  private List<Player> users;

  /*
   * Constructor for the class that establishes sign in checks 
   * and the list of currently signed in users
   */
  public Users(){
    this.checkSignin = new CheckSignin();
    this.users = new ArrayList<>();
  }

  /*
   * Class that facilitates the creation of new players
   * and ensures usernames are created properly before player creation
   * @param username the username input from the signin page
   */
  public boolean addPlayer(String username){
    if(this.checkSignin.validateUser(username, this.users)){
      Player player = new Player(username);
      this.users.add(player);
      return true;
    }
    return false;
  }

  /*
   * Getter for the list of all players currently signed into the game
   */
  public List<Player> getAllPlayers(){
    return this.users;
  }
}
