package com.webcheckers.model;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Application-tier")
class SpaceTest {

  private Space space1;
  private Space space2;
  private Piece pieceMock;

  @BeforeEach
  void setUp() {

    space1 = new Space(1, 2, Space.Color.BLACK);
    space2 = new Space(3, 2, Space.Color.WHITE);
    pieceMock = mock(Piece.class);
  }

  @AfterEach
  void tearUp() {
    space1 = null;
    space2 = null;
    pieceMock = null;
  }

  @Test
  void getColorTest() {

    assertEquals(space1.getColor(), Space.Color.BLACK);
    assertEquals(space2.getColor(), Space.Color.WHITE);
  }

  @Test
  void pieceColorTest() {
    Piece piece = new Piece(GameLobby.RED, space1);
    space1.occupy(piece);
    assertTrue(space1.pieceIsRed());
    assertFalse(space1.pieceIsWhite());
    assertFalse(space2.pieceIsRed());
    assertFalse(space2.pieceIsWhite());
  }

  @Test
  void occupyTest() {
    space1.occupy(pieceMock);
    assertTrue(space1.isOccupied());
    assertEquals(space1.getPiece(), pieceMock);
  }

  @Test
  void isOccupiedTest() {
    assertFalse(space1.isOccupied());
    space1.occupy(pieceMock);
    assertTrue(space1.isOccupied());

  }

  @Test
  void unoccupyTest() {
    space1.occupy(pieceMock);
    assertTrue(space1.isOccupied());
    space1.unoccupy();
    assertFalse(space1.isOccupied());
  }

  @Test
  void getCellIdxTest() {
    assertEquals(space1.getCellIdx(), 2);
  }

  @Test
  void getxCoordinate() {
    assertEquals(space1.getxCoordinate(), 1);
  }

  @Test
  void isValid() {
    assertTrue(space1.isValid());
    assertFalse(space2.isValid());

    space1.occupy(pieceMock);
    assertFalse(space1.isValid());
  }


}
