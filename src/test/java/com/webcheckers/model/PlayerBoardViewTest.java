package com.webcheckers.model;

<<<<<<< HEAD:src/test/java/com/webcheckers/appl/BoardViewTest.java
import com.webcheckers.model.ModelBoard;
=======

>>>>>>> 341cb3a868ec2ee95b3286ce8c9f85e56d0f2a16:src/test/java/com/webcheckers/model/PlayerBoardViewTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("Application-tier")
public class PlayerBoardViewTest {

  private Player whitePlayer;
  private Player redPLayer;
<<<<<<< HEAD:src/test/java/com/webcheckers/appl/BoardViewTest.java
  private BoardView boardView1;
  private BoardView boardViewTesting;
  private Piece piece;
=======
  private PlayerBoardView playerBoardView1;
>>>>>>> 341cb3a868ec2ee95b3286ce8c9f85e56d0f2a16:src/test/java/com/webcheckers/model/PlayerBoardViewTest.java
  private ModelBoard modelBoard;

  @BeforeEach
  public void setUp() throws Exception {
    this.whitePlayer = new Player("White");
    this.redPLayer = new Player("Red");
<<<<<<< HEAD:src/test/java/com/webcheckers/appl/BoardViewTest.java
    this.boardView1 = new BoardView(redPLayer, whitePlayer, 8, "red");
    this.piece = new Piece("red", new Space(4, 1, Space.Color.BLACK));
    List<Piece> pieces = new ArrayList<>();
    pieces.add(piece);
    this.boardViewTesting = new BoardView(redPLayer, whitePlayer, 8, "red", pieces);
=======
    this.playerBoardView1 = new PlayerBoardView(redPLayer, whitePlayer, 8, "red");
>>>>>>> 341cb3a868ec2ee95b3286ce8c9f85e56d0f2a16:src/test/java/com/webcheckers/model/PlayerBoardViewTest.java
    this.modelBoard = new ModelBoard(redPLayer, whitePlayer, 8);
  }

  @Test
  void getWhitePlayer() {
    assertEquals(whitePlayer, playerBoardView1.getWhitePlayer());
  }

  @Test
  void getRedPlayer() {
    assertEquals(redPLayer, playerBoardView1.getRedPlayer());
  }

  @Test
  void getBoard() {
    assertNotNull(playerBoardView1.getBoard());
  }

  @Test
  void getRow() {
    assertNotNull(playerBoardView1.getRow(0));
  }

  @Test
  void checkTestBoardCreation() {
    assertEquals(boardViewTesting.getBoard().get(4).getSpace(1).getPiece(), piece);
    assertNull(boardViewTesting.getBoard().get(0).getSpace(0).getPiece());
  }

  @Test
  void getIterator() {
    assertNotNull(playerBoardView1.iterator());
  }

  @Test
  void redTurn() {
    assertTrue(modelBoard.checkRedTurn());
    modelBoard.setMove(true);
    assertFalse(modelBoard.checkRedTurn());
  }

  @Test
  void turnEnded() {
    assertTrue(modelBoard.checkRedTurn());
    modelBoard.setMove(true);
    assertFalse(modelBoard.checkRedTurn());
  }
}