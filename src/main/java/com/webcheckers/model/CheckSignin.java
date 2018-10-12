package com.webcheckers.model;

import java.util.*;
import com.webcheckers.appl.Player;

/*
 * Class that ensures input for usernames matches requirements
 */
public class CheckSignin {

  /*
   * Main validation for user input. Ensures that the username doesn't already exist
   * and that the values entered are only alpha numeric characters or spaces
   * @param username the string input from the signin page
   * @param users the list of users already signed into the game
   * @return boolean true/false based on if the player entered information is valid or not
   */
  public boolean validateUser(String username, List<Player> users) {
    for (Player user : users) {
      if (user.getName().equals(username)) {
        return false;
      }
    }
    if (username.equals("")) {
      return false;
    } else if (username.length() >= 18) {
      return false;
    } else {
      int spaces = 0;
      for (int i = 0; i < username.length(); i++) {
        int c = username.charAt(i);
        if (!((65 <= c && c <= 90) || (97 <= c && c <= 122) || c == 32 ||
            (48 <= c && c <= 57))) {
          return false;
        }
        if (c == 32) {
          spaces++;
        }
      }
      if (spaces == username.length()) {
        return false;
      }
      return true;
    }
  }
}
