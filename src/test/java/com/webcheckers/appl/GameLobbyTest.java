package com.webcheckers.appl;

import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class GameLobbyTest {

  //Temp players for the game lobby creation
  private Player redPlayer;
  private Player whitePlayer;

  //Game lobby for the test
  private GameLobby gameLobby;

  @BeforeEach
  void setUp() throws Exception {
    this.redPlayer = new Player("Bob");
    this.whitePlayer = new Player("Joe");
    this.gameLobby = new GameLobby(redPlayer, whitePlayer);
  }

  @Test
  void testGetBoardView() {
    assertNotNull(gameLobby.getBoardForPlayer(redPlayer));
    assertNotNull(gameLobby.getBoardForPlayer(whitePlayer));
    assertNull(gameLobby.getBoardForPlayer(new Player("NULL")));
  }

  @Test
  void testOpponentPlayer() {
    assertEquals(whitePlayer, gameLobby.getOpponent(redPlayer));
    assertEquals(redPlayer, gameLobby.getOpponent(whitePlayer));
  }

  @Test
  void testCheckRedPlayer() {
    assertTrue(gameLobby.checkRedPlayer(redPlayer.getName()));
    assertFalse(gameLobby.checkRedPlayer(whitePlayer.getName()));
  }

  @Test
  void testCheckRedTurn() {
    assertTrue(gameLobby.checkRedTurn());
    Map<Boolean, String> resultTrue = gameLobby.validateMove(new Position(5, 2),
            new Position(4, 3), this.redPlayer);
    gameLobby.pendingMove(new Move(new Position(5, 2), new Position(4, 3)));
    gameLobby.submitMove();
    assertFalse(gameLobby.checkRedTurn());
  }

  @Test
  void testValidateMove() {
    Map<Boolean, String> resultTrue = gameLobby.validateMove(new Position(5, 2),
            new Position(4, 3), this.redPlayer);
    Map<Boolean, String> resultFalse = gameLobby.validateMove(new Position(1, 1),
            new Position(7, 7), this.redPlayer);
    assertTrue(resultTrue.containsKey(true));
    assertTrue(resultFalse.containsKey(false));
  }

  @Test
  void testBackupMove() {
    Map<Boolean, String> resultTrue = gameLobby.validateMove(new Position(5, 2),
            new Position(4, 3), this.redPlayer);
    gameLobby.pendingMove(new Move(new Position(5, 2), new Position(4, 3)));
    gameLobby.submitMove();
    gameLobby.backupMove();
  }
}