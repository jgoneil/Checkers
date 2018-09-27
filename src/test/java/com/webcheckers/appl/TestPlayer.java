package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayer {

  public void assertMatchingString(){
    String username = "ABCD";
    Player player = new Player(username);
    assertEquals(player.getUsername(), username); 
  }
}
