package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test Class for Player
 */
public class PlayerTest {

  private Player player1;
  private String usernamePlayer1;


  @BeforeEach
  public void setUp() throws Exception {
    usernamePlayer1 = "player1";
    player1 = new Player(usernamePlayer1);

  }

  @AfterEach
  public void tearDown() throws Exception {
    player1 = null;
    usernamePlayer1 = null;

  }

  @Test
  public void getUsernameTest() {
    assertEquals(player1.getUsername(), usernamePlayer1);
  }
}
