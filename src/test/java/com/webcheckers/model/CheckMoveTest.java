package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Space;
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

  // White square
  private Position position44;

  @BeforeEach
  void setUp() {
    this.redPlayerMock = new Player("Tim");
    this.whitePlayerMock = new Player("Joe");

    modelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8);
    BoardView boardView = mock(BoardView.class);
    redPlayerMock.setColor("Red", boardView);
    whitePlayerMock.setColor("White", boardView);
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
    //Single Valid Move
    assertTrue(checkMove.validateMove(position54, position43, redPlayerMock).containsKey(true));
    assertTrue(checkMove.validateMove(position54, position43, whitePlayerMock).containsKey(true));
  }

  @Test
  void validateKingMoveTest_Valid(){
    //King pieces for move
    Space kingSpace1 = modelBoard.getSpace(position34.getRow(), position34.getCell());
    Space kingSpace2 = modelBoard.getSpace(position43.getRow(), position43.getCell());
    Space kingSpace3 = modelBoard.getSpace(position54.getRow(), position54.getCell());
    Space kingSpace4 = modelBoard.getSpace(position63.getRow(), position63.getCell());
    kingSpace1.getPiece().King();
    kingSpace2.getPiece().King();
    kingSpace3.getPiece().King();
    kingSpace4.getPiece().King();

    //King Valid Backward Move
    assertTrue(checkMove.validateMove(position34, position43, redPlayerMock).containsKey(true));
    assertTrue(checkMove.validateMove(position54, position63, whitePlayerMock).containsKey(true));
  }

  @Test
  void validateMoveTest_InvalidMove() {
    // more than one space
    assertTrue(checkMove.validateMove(position54, position34, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position34, whitePlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47, whitePlayerMock).containsKey(false));

    // Single attempt to move backward
    assertTrue(checkMove.validateMove(position34, position43, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position63, whitePlayerMock).containsKey(false));

    // move to white
    assertTrue(checkMove.validateMove(position54, position44, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position44, whitePlayerMock).containsKey(false));

    // move to occupied
    assertTrue(checkMove.validateMove(position34, position23, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position34, position23, whitePlayerMock).containsKey(false));

  }
}
