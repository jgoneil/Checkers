package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserTest {

  final String validPlayerString2 = "DEF";
  final String validPlayerString3 = "123";
  final String validPlayerString4 = "a b";
  final String validPlayerString5 = " a ";
  final String nonAlphabeticalString1 = "$$$";
  final String nonAlphabeticalString2 = "\"System.out.println(\"Sanitize your input\");";
  final String emptyString = "";
  final String whiteSpace = " ";
  final String tab = "\t";
  final String newLine = "\n";
  final private String validPlayerString1 = "abc";
  private Users users;


  @BeforeEach
  public void setUp() throws Exception {
    users = new Users();

  }

  @AfterEach
  public void tearDown() throws Exception {
    users = null;

  }

  @ParameterizedTest
  @ValueSource(strings = {validPlayerString1, validPlayerString2, validPlayerString3,
      validPlayerString4, validPlayerString5})
  public void AddPlayerTest_ValidName(String username) {
    assertTrue(users.addPlayer(username));
  }

  @ParameterizedTest
  @ValueSource(strings = {nonAlphabeticalString1, nonAlphabeticalString2})
  public void AddPlayerTest_InvalidName_InvalidCharacters(String username) {
    assertFalse(users.addPlayer(username));
  }

  @ParameterizedTest
  @ValueSource(strings = {emptyString, whiteSpace, tab, newLine})
  public void AddPlayerTest_InvalidName_InvalidWhiteSpace(String username) {
    assertFalse(users.addPlayer(username));
  }

  @Test
  public void GetSpecificPlayerTest() {
    users.addPlayer(validPlayerString1);
    assertEquals(users.getSpecificPlayer(validPlayerString1).getUsername(), validPlayerString1);
    assertNull(users.getSpecificPlayer("Not a user"));
  }

  @Test
  public void getAllPlayersTest() {
    users.addPlayer(validPlayerString1);
    users.addPlayer(validPlayerString2);
    assertEquals(users.getAllPlayers().get(0), validPlayerString1);
    assertEquals(users.getAllPlayers().get(1), validPlayerString2);
  }

  @Test
  public void getAllPlayersExceptUserTest() {
    users.addPlayer(validPlayerString1);
    users.addPlayer(validPlayerString2);
    users.addPlayer(validPlayerString3);
    assertEquals(2, users.getAllPlayersExceptUser(validPlayerString1).size());
    assertEquals(3, users.getAllPlayersExceptUser("").size());

  }
}
