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
  private List<String> usernames;

  /*
   * Constructor for the class that establishes sign in checks 
   * and the list of currently signed in users
   */
  public Users(){
    this.checkSignin = new CheckSignin();
    this.users = new ArrayList<>();
    this.usernames = new ArrayList<String>();
  }

  /*
   * Class that facilitates the creation of new players
   * and ensures usernames are created properly before player creation
   * @param username the username input from the signin page
   * @return boolean true/false based on if the player was added to the system or not
   */
  public boolean addPlayer(String username){
    if(this.checkSignin.validateUser(username, this.users)){
      Player player = new Player(username);
      this.users.add(player);
      this.usernames.add(username);
      return true;
    }
    return false;
  }
  
  /*
   * Getter for a specified player in the list of currently signed in users
   * @param String the username of the player the system wants to obtain
   * @return Player Either null or the player requested 
   */
  public Player getSpecificPlayer(String username){
    if(usernames.contains(username)){
      for(Player p: users){
        if(p.getUsername().equals(username)){
          return p;
        }
      }
    } 
    return null;
  }

  /*
   * Getter for the list of all players currently signed into the game
   * @return String[] the array of all usernames associated to players currently signed into the game
   */
  public String[] getAllPlayers(){
    String[] tempArray = new String[usernames.size()];
    tempArray = usernames.toArray(tempArray);
    return tempArray;
  }

  /*
   * Getter for the list of all usernames of players except a specified player (used for display of potential opponents)
   * @param String the username that is excluded from the list
   * @return String[] the array of usernames currently signed into the game without the specified player
   */ 
  public String[] getAllPlayersExceptUser(String username){
    String[] playerNames = new String[usernames.size() - 1];
    int counter = 0;
    for(String u: this.usernames){
      if(u.equals(username)){
        continue;
      }
      else{
        playerNames[counter] = u;
      }
    }
    return playerNames;
  }
}
