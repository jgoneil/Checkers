package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionTest {

  private Position position1;

  @BeforeEach
  void setUp() {
    position1 = new Position(0, 1);
  }

  @AfterEach
  void tearDown() {
    position1 = null;
  }

  @Test
  void getRow() {
    assertEquals(position1.getRow(), 0);
  }

  @Test
  void getCell() {
    assertEquals(position1.getCell(), 1);
  }
}