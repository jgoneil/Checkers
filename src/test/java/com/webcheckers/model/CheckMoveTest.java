package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckMoveTest {

  private ModelBoard modelBoard;
  private CheckMove checkMove;
  private Player redPlayerMock;
  private Player whitePlayerMock;
  private Position whiteGoal;
  @BeforeEach
  void setUp() {
    redPlayerMock = mock(Player.class);
    whitePlayerMock = mock(Player.class);

    modelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8);
    checkMove = new CheckMove(modelBoard);

    whiteGoal = new Position(0,0);
  }

  @AfterEach
  void tearDown() {
    modelBoard = null;
    checkMove = null;
  }

  @Test
  void validateMove() {
    assertTrue(checkMove.validateMove(new Position(1,0), whiteGoal).containsKey(false));
    assertTrue(checkMove.validateMove(new Position(0,0), new Position(0,1)).containsKey(false));
  }
}