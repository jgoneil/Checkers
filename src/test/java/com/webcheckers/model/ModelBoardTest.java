package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.model.Space.Color;
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
    PlayerBoardView playerBoardViewRed = new PlayerBoardView(player1Mock, player2Mock, 8, "red");
    PlayerBoardView playerBoardViewWhite = new PlayerBoardView(player2Mock, player1Mock, 8, "white");
    modelBoard = new ModelBoard(player1Mock, player2Mock, 8, playerBoardViewRed, playerBoardViewWhite);
  }

  @AfterEach
  void tearDown() {
    player1Mock = null;
    player2Mock = null;
  }

  @Test
  void submitMove() {
    modelBoard.madeMove(new Move(new Position(5, 0), new Position(6, 1)));
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

  @Test
  void redPiecesList(){
    assertNotNull(modelBoard.getRedPieces());
    assertEquals(12, modelBoard.getRedPieces().size());
  }

  @Test
  void whitePiecesList() {
    assertNotNull(modelBoard.getWhitePieces());
    assertEquals(12, modelBoard.getWhitePieces().size());
  }

  @Test
  void isBecomingKingTest(){
    Piece piece = new Piece("red", new Space(6, 1, Color.BLACK));
    Move move = (new Move(new Position(6,1), new Position(7,0)));
    modelBoard.madeMove(move);
    Space space = new Space(7, 0 , Color.BLACK);
    modelBoard.addPieceToSpace(piece, space);

    assertTrue(modelBoard.isBecomingKing(piece, move.getEnd().getCell()));
    assertFalse(modelBoard.isBecomingKing(piece, move.getStart().getRow()));

    piece.King();
    assertFalse(modelBoard.isBecomingKing(piece, move.getEnd().getRow()));
  }
}