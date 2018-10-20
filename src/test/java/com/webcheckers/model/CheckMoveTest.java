package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class CheckMoveTest {

  private ModelBoard modelBoard;
  private CheckMove checkMove;
  private Player redPlayerMock;

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
    this.redPlayerMock = new Player("Tim");
    Player whitePlayerMock = mock(Player.class);

    modelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8);
    BoardView boardView = mock(BoardView.class);
    redPlayerMock.setColor("Red", boardView);
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
    assertTrue(checkMove.validateMove(position54, position43, redPlayerMock).containsKey(true));
  }

  @Test
  void validateMoveTest_InvalidMove() {
    // more than one space
    assertTrue(checkMove.validateMove(position54, position34, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47, redPlayerMock).containsKey(false));

    // move backward
    assertTrue(checkMove.validateMove(position54, position63, redPlayerMock).containsKey(false));

    // move to white
    assertTrue(checkMove.validateMove(position54, position44, redPlayerMock).containsKey(false));

    // move to occupied
    assertTrue(checkMove.validateMove(position34, position23, redPlayerMock).containsKey(false));

  }
}
