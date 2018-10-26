package com.webcheckers.appl;


import com.webcheckers.model.BoardView;
import com.webcheckers.model.ModelBoard;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class BoardViewTest {

  private Player whitePlayer;
  private Player redPLayer;
  private BoardView boardView1;
  private ModelBoard modelBoard;

  @BeforeEach
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
    this.boardView1 = new BoardView(redPLayer, whitePlayer, 8, "red");
    this.modelBoard = new ModelBoard(redPLayer, whitePlayer, 8);
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
  void getRow() {
    assertNotNull(boardView1.getRow(0));
  }

  @Test
  void getIterator() {
    assertNotNull(boardView1.iterator());
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