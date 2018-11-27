package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test Class for Player
 */
@Tag("Application-tier")
public class PlayerTest {

  private Player player1;
  private Player player2;
  private Player player3;
  private String usernamePlayer1;
  private String usernamePlayer2;
  private PlayerBoard playerBoardMock;
  private ModelBoard modelBoardMock;


  @BeforeEach
  void setUp() throws Exception {
    usernamePlayer1 = "player1";
    player1 = new Player(usernamePlayer1);

    usernamePlayer2 = "player2";
    player2 = new Player(usernamePlayer2);

    player3 = new Player(usernamePlayer1);

    playerBoardMock = mock(PlayerBoard.class);
    modelBoardMock = mock(ModelBoard.class);

  }

  @AfterEach
  void tearDown() throws Exception {
    player1 = null;
    player2 = null;
    usernamePlayer1 = null;
    usernamePlayer2 = null;
    playerBoardMock = null;

  }

  @Test
  void getUsernameTest() {
    assertEquals(player1.getName(), usernamePlayer1);
  }

  @Test
  void equalsTest() {
    assertEquals(player1, player3);
    assertNotEquals(player1, player2);
    assertNotEquals(player1, "a");
  }

  @Test
  void inGameTest() {
    assertFalse(player1.inGame());
    player1.setColor("White");
    assertTrue(player1.inGame());
  }

  @Test
  void setColorTest() {
    player1.setColor("White");
    assertEquals("White", player1.getColor());
  }

  @Test
  void getColorTest() {
    player1.setColor("White");
    assertEquals("White", player1.getColor());
  }

}
