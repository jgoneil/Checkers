package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoveTest {

  private Move move1;
  private Position positionMock1;
  private Position positionMock2;

  @BeforeEach
  void setUp() {
    positionMock1 = mock(Position.class);
    positionMock2 = mock(Position.class);
    move1 = new Move(positionMock1, positionMock2);
  }

  @AfterEach
  void tearDown() {
    positionMock1 = null;
    positionMock2 = null;
    move1 = null;
  }

  @Test
  void getStartTest() {
    assertEquals(move1.getStart(), positionMock1);
  }

  @Test
  void getEndTest() {
    assertEquals(move1.getEnd(), positionMock2);
  }
}