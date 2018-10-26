package com.webcheckers.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class PlayerBoardViewTest {

  private Player whitePlayer;
  private Player redPLayer;
  private PlayerBoardView playerBoardView1;
  private ModelBoard modelBoard;

  @BeforeEach
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
    this.playerBoardView1 = new PlayerBoardView(redPLayer, whitePlayer, 8, "red");
    this.modelBoard = new ModelBoard(redPLayer, whitePlayer, 8);
  }

  @Test
  void getWhitePlayer() {
    assertEquals(whitePlayer, playerBoardView1.getWhitePlayer());
  }

  @Test
  void getRedPlayer() {
    assertEquals(redPLayer, playerBoardView1.getRedPlayer());
  }

  @Test
  void getBoard() {
    assertNotNull(playerBoardView1.getBoard());
  }

  @Test
  void getRow() {
    assertNotNull(playerBoardView1.getRow(0));
  }

  @Test
  void getIterator() {
    assertNotNull(playerBoardView1.iterator());
  }

  @Test
  void redTurn() {
    assertTrue(modelBoard.checkRedTurn());
    modelBoard.setMove(true);
    assertFalse(modelBoard.checkRedTurn());
  }

  @Test
  void turnEnded() {
    assertTrue(modelBoard.checkRedTurn());
    modelBoard.setMove(true);
    assertFalse(modelBoard.checkRedTurn());
  }
}