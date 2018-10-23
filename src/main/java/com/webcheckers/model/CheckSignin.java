package com.webcheckers.model;

import com.webcheckers.appl.Player;
import java.util.List;

/**
 * Class that ensures input for usernames matches requirements
 */
public class CheckSignin {

  /**
   * Main validation for user input. Ensures that the username doesn't already exist and that the
   * values entered are only alpha numeric characters or spaces
   *
   * @param input the string input from the signin page
   * @param users the list of users already signed into the game
   * @return boolean true/false based on if the player entered information is valid or not
   */
  public boolean validateUser(String input, List<Player> users) {
    String username = input.toLowerCase().trim();
    //Checking to make sure the username is not already in the system
    for (Player user : users) {
      if (username.equals(user.getName().toLowerCase())) {
        return false;
      }
    }
    //Checking to make sure the username is not empty
    if (username.equals("")) {
      return false;
    } else {
      int spaces = 0;
      for (int i = 0; i < username.length(); i++) {
        int c = username.charAt(i);
        //Checking to make sure each entered letter is a character, number, or space
        if (!((65 <= c && c <= 90) || (97 <= c && c <= 122) || c == 32 ||
            (48 <= c && c <= 57))) {
          return false;
        }
        if (c == 32) {
          spaces++;
        }
      }

      return true;
    }
  }
}
