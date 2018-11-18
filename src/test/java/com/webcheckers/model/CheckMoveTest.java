package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.appl.GameLobby;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  private Position position45;
  private Position position36;
  private Position position52;
  private Position position56;
  private Position position01;
  private Position position41;
  private Position position05;
  private Position position76;
  private Position position72;

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
    position52 = new Position(5, 2);
    position56 = new Position(5, 6);

    position01 = new Position(0, 1);
    position41 = new Position(4, 1);
    position05 = new Position(0, 5);
    position76 = new Position(7, 6);
    position72 = new Position(7, 2);

    position32 = new Position(3,2);
    position45 = new Position(4,5);
    position36 = new Position(3,6);

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
    assertTrue(checkMove.validateMove(position54, position43, redPlayerMock).containsKey(true));
    modelBoard.setMove(true);
    assertTrue(checkMove.validateMove(position54, position43, whitePlayerMock).containsKey(true));
  }

  @Test
  void validateKingMoveTest_Valid(){
    //King pieces for move
    Space kingSpace1 = modelBoard.getSpace(position47.getRow(), position47.getCell());
    kingSpace1.occupy(new Piece(GameLobby.RED, kingSpace1));
    kingSpace1.getPiece().King();
    modelBoard.eatPiece(modelBoard.getSpace(position50.getRow(), position50.getCell()).getPiece());
    Space kingSpace2 =  modelBoard.getSpace(position45.getRow(), position45.getCell());
    kingSpace2.occupy(new Piece(GameLobby.WHITE, kingSpace2));
    kingSpace2.getPiece().King();

    //Clear target space
    modelBoard.eatPiece(modelBoard.getSpace(position54.getRow(), position54.getCell()).getPiece());
    modelBoard.eatPiece(modelBoard.getSpace(position52.getRow(), position52.getCell()).getPiece());

    modelBoard.eatPiece(modelBoard.getSpace(position56.getRow(), position56.getCell()).getPiece());

    //King Valid Backward Move
    assertTrue(checkMove.validateMove(position47, position56, redPlayerMock).containsKey(true));
    assertTrue(checkMove.validateMove(position45, position36, whitePlayerMock).containsKey(true));
  }

  @Test
  void validateMoveTest_InvalidMove() {
    // more than one space
    assertTrue(checkMove.validateMove(position54, position34, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position34, whitePlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position47, whitePlayerMock).containsKey(false));

    // move backward
    assertTrue(checkMove.validateMove(position34, position43, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position63, whitePlayerMock).containsKey(false));

    // move to white
    assertTrue(checkMove.validateMove(position54, position44, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position54, position44, whitePlayerMock).containsKey(false));

    // move to occupied
    assertTrue(checkMove.validateMove(position34, position23, redPlayerMock).containsKey(false));
    assertTrue(checkMove.validateMove(position34, position23, whitePlayerMock).containsKey(false));

    // move forward not diagonally
    assertTrue(checkMove.validateMove(position50, position54, redPlayerMock).containsKey(false));
  }

  @Test
  void validateJump() {
    modelBoard.addPieceToSpace(new Piece(GameLobby.WHITE, new Space(4, 1, Space.Color.BLACK)),
            new Space(4, 1, Space.Color.BLACK));
    assertTrue(checkMove.validateMove(position50, position32, redPlayerMock).containsKey(true));
  }

  @Test
  void CantJump() {
    modelBoard.addPieceToSpace(new Piece(GameLobby.RED, new Space(5, 0, Space.Color.BLACK)),
            new Space(3, 2, Space.Color.BLACK));
    modelBoard.addPieceToSpace(new Piece(GameLobby.WHITE, new Space(4, 1, Space.Color.BLACK)),
            new Space(4, 1, Space.Color.BLACK));
    assertTrue(checkMove.validateMove(position54, position43, redPlayerMock).containsKey(false));
  }

  @Test
  void redCantMove() {
    Piece redPiece = new Piece(GameLobby.RED, new Space(1, 0, Space.Color.BLACK));
    Piece pieceUpperRight = new Piece(GameLobby.WHITE, new Space(0, 1, Space.Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(redPiece);
    pieces.add(pieceUpperRight);

    ModelBoard customModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkCustomBoard = new CheckMove(customModelBoard);

    assertFalse(checkCustomBoard.moveAvailable(redPlayerMock));
    assertTrue(checkCustomBoard.moveAvailable(whitePlayerMock));
  }

  @Test
  void whiteCantMove() {
    Piece whitePiece = new Piece(GameLobby.WHITE, new Space(6, 7, Space.Color.BLACK));
    Piece pieceUpperRight = new Piece(GameLobby.RED, new Space(7, 6, Space.Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(whitePiece);
    pieces.add(pieceUpperRight);

    ModelBoard customModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkCustomBoard = new CheckMove(customModelBoard);

    assertFalse(checkCustomBoard.moveAvailable(whitePlayerMock));
    assertTrue(checkCustomBoard.moveAvailable(redPlayerMock));
  }

  @Test
  void bothCanMove() {
    assertTrue(checkMove.moveAvailable(redPlayerMock));
    assertTrue(checkMove.moveAvailable(whitePlayerMock));
  }

  @Test
  void validateRedKingJump() {
    Space kingSpace1 = modelBoard.getSpace(position50.getRow(), position50.getCell());
    kingSpace1.occupy(new Piece(GameLobby.RED, kingSpace1));
    kingSpace1.getPiece().King();

    modelBoard.addPieceToSpace(new Piece(GameLobby.WHITE, new Space(4, 1, Space.Color.BLACK)),
            new Space(4, 1, Space.Color.BLACK));
    assertTrue(checkMove.validateMove(position50, position32, redPlayerMock).containsKey(true));
  }

  @Test
  void validateRedKingJumpMultiple() {
    Piece kingRedPiece = new Piece(GameLobby.RED, new Space(2, 3, Space.Color.BLACK));
    kingRedPiece.King();
    Piece pieceUpperLeft = new Piece(GameLobby.WHITE, new Space(1, 2, Space.Color.BLACK));
    Piece pieceUpperRight = new Piece(GameLobby.WHITE, new Space(3, 2, Space.Color.BLACK));
    Piece pieceBottomLeft = new Piece(GameLobby.WHITE, new Space(1, 4, Space.Color.BLACK));
    Piece pieceBottomRight = new Piece(GameLobby.WHITE, new Space(3, 4, Space.Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(kingRedPiece);
    pieces.add(pieceUpperLeft);
    pieces.add(pieceUpperRight);
    pieces.add(pieceBottomLeft);
    pieces.add(pieceBottomRight);

    ModelBoard customModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkCustomBoard = new CheckMove(customModelBoard);

    assertTrue(checkCustomBoard.validateMove(position23, position01, redPlayerMock).containsKey(true));
    assertTrue(checkCustomBoard.validateMove(position23, position05, redPlayerMock).containsKey(true));
    assertTrue(checkCustomBoard.validateMove(position23, position41, redPlayerMock).containsKey(true));
    assertTrue(checkCustomBoard.validateMove(position23, position45, redPlayerMock).containsKey(true));
  }

  @Test
  void validateWhiteKingJumpMultiple() {
    Piece kingWhitePiece = new Piece(GameLobby.WHITE, new Space(2, 3, Space.Color.BLACK));
    kingWhitePiece.King();
    Piece pieceUpperLeft = new Piece(GameLobby.RED, new Space(1, 2, Space.Color.BLACK));
    Piece pieceUpperRight = new Piece(GameLobby.RED, new Space(3, 2, Space.Color.BLACK));
    Piece pieceBottomLeft = new Piece(GameLobby.RED, new Space(1, 4, Space.Color.BLACK));
    Piece pieceBottomRight = new Piece(GameLobby.RED, new Space(3, 4, Space.Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(kingWhitePiece);
    pieces.add(pieceUpperLeft);
    pieces.add(pieceUpperRight);
    pieces.add(pieceBottomLeft);
    pieces.add(pieceBottomRight);

    ModelBoard customModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkCustomBoard = new CheckMove(customModelBoard);

    assertTrue(checkCustomBoard.validateMove(position54, position76, whitePlayerMock).containsKey(true));
    assertTrue(checkCustomBoard.validateMove(position54, position72, whitePlayerMock).containsKey(true));
    assertTrue(checkCustomBoard.validateMove(position54, position36, whitePlayerMock).containsKey(true));
    assertTrue(checkCustomBoard.validateMove(position54, position32, whitePlayerMock).containsKey(true));
  }

  @Test
  void twoMultiJumps() {
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece(GameLobby.RED, new Space(6,1, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(6,5, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(5,2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(5,4, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(3,2, Space.Color.BLACK)));

    ModelBoard customModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkCustomBoard = new CheckMove(customModelBoard);
    Position middlePosition = new Position(4, 3);

    Map<Boolean, String> moveSingleOne = checkCustomBoard.validateMove(new Position(6, 1),
            middlePosition, redPlayerMock);
    Map<Boolean, String> moveSingleTwo = checkCustomBoard.validateMove(new Position(6, 5),
            middlePosition, redPlayerMock);

    assertTrue(moveSingleOne.containsKey(true));
    assertEquals(moveSingleOne.get(true), CheckMove.multiJumpPossible);
    assertTrue(moveSingleTwo.containsKey(true));
    assertEquals(moveSingleTwo.get(true), CheckMove.multiJumpPossible);
  }

  @Test
  void kingJumpOnlyOption() {
    List<Piece> pieces = new ArrayList<>();
    Piece kingPieceRed = new Piece(GameLobby.RED, new Space(0, 3, Space.Color.BLACK));
    kingPieceRed.King();
    pieces.add(kingPieceRed);
    pieces.add(new Piece(GameLobby.WHITE, new Space(1, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(5, 0, Space.Color.BLACK)));

    ModelBoard customModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkCustomBoard = new CheckMove(customModelBoard);

    Map<Boolean, String> attemptMoveRedFalse = checkCustomBoard.validateMove(new Position(5, 0),
            new Position(4, 1), redPlayerMock);
    Map<Boolean, String> attemptMoveRedTrue = checkCustomBoard.validateMove(new Position(0, 3),
            new Position(2, 1), redPlayerMock);
    int i = 0;
    assertTrue(attemptMoveRedFalse.containsKey(false));
    assertEquals(attemptMoveRedFalse.get(false), "Attempted to move when jump is possible.");
    assertTrue(attemptMoveRedTrue.containsKey(true));
    assertEquals(attemptMoveRedTrue.get(true), "This jump is valid.");
  }

  @Test
  void moveRedKingSingleAvailable() {
    List<Piece> pieces = new ArrayList<>();
    Piece kingRed = new Piece(GameLobby.RED, new Space(2, 3, Space.Color.BLACK));
    kingRed.King();
    pieces.add(kingRed);
    pieces.add(new Piece(GameLobby.RED, new Space(1, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(1, 4, Space.Color.BLACK)));

    ModelBoard cusomModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkMove = new CheckMove(cusomModelBoard);

    assertTrue(checkMove.moveAvailable(redPlayerMock));
  }

  @Test
  void moveWhiteKingSingleAvailable() {
    List<Piece> pieces = new ArrayList<>();
    Piece kingRed = new Piece(GameLobby.WHITE, new Space(2, 3, Space.Color.BLACK));
    kingRed.King();
    pieces.add(kingRed);
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 4, Space.Color.BLACK)));

    ModelBoard cusomModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkMove = new CheckMove(cusomModelBoard);

    assertTrue(checkMove.moveAvailable(whitePlayerMock));
  }

  @Test
  void moveRedKingJumpAvailable() {
    List<Piece> pieces = new ArrayList<>();
    Piece kingRed = new Piece(GameLobby.RED, new Space(2, 3, Space.Color.BLACK));
    kingRed.King();
    pieces.add(kingRed);
    pieces.add(new Piece(GameLobby.RED, new Space(1, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(1, 4, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 4, Space.Color.BLACK)));

    ModelBoard cusomModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkMove = new CheckMove(cusomModelBoard);

    assertTrue(checkMove.moveAvailable(redPlayerMock));
  }

  @Test
  void moveWhiteKingJumpAvailable() {
    List<Piece> pieces = new ArrayList<>();
    Piece kingRed = new Piece(GameLobby.WHITE,  new Space(2, 3, Space.Color.BLACK));
    kingRed.King();
    pieces.add(kingRed);
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 4, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(1, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(1, 4, Space.Color.BLACK)));

    ModelBoard cusomModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkMove = new CheckMove(cusomModelBoard);

    assertTrue(checkMove.moveAvailable(whitePlayerMock));
  }

  @Test
  void moveRedKingMultiJumpAvailable() {
    List<Piece> pieces = new ArrayList<>();
    Piece kingRed = new Piece(GameLobby.RED,  new Space(2, 3, Space.Color.BLACK));
    kingRed.King();
    pieces.add(kingRed);
    pieces.add(new Piece(GameLobby.RED, new Space(1, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(1, 4, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 4, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(3, 6, Space.Color.BLACK)));

    ModelBoard cusomModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkMove = new CheckMove(cusomModelBoard);

    assertTrue(checkMove.moveAvailable(redPlayerMock));
  }

  @Test
  void moveWhiteKingMultiJumpAvailable() {
    List<Piece> pieces = new ArrayList<>();
    Piece kingRed = new Piece(GameLobby.WHITE,  new Space(4, 3, Space.Color.BLACK));
    kingRed.King();
    pieces.add(kingRed);
    pieces.add(new Piece(GameLobby.WHITE, new Space(5, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(5, 4, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(3, 2, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.RED, new Space(3, 4, Space.Color.BLACK)));

    ModelBoard cusomModelBoard = new ModelBoard(redPlayerMock, whitePlayerMock, 8, pieces);
    CheckMove checkMove = new CheckMove(cusomModelBoard);

    assertTrue(checkMove.moveAvailable(whitePlayerMock));
  }
}
