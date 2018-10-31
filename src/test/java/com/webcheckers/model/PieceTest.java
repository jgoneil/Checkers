package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Piece.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Application-tier")
class PieceTest {

  Piece piece1;
  Space originalSpace;
  Space finalSapce;

  @BeforeEach
  void setUp() {
    originalSpace = new Space(0, 0, Space.Color.WHITE);
    finalSapce = new Space(1, 1, Space.Color.WHITE);
    piece1 = new Piece(GameLobby.RED, originalSpace);
  }

  @AfterEach
  void tearDown() {
    originalSpace = null;
    piece1 = null;
  }

  @Test
  void moveTest() {
    piece1.move(finalSapce);
    assertEquals(piece1.getSpace(), finalSapce);
  }

  @Test
  void getSpaceTest() {
    assertEquals(piece1.getSpace(), originalSpace);
  }

  @Test
  void geTypeTest() {
    assertEquals(piece1.getType(), Type.SINGLE);
  }

  @Test
  void getColorTest() {
    assertEquals(piece1.getColor(), Piece.Color.RED);
  }

  @Test 
  void checkColor() {
    assertTrue(piece1.isRed());
    assertFalse(piece1.isWhite());
  }
}
