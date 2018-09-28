package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestPlayer {

  @Rule
  public ExpectedException exceptions = ExpectedException.none();

  private Player player1;
  private String usernamePlayer1;


  @Before
  public void setUp() throws Exception {
    usernamePlayer1 = "player1";
    player1 = new Player(usernamePlayer1);

  }

  @After
  public void tearDown() throws Exception {
    player1 = null;
    usernamePlayer1 = null;

  }

  @Test
  public void assertMatchingString(){
    assertEquals(player1.getUsername(), usernamePlayer1);
  }
}
