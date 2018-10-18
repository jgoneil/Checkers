package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("Model-tier")
class CheckMoveTest {

  private ModelBoard modelBoard;
  private CheckMove checkMove;

  private Position position54;
  private Position position43;
  private Position position34;
  private Position position47;
  private Position position63;
  private Position position23;

  // White square
  private Position position44;

  @BeforeEach
  void setUp() {
    Player redPlayerMock = mock(Player.class);
    Player whitePlayerMock = mock(Player.class);

    modelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8);
    checkMove = new CheckMove(modelBoard);

    position54 = new Position(5, 4);
    position43 = new Position(4, 3);
    position34 = new Position(3, 4);
    position47 = new Position(4, 7);
    position63 = new Position(6, 3);
    position23 = new Position(2, 3);
    position44 = new Position(4, 4);
  }

  @AfterEach
  void tearDown() {
    modelBoard = null;
    checkMove = null;
  }


  @Test
  void validateMoveTest_ValidMove() {
    assertTrue(checkMove.validateMove(position54, position43).containsKey(true));
  }

  @Test
  void validateMoveTest_InvalidMove() {
    // more than one space
    assertTrue(checkMove.validateMove(position54, position34).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47).containsKey(false));

    // move backward
    assertTrue(checkMove.validateMove(position54, position63).containsKey(false));

    // move to white
    assertTrue(checkMove.validateMove(position54, position44).containsKey(false));

    // move to occupied
    assertTrue(checkMove.validateMove(position34, position23).containsKey(false));

  }
}
