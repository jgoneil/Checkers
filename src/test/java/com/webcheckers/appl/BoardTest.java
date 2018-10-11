package com.webcheckers.appl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class BoardTest {

  private Player whitePlayer;
  private Player redPLayer;
  private Board board1;

  @BeforeEach
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
    this.board1 = new Board(redPLayer, whitePlayer, 8, "red");
  }

  @AfterEach
  void tearDown() throws Exception {
    whitePlayer = null;
    redPLayer = null;
    board1 = null;
  }

  @Test
  public void getWhitePlayer() {
    assertEquals(whitePlayer, board1.getWhitePlayer());
  }

  @Test
  public void getRedPlayer() {
    assertEquals(redPLayer, board1.getRedPlayer());
  }

  @Test
  public void getBoard() {
    assertNotNull(board1.getBoard());
  }

  @Test
  public void redTurn() {
    assertTrue(board1.redTurn());
    board1.turnEnded();
    assertFalse(board1.redTurn());
  }

  @Test
  public void turnEnded() {
    assertTrue(board1.redTurn());
    board1.turnEnded();
    assertFalse(board1.redTurn());
  }
}