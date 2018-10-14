package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckMoveTest {

  private ModelBoard mockModelBoard;
  private CheckMove checkMove;

  @BeforeEach
  void setUp() {
    mockModelBoard = mock(ModelBoard.class);
    checkMove = new CheckMove(mockModelBoard);
  }

  @AfterEach
  void tearDown() {
    mockModelBoard = null;
  }

  @Test
  void validateMove() {
//    assertEquals(checkMove.validateMove(new Position(), new Position()));
  }
}