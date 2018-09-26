package com.webcheckers.model;

import java.util.*;
import com.webcheckers.appl.Player;

public class CheckSignin{

  public boolean validateUser(String username, List<Player> users){
    for(Player user: users){
      if(user.getUsername().equals(username)) {
        return false;
      }
    }
    if(username.equals("")) {
      return false;
    } 
    else {
      for(int i = 0; i < username.length(); i++) {
        int c = Character.getNumericValue(username.charAt(i));
        if (!((65 <= c && c <= 90) || (97 <= c && c <= 122) || c == 32)) {
          return false;
        }
      }
      return true;
    }
  }
}
