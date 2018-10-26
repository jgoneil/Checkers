package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for User
 */
@Tag("Application-tier")
public class UserTest {

  // Valid usernames
  final String validPlayerString1 = "abc";
  final String validPlayerString2 = "DEF";
  final String validPlayerString3 = "123";
  final String validPlayerString4 = "a b";
  final String validPlayerString5 = " a ";

  // Invalid usernames
  final String nonAlphabeticalString1 = "$$$";
  final String nonAlphabeticalString2 = "\"System.out.println(\"Sanitize your input\");";

  // Invalid whitespace
  final String emptyString = "";
  final String whiteSpace = " ";
  final String tab = "\t";
  final String newLine = "\n";

  private PlayerLobby playerLobby;


  @BeforeEach
  void setUp() throws Exception {
    playerLobby = new PlayerLobby();

  }

  @AfterEach
  void tearDown() throws Exception {
    playerLobby = null;

  }

  @ParameterizedTest
  @ValueSource(strings = {validPlayerString1, validPlayerString2, validPlayerString3,
      validPlayerString4, validPlayerString5})
  void AddPlayerTest_ValidName(String username) {
    assertTrue(playerLobby.addPlayer(username));
  }

  @ParameterizedTest
  @ValueSource(strings = {nonAlphabeticalString1, nonAlphabeticalString2})
  void AddPlayerTest_InvalidName_InvalidCharacters(String username) {
    assertFalse(playerLobby.addPlayer(username));
  }

  @ParameterizedTest
  @ValueSource(strings = {emptyString, whiteSpace, tab, newLine})
  void AddPlayerTest_InvalidName_InvalidWhiteSpace(String username) {
    assertFalse(playerLobby.addPlayer(username));
  }

  @Test
  void GetSpecificPlayerTest() {
    playerLobby.getAllPlayers().add("No players");
    assertNull(playerLobby.getSpecificPlayer("No players"));
    playerLobby.addPlayer(validPlayerString1);
    assertEquals(playerLobby.getSpecificPlayer(validPlayerString1).getName(), validPlayerString1);
    assertNull(playerLobby.getSpecificPlayer("Not a user"));
  }

  @Test
  void getAllPlayersTest() {
    playerLobby.addPlayer(validPlayerString1);
    playerLobby.addPlayer(validPlayerString2);
    assertEquals(playerLobby.getAllPlayers().get(0), validPlayerString1);
    assertEquals(playerLobby.getAllPlayers().get(1), validPlayerString2);
  }

  @Test
  void getAllPlayersExceptUserTest() {
    playerLobby.addPlayer(validPlayerString1);
    playerLobby.addPlayer(validPlayerString2);
    playerLobby.addPlayer(validPlayerString3);
    assertEquals(2, playerLobby.getAllPlayersExceptUser(validPlayerString1).size());
    assertEquals(3, playerLobby.getAllPlayersExceptUser("").size());

  }

  @Test
  void removePlayer() {
    playerLobby.addPlayer(validPlayerString1);
    playerLobby.addPlayer(validPlayerString2);
    playerLobby.addPlayer(validPlayerString3);
    playerLobby.removeUser(validPlayerString1);
    assertEquals(2, playerLobby.getAllPlayers().size());
  }
}
