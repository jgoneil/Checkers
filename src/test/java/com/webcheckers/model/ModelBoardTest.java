package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Piece;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Space;
import com.webcheckers.appl.Space.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class ModelBoardTest {

  private Player player1Mock;
  private Player player2Mock;
  private ModelBoard modelBoard;

  @BeforeEach
  void setUp() {
    player1Mock = new Player("a");
    player2Mock = new Player("b");
    modelBoard = new ModelBoard(player1Mock, player2Mock, 8);
    BoardView boardViewRed = new BoardView(player1Mock, player2Mock, 8, "red");
    BoardView boardViewWhite = new BoardView(player2Mock, player1Mock, 8, "white");
    player1Mock.setColor("Red", boardViewRed);
    player2Mock.setColor("White", boardViewWhite);
  }

  @AfterEach
  void tearDown() {
    player1Mock = null;
    player2Mock = null;
  }

  @Test
  void submitMove() {
    modelBoard.madeMove(new Move(new Position(5, 0), new Position(6, 1)));
    player1Mock.addModelBoard(modelBoard);
    player2Mock.addModelBoard(modelBoard);
    modelBoard.submitMove();
  }

  @Test
  void backupMoveTest(){
    Piece piece = new Piece("red", new Space(5, 0, Color.BLACK));
    Move move = (new Move(new Position(5, 0), new Position(4, 1)));
    modelBoard.madeMove(move);
    modelBoard.addPieceToSpace(piece, new Space(4, 1 , Color.BLACK));

    modelBoard.backupMove();

    assertFalse(modelBoard.checkMadeMove());
    assertTrue(modelBoard.getSpace(5,0).isOccupied());
    assertFalse(modelBoard.getSpace(4,1).isOccupied());
  }
}