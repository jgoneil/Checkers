package com.webcheckers.appl;

import com.webcheckers.model.ModelBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class BoardViewTest {

  private Player whitePlayer;
  private Player redPLayer;
  private BoardView boardView1;
  private BoardView boardViewTesting;
  private Piece piece;
  private ModelBoard modelBoard;

  @BeforeEach
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
    this.boardView1 = new BoardView(redPLayer, whitePlayer, 8, "red");
    this.piece = new Piece("red", new Space(4, 1, Space.Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(piece);
    this.boardViewTesting = new BoardView(redPLayer, whitePlayer, 8, "red", pieces);
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
  void checkTestBoardCreation() {
    assertEquals(boardViewTesting.getBoard().get(4).getSpace(1).getPiece(), piece);
    assertNull(boardViewTesting.getBoard().get(0).getSpace(0).getPiece());
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