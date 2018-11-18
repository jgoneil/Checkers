package com.webcheckers.model;

import com.webcheckers.appl.GameLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@Tag("Model-tier")
public class FindBestMoveTest {

  private Player redPlayer;
  private Player whitePlayer;
  private List<Piece> pieceList;

  @BeforeEach
  void setup() {
    this.redPlayer = new Player("Joe");
    this.whitePlayer = new Player("Tim");
    redPlayer.setColor(GameLobby.RED);
    whitePlayer.setColor(GameLobby.WHITE);
    pieceList = new ArrayList<>();
  }

  @Test
  void testSingleMoveRed() {
    Piece redPiece = new Piece(GameLobby.RED, new Space(3,2, Space.Color.BLACK));
    pieceList.add(redPiece);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, redPlayer);
    Move move = findBestMove.findMove();
    assertEquals(2, move.getEndRow());
    assertEquals(1, move.getEndCell());
  }

  @Test
  void testNoMoveRed() {
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, redPlayer);
    Move move = findBestMove.findMove();
    assertNull(move);
  }

  @Test
  void testOneJumpSingleRed() {
    Piece redPiece = new Piece(GameLobby.RED, new Space(3, 2, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(7, 0, Space.Color.BLACK));
    Piece whitePiece = new Piece(GameLobby.WHITE, new Space(2, 3, Space.Color.BLACK));
    pieceList.add(redPiece);
    pieceList.add(whitePiece);
    pieceList.add(redPieceTwo);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, redPlayer);
    Move move = findBestMove.findMove();
    assertEquals(1, move.getEndRow());
    assertEquals(4, move.getEndCell());
  }

  @Test
  void testOneJumpMultipleRed() {
    Piece redPiece = new Piece(GameLobby.RED, new Space(5, 2, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(7, 0, Space.Color.BLACK));
    Piece whitePiece = new Piece(GameLobby.WHITE, new Space(4, 3, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.WHITE, new Space(2, 5, Space.Color.BLACK));
    Piece redPieceThree = new Piece(GameLobby.RED, new Space(3, 6, Space.Color.BLACK));
    pieceList.add(redPiece);
    pieceList.add(whitePiece);
    pieceList.add(redPieceTwo);
    pieceList.add(whitePieceTwo);
    pieceList.add(redPieceThree);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, redPlayer);
    Move move = findBestMove.findMove();
    assertEquals(1, move.getEndRow());
    assertEquals(6, move.getEndCell());
  }

  @Test
  void testMultipleMovesRed() {
    Piece redPieceOne = new Piece(GameLobby.RED, new Space(5, 2, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(5, 4, Space.Color.BLACK));
    pieceList.add(redPieceOne);
    pieceList.add(redPieceTwo);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, redPlayer);
    Move move = findBestMove.findMove();
    assertEquals(4, move.getEndRow());
    if (move.getEndCell() != 1 && move.getEndCell() != 3) {
      fail();
    }
  }

  @Test
  void testMultipleSingleJumpsRed() {
    Piece redPieceOne = new Piece(GameLobby.RED, new Space(5, 2, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(5, 4, Space.Color.BLACK));
    Piece whitePieceOne = new Piece(GameLobby.WHITE, new Space(4, 1, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.WHITE, new Space(4, 5, Space.Color.BLACK));
    pieceList.add(redPieceOne);
    pieceList.add(redPieceTwo);
    pieceList.add(whitePieceOne);
    pieceList.add(whitePieceTwo);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, redPlayer);
    Move move = findBestMove.findMove();
    assertEquals(3, move.getEndRow());
    if (move.getEndCell() != 0 && move.getEndCell() != 6) {
      fail();
    }
  }

  @Test
  void testMultipleMultiJumpsRed() {
    Piece redPieceOne = new Piece(GameLobby.RED, new Space(5, 2, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(5, 4, Space.Color.BLACK));
    Piece whitePieceOne = new Piece(GameLobby.WHITE, new Space(4, 3, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.WHITE, new Space(2, 5, Space.Color.BLACK));
    Piece whitePieceThree = new Piece(GameLobby.WHITE, new Space(2, 1, Space.Color.BLACK));
    pieceList.add(redPieceOne);
    pieceList.add(redPieceTwo);
    pieceList.add(whitePieceOne);
    pieceList.add(whitePieceTwo);
    pieceList.add(whitePieceThree);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, redPlayer);
    Move move = findBestMove.findMove();
    assertEquals(1, move.getEndRow());
    if (move.getEndCell() != 0 && move.getEndCell() != 6) {
      fail();
    }
  }

  @Test
  void testSingleJumpAndKingRed() {

  }

  @Test
  void testMultiJumpAndKingRed() {

  }

  @Test
  void testSingleJumpAndEatenRed() {

  }

  @Test
  void testMultiJumpAndEatenRed() {

  }

  @Test
  void testSingleMoveWhite() {
    Piece whitePiece = new Piece(GameLobby.WHITE, new Space(3,2, Space.Color.BLACK));
    pieceList.add(whitePiece);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, whitePlayer);
    Move move = findBestMove.findMove();
    assertEquals(4, move.getEndRow());
    assertEquals(1, move.getEndCell());
  }

  @Test
  void testNoMoveWhite() {
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, whitePlayer);
    Move move = findBestMove.findMove();
    assertNull(move);
  }

  @Test
  void testOneJumpSingleWhite() {
    Piece whitePiece = new Piece(GameLobby.WHITE, new Space(3, 2, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.WHITE, new Space(0, 0, Space.Color.BLACK));
    Piece redPiece = new Piece(GameLobby.RED, new Space(4, 3, Space.Color.BLACK));
    pieceList.add(redPiece);
    pieceList.add(whitePiece);
    pieceList.add(whitePieceTwo);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, whitePlayer);
    Move move = findBestMove.findMove();
    assertEquals(5, move.getEndRow());
    assertEquals(4, move.getEndCell());
  }

  @Test
  void testOneJumpMultipleWhite() {
    Piece whitePiece = new Piece(GameLobby.WHITE, new Space(1, 2, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.WHITE, new Space(0, 7, Space.Color.BLACK));
    Piece redPiece = new Piece(GameLobby.RED, new Space(2, 3, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(4, 5, Space.Color.BLACK));
    Piece whitePieceThree = new Piece(GameLobby.WHITE, new Space(4, 6, Space.Color.BLACK));
    pieceList.add(redPiece);
    pieceList.add(whitePiece);
    pieceList.add(redPieceTwo);
    pieceList.add(whitePieceTwo);
    pieceList.add(whitePieceThree);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, whitePlayer);
    Move move = findBestMove.findMove();
    assertEquals(5, move.getEndRow());
    assertEquals(6, move.getEndCell());
  }

  @Test
  void testMultipleMovesWhite() {
    Piece whitePieceOne = new Piece(GameLobby.WHITE, new Space(2, 1, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.RED, new Space(2, 3, Space.Color.BLACK));
    pieceList.add(whitePieceOne);
    pieceList.add(whitePieceTwo);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, whitePlayer);
    Move move = findBestMove.findMove();
    assertEquals(3, move.getEndRow());
    if (move.getEndCell() != 0 && move.getEndCell() != 2) {
      fail();
    }
  }

  @Test
  void testMultipleSingleJumpsWhite() {
    Piece whitePieceOne = new Piece(GameLobby.WHITE, new Space(2, 1, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.WHITE, new Space(2, 5, Space.Color.BLACK));
    Piece redPieceOne = new Piece(GameLobby.RED, new Space(3, 2, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(3, 6, Space.Color.BLACK));
    pieceList.add(redPieceOne);
    pieceList.add(redPieceTwo);
    pieceList.add(whitePieceOne);
    pieceList.add(whitePieceTwo);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, whitePlayer);
    Move move = findBestMove.findMove();
    assertEquals(4, move.getEndRow());
    if (move.getEndCell() != 3 && move.getEndCell() != 7) {
      fail();
    }
  }

  @Test
  void testMultipleMultiJumpsWhite() {
    Piece whitePieceOne = new Piece(GameLobby.WHITE, new Space(2, 3, Space.Color.BLACK));
    Piece whitePieceTwo = new Piece(GameLobby.WHITE, new Space(2, 5, Space.Color.BLACK));
    Piece redPieceOne = new Piece(GameLobby.RED, new Space(3, 4, Space.Color.BLACK));
    Piece redPieceTwo = new Piece(GameLobby.RED, new Space(5, 2, Space.Color.BLACK));
    Piece redPieceThree = new Piece(GameLobby.RED, new Space(5, 6, Space.Color.BLACK));
    pieceList.add(redPieceOne);
    pieceList.add(redPieceTwo);
    pieceList.add(whitePieceOne);
    pieceList.add(whitePieceTwo);
    pieceList.add(redPieceThree);
    ModelBoard board = new ModelBoard(redPlayer, whitePlayer, 8, pieceList);
    FindBestMove findBestMove = new FindBestMove(board, whitePlayer);
    Move move = findBestMove.findMove();
    assertEquals(6, move.getEndRow());
    if (move.getEndCell() != 1 && move.getEndCell() != 7) {
      fail();
    }
  }

  @Test
  void testSingleJumpAndKingWhite() {

  }

  @Test
  void testMultiJumpAndKingWhite() {

  }

  @Test
  void testSingleJumpAndEatenWhite() {

  }

  @Test
  void testMultiJumpAndEatenWhite() {

  }

}