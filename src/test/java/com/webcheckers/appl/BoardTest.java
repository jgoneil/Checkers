package com.webcheckers.appl;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BoardTest {

  Player whitePlayer;
  Player redPLayer;
  Board board1;

  @Before
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
    this.board1 = new Board(redPLayer, whitePlayer, 7);
  }

  @Test
  public void getWhitePlayer() {
    assert (board1.getWhitePlayer() == whitePlayer);
  }

  @Test
  public void getRedPlayer() {
    assert (board1.getRedPlayer() == redPLayer);
  }

  @Test
  public void getBoard() {
    board1.getBoard();
  }

  @Test
  public void redTurn() {
    assert (board1.redTurn());
  }

  @Test
  public void turnEnded() {
    board1.turnEnded();
    assert (!board1.redTurn());
  }
}