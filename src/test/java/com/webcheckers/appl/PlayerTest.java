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
  private Player player2;
  private Player player3;
  private String usernamePlayer1;
  private String usernamePlayer2;


  @BeforeEach
  void setUp() throws Exception {
    usernamePlayer1 = "player1";
    player1 = new Player(usernamePlayer1);

    usernamePlayer2 = "player2";
    player2 = new Player(usernamePlayer2);

    player3 = new Player(usernamePlayer1);

  }

  @AfterEach
  void tearDown() throws Exception {
    player1 = null;
    player2 = null;
    usernamePlayer1 = null;
    usernamePlayer2 = null;

  }

  @Test
  void getUsernameTest() {
    assertEquals(player1.getUsername(), usernamePlayer1);
  }

  @Test
  void equalsTest(){
    assertEquals(player1, player3);
    assertNotEquals(player1, player2);
  }

  @Test
  void inGameTest() {
  }

  @Test
  void setColorTest() {
  }

  @Test
  void getColorTest() {
  }

  @Test
  void getBoardTest() {
  }
}
