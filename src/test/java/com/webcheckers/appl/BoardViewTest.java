package com.webcheckers.appl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardViewTest {

  private Player whitePlayer;
  private Player redPLayer;
  private BoardView boardView1;

  @Before
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
    this.boardView1 = new BoardView(redPLayer, whitePlayer, 8, "red");
  }

  @Test
  void getWhitePlayer() {
    assertEquals(whitePlayer, boardView1.getWhitePlayer());
  }

  @Test
  void getRedPlayer() {
    assertEquals(redPLayer, boardView1.getRedPlayer());
  }

  @Test
  void getBoard() {
    assertNotNull(boardView1.getBoard());
  }

  @Test
  void redTurn() {
    assertTrue(boardView1.redTurn());
    boardView1.turnEnded();
    assertFalse(boardView1.redTurn());
  }

  @Test
  void turnEnded() {
    assertTrue(boardView1.redTurn());
    boardView1.turnEnded();
    assertFalse(boardView1.redTurn());
  }
}