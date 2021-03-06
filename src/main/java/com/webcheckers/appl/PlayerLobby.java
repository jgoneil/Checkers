package com.webcheckers.appl;

import static java.util.stream.Collectors.toList;

import java.util.*;

import com.webcheckers.model.AbstractPlayer;
import com.webcheckers.model.AiPlayer;
import com.webcheckers.model.CheckSignin;
import com.webcheckers.model.Player;

/**
 * Class that stores and controls all users signed into the game
 */
public class PlayerLobby {

  //The function for checking if user input for a username is valid
  private final CheckSignin checkSignin;
  //The list of players currently in the game
  private List<AbstractPlayer> users;
  //The list of usernames for the players signed into the game
  private List<String> usernames;

  /**
   * Constructor for the class that establishes sign in checks and the list of currently signed in
   * users
   */
  public PlayerLobby() {
    this.checkSignin = new CheckSignin();
    this.users = new ArrayList<>();
    this.usernames = new ArrayList<>();
  }

  /**
   * Class that facilitates the creation of new players and ensures usernames are created properly
   * before player creation
   *
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

  /**
   * Class that facilitates the addition of new ai players before player creation
   *
   * @param aiPlayer the username input from the signin page
   * @return boolean true/false based on if the player was added to the system or not
   */
  public void addPlayer(AiPlayer aiPlayer) {
    this.users.add(aiPlayer);
    this.usernames.add(aiPlayer.getName());
  }

  /**
   * Getter for a specified player in the list of currently signed in users
   *
   * @param username the username of the player the system wants to obtain
   * @return Player Either null or the player requested
   */
  public AbstractPlayer getSpecificPlayer(String username) {
    if (usernames.contains(username)) {
      for (AbstractPlayer p : users) {
        if (p.getName().equals(username)) {
          return p;
        }
      }
    }
    return null;
  }

  /**
   * Getter for the list of all players currently signed into the game
   *
   * @return the list of all usernames associated to players currently signed into the game
   */
  public List<String> getAllPlayers() {
    return this.usernames.stream().filter(username -> !username.contains("F@ke")).collect(toList());
  }

  /**
   * Getter for the list of all usernames of players except a specified player (used for display of
   * potential opponents)
   *
   * Also will not display the AI player
   *
   * @param username the username that is excluded from the list
   * @return the lsit of usernames currently signed into the game without the specified player
   */
  public List<String> getAllPlayersExceptUser(String username) {
    if (!this.usernames.contains(username)) {
      return this.usernames;
    }
    List<String> playerNames = new ArrayList<>();
    for (String u : this.usernames) {
      if (!u.equals(username)) {
        if (!u.contains("F@ke")) {
          playerNames.add(u);
        }
      }
    }
    return playerNames;
  }

  /**
   * Removes a user from the list of users connected to the system
   *
   * @param username the username of the player being removed
   */
  public void removeUser(String username) {
    users.remove(getSpecificPlayer(username));
    usernames.remove(username);
  }

  /**
   * Gets the number of users currently logged into the system
   *
   * @return the number of players connected to the system
   */
  public int getNumberOfPlayers() {
    return this.users.stream().filter(user -> !(user instanceof AiPlayer)).collect(toList()).size();
  }
}
