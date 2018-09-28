package com.webcheckers.model;

import java.util.*;
import com.webcheckers.appl.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCheckSignin {

  List<Player> users = new ArrayList<>();
  
  private String validUser1 = "ABCD";
  private String validUser2 = "HI TEST";
  private String validUser3 = "     A     ";
  private String validUser4 = "A";

  private String invalidUser1 = "";
  private String invalidUser2 = "$$";
  private String invalidUser3 = "         ";
  private String invalidUser4 = "ABCD";
  private String invalidUser5 = "ADNO$$IWT";

  private CheckSignin checkSignin = new CheckSignin();
  
  public void assertValidUser(){
    assertTrue(checkSignin.validateUser(validUser1, users));
    assertTrue(checkSignin.validateUser(validUser2, users));
    assertTrue(checkSignin.validateUser(validUser3, users));
    assertTrue(checkSignin.validateUser(validUser4, users));
  }

  public void assertInValidUser(){
    users.add(new Player(validUser1));
    assertFalse(checkSignin.validateUser(invalidUser1, users));
    assertFalse(checkSignin.validateUser(invalidUser2, users));
    assertFalse(checkSignin.validateUser(invalidUser3, users));
    assertFalse(checkSignin.validateUser(invalidUser4, users));
    assertFalse(checkSignin.validateUser(invalidUser5, users));
  }
}
