package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class PlayerBoardTest {

  private Player whitePlayer;
  private Player redPLayer;
  private PlayerBoard playerBoard1;
  private ModelBoard modelBoard;
  private Piece piece;
  private PlayerBoard boardViewTesting;

  @BeforeEach
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
    this.piece = new Piece("red", new Space(4, 1, Space.Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(piece);
    this.boardViewTesting = new PlayerBoard(redPLayer, whitePlayer, 8, "red", pieces);
    this.playerBoard1 = new PlayerBoard(redPLayer, whitePlayer, 8, "red");
    this.modelBoard = new ModelBoard(redPLayer, whitePlayer, 8);
  }

  @Test
  void getWhitePlayer() {
    assertEquals(whitePlayer, playerBoard1.getWhitePlayer());
  }

  @Test
  void getRedPlayer() {
    assertEquals(redPLayer, playerBoard1.getRedPlayer());
  }

  @Test
  void getBoard() {
    assertNotNull(playerBoard1.getBoard());
  }

  @Test
  void getRow() {
    assertNotNull(playerBoard1.getRow(0));
  }

  @Test
  void checkTestBoardCreation() {
    assertEquals(boardViewTesting.getBoard().get(4).getSpace(1).getPiece(), piece);
    assertNull(boardViewTesting.getBoard().get(0).getSpace(0).getPiece());
  }

  @Test
  void getIterator() {
    assertNotNull(playerBoard1.iterator());
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