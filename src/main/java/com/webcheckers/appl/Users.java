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
  public Users() {
    this.checkSignin = new CheckSignin();
    this.users = new ArrayList<>();
    this.usernames = new ArrayList<>();
  }

  /*
   * Class that facilitates the creation of new players
   * and ensures usernames are created properly before player creation
   * @param username the username input from the signin page
   * @return boolean true/false based on if the player was added to the system or not
   */
  public boolean addPlayer(String username) {
    if (this.checkSignin.validateUser(username, this.users)) {
      Player player = new Player(username.trim());
      this.users.add(player);
      this.usernames.add(username.trim());
      return true;
    }
    return false;
  }

  /*
   * Getter for a specified player in the list of currently signed in users
   * @param String the username of the player the system wants to obtain
   * @return Player Either null or the player requested
   */
  public Player getSpecificPlayer(String username) {
    if (usernames.contains(username)) {
      for (Player p : users) {
        if (p.getName().equals(username)) {
          return p;
        }
      }
    }
    return null;
  }

  /*
   * Getter for the list of all players currently signed into the game
   * @return List<String> the list of all usernames associated to players currently signed into the game
   */
  public List<String> getAllPlayers() {
    return this.usernames;
  }

  /*
   * Getter for the list of all usernames of players except a specified player (used for display of potential opponents)
   * @param String the username that is excluded from the list
   * @return List<String> the lsit of usernames currently signed into the game without the specified player
   */
  public List<String> getAllPlayersExceptUser(String username) {
    if (!this.usernames.contains(username)) {
      return this.usernames;
    }
    List<String> playerNames = new ArrayList<>();
    for (String u : this.usernames) {
      if (!u.equals(username)) {
        playerNames.add(u);
      }
    }
    return playerNames;
  }
}
