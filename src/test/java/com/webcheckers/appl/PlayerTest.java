package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


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
  private Board boardMock;


  @BeforeEach
  void setUp() throws Exception {
    usernamePlayer1 = "player1";
    player1 = new Player(usernamePlayer1);

    usernamePlayer2 = "player2";
    player2 = new Player(usernamePlayer2);

    player3 = new Player(usernamePlayer1);

    boardMock = mock(Board.class);

  }

  @AfterEach
  void tearDown() throws Exception {
    player1 = null;
    player2 = null;
    usernamePlayer1 = null;
    usernamePlayer2 = null;
    boardMock = null;

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
    assertFalse(player1.inGame());
    player1.setColor("White", boardMock);
    assertTrue(player1.inGame());
  }

  @Test
  void setColorTest() {
    player1.setColor("White", boardMock);
    assertEquals("White", player1.getColor());
    assertEquals(boardMock, player1.getBoard());
  }

  @Test
  void getColorTest() {
    player1.setColor("White", boardMock);
    assertEquals("White", player1.getColor());
  }

  @Test
  void getBoardTest() {
    player1.setColor("White", boardMock);
    assertEquals(boardMock, player1.getBoard());
  }
}
