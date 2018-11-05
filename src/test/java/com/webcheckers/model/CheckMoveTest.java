package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.GameLobby;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class CheckMoveTest {

  private ModelBoard modelBoard;
  private CheckMove checkMove;
  private Player redPlayerMock;
  private Player whitePlayerMock;

  private Position position54;
  private Position position43;
  private Position position34;
  private Position position47;
  private Position position63;
  private Position position23;
  private Position position50;
  private Position position32;

  // White square
  private Position position44;

  private Move pendingMove1;

  @BeforeEach
  void setUp() {
    this.redPlayerMock = new Player("Tim");
    this.whitePlayerMock = new Player("Joe");
    this.redPlayerMock.setColor(GameLobby.RED);
    this.whitePlayerMock.setColor(GameLobby.WHITE);

    modelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8);
    checkMove = new CheckMove(modelBoard);

    position54 = new Position(5, 4);
    position43 = new Position(4, 3);
    position34 = new Position(3, 4);
    position47 = new Position(4, 7);
    position63 = new Position(6, 3);
    position23 = new Position(2, 3);
    position44 = new Position(4, 4);
    position50 = new Position(5, 0);
    position32 = new Position(3, 2);

    pendingMove1 = null;
  }

  @AfterEach
  void tearDown() {
    modelBoard = null;
    checkMove = null;
  }


  @Test
  void validateMoveTest_ValidMove() {
    assertTrue(checkMove.validateMove(position54, position43, redPlayerMock, pendingMove1).containsKey(true));
    assertTrue(checkMove.validateMove(position54, position43, whitePlayerMock, pendingMove1).containsKey(true));
  }

  @Test
  void validateMoveTest_InvalidMove() {
    // more than one space
    assertTrue(checkMove.validateMove(position54, position34, redPlayerMock, pendingMove1).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position34, whitePlayerMock, pendingMove1).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47, redPlayerMock, pendingMove1).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47, whitePlayerMock, pendingMove1).containsKey(false));

    // move backward
    assertTrue(checkMove.validateMove(position34, position43, redPlayerMock, pendingMove1).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position63, whitePlayerMock, pendingMove1).containsKey(false));

    // move to white
    assertTrue(checkMove.validateMove(position54, position44, redPlayerMock, pendingMove1).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position44, whitePlayerMock, pendingMove1).containsKey(false));

    // move to occupied
    assertTrue(checkMove.validateMove(position34, position23, redPlayerMock, pendingMove1).containsKey(false));
    assertTrue(checkMove.validateMove(position34, position23, whitePlayerMock, pendingMove1).containsKey(false));

    // move forward not diagonally
    assertTrue(checkMove.validateMove(position50, position54, redPlayerMock, pendingMove1).containsKey(false));
  }

  @Test
  void validateJump() {
    modelBoard.addPieceToSpace(new Piece("white", new Space(4, 1, Space.Color.BLACK)),
            new Space(4, 1, Space.Color.BLACK));
    assertTrue(checkMove.validateMove(position50, position32, redPlayerMock, pendingMove1).containsKey(true));
  }

  @Test
  void CantJump() {
    modelBoard.addPieceToSpace(new Piece("red", new Space(5, 0, Space.Color.BLACK)),
            new Space(3, 2, Space.Color.BLACK));
    modelBoard.addPieceToSpace(new Piece("white", new Space(4, 1, Space.Color.BLACK)),
            new Space(4, 1, Space.Color.BLACK));
    assertTrue(checkMove.validateMove(position54, position43, redPlayerMock, pendingMove1).containsKey(false));
  }
}
