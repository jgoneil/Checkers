package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Space.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Tag("Model-tier")
class ModelBoardTest {

  private Player player1Mock;
  private Player player2Mock;
  private ModelBoard modelBoard;
  private ModelBoard modelBoardTest;
  private ModelBoard modelBoardEmpty;
  private Piece pieceRed;
  private Piece pieceWhite;

  @BeforeEach
  void setUp() {
    player1Mock = new Player("a");
    player2Mock = new Player("b");
    modelBoard = new ModelBoard(player1Mock, player2Mock, 8);
    this.pieceRed = new Piece("red", new Space(4, 1, Color.BLACK));
    this.pieceWhite = new Piece("white", new Space(0, 1, Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(pieceRed);
    pieces.add(pieceWhite);
    List<Piece> piecesEmpty = new ArrayList<>();
    modelBoardEmpty = new ModelBoard(player1Mock, player2Mock, 8, piecesEmpty);
    modelBoardTest = new ModelBoard(player1Mock, player2Mock, 8, pieces);
    player1Mock.setColor(GameLobby.RED);
    player2Mock.setColor(GameLobby.WHITE);
  }

  @AfterEach
  void tearDown() {
    player1Mock = null;
    player2Mock = null;
  }

  @Test
  void submitMove() {
    modelBoard.pendingMove(new Move(new Position(5, 0), new Position(6, 1)));
    modelBoard.submitMove();
  }

  @Test
  void CreateBoardWithSpecifiedPieces() {
    assertEquals(modelBoardTest.getSpace(4, 1).getPiece(), pieceRed);
    assertEquals(modelBoardTest.getSpace(0, 1).getPiece(), pieceWhite);
    assertNull(modelBoardTest.getSpace(0, 3).getPiece());
  }

  @Test
  void CreateEmptyBoard() {
    assertNull(modelBoardEmpty.getSpace(0,1).getPiece());
    assertFalse(modelBoardEmpty.whiteCanPlay());
    assertFalse(modelBoardEmpty.redCanPlay());
  }

  @Test
  void backupMoveTest(){
    Move move = (new Move(new Position(5, 0), new Position(4, 1)));
    modelBoard.pendingMove(move);
    modelBoard.setPendingMove(true);

    modelBoard.backupMove();

    assertFalse(modelBoard.checkPendingMove());
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
  void getInvalidSpace() {
    assertNull(modelBoard.getSpace(3, 18));
    assertNull(modelBoard.getSpace(821, 6));
    assertNull(modelBoard.getSpace(23, 18));
    assertEquals(3, modelBoard.getSpace(3, 6).getxCoordinate());
    assertEquals(6, modelBoard.getSpace(3, 6).getCellIdx());

  }

  @Test
  void isBecomingKingTest(){
    Piece piece = new Piece("red", new Space(6, 1, Color.BLACK));
    Move move = (new Move(new Position(6,1), new Position(7,0)));
    modelBoard.pendingMove(move);
    Space space = new Space(7, 0 , Color.BLACK);
    modelBoard.addPieceToSpace(piece, space);

    assertTrue(modelBoard.isBecomingKing(piece, move.getEnd().getCell()));
    assertFalse(modelBoard.isBecomingKing(piece, move.getStart().getRow()));

    piece.King();
    assertFalse(modelBoard.isBecomingKing(piece, move.getEnd().getRow()));
  }

  @Test
  void eatRedPiece() {
    Space space = new Space(6, 1, Color.BLACK);
    Piece piece = new Piece("red", space);
    modelBoard.eatPiece(piece);

    assertFalse(modelBoard.getRedPieces().contains(piece));
  }

  @Test
  void eatWhitePiece() {
    Space space = new Space(6, 1, Color.BLACK);
    Piece piece = new Piece("white", space);
    modelBoard.eatPiece(piece);

    assertFalse(modelBoard.getRedPieces().contains(piece));
  }

  @Test
  void redJumpBackUp() {
    Piece redPiece = new Piece("red", new Space(5, 0, Color.BLACK));
    Piece whitePiece = new Piece("white", new Space(4, 1, Color.BLACK));
    Move move = (new Move(new Position(5, 0), new Position(3, 2)));
    modelBoard.getWhitePieces().add(whitePiece);
    modelBoard.pendingMove(move);
    modelBoard.eatPiece(whitePiece);
    modelBoard.addPieceToSpace(redPiece, new Space(3, 2, Color.BLACK));

    modelBoard.backupMove();

    assertFalse(modelBoard.checkPendingMove());
    assertTrue(modelBoard.getSpace(5,0).isOccupied());
    assertTrue(modelBoard.getSpace(3,2).isOccupied());
    assertFalse(modelBoard.getSpace(4, 1).isOccupied());
  }

  @Test
  void whiteJumpBackUp() {
    modelBoard.setMove(false);
    Piece redPiece = new Piece("red", new Space(4, 1, Color.BLACK));
    Piece whitePiece = new Piece("white", new Space(3, 2, Color.BLACK));
    Move move = (new Move(new Position(3, 2), new Position(5, 0)));
    modelBoard.getRedPieces().add(redPiece);
    modelBoard.pendingMove(move);
    modelBoard.eatPiece(redPiece);
    modelBoard.addPieceToSpace(whitePiece, new Space(5, 0, Color.BLACK));

    modelBoard.backupMove();

    assertFalse(modelBoard.checkPendingMove());
    assertFalse(modelBoard.getSpace(4,1).isOccupied());
  }
}