package com.webcheckers.appl;

import java.util.*;
import com.webcheckers.model.CheckSignin;
import com.webcheckers.appl.Player;

public class Users {

  private CheckSignin checkSignin;
  private List<Player> users;

  public Users(){
    this.checkSignin = new CheckSignin();
    this.users = new ArrayList<>();
  }
 
  public boolean addPlayer(String username){
    if(this.checkSignin.validateUser(username, this.users)){
      Player player = new Player(username);
      this.users.add(player);
      return true;
    }
    return false;
  }

  public List<Player> getAllPlayers(){
    return this.users;
  }
}
