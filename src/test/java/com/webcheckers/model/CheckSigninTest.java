package com.webcheckers.model;

import java.util.*;
import com.webcheckers.appl.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class CheckSigninTest {

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

  List<Player> users;
  CheckSignin checkSignin;

  @BeforeEach
  void setUp() throws Exception {
    users = new ArrayList<>();
    checkSignin = new CheckSignin();

  }

  @AfterEach
  void tearDown() throws Exception {
    users = null;
    checkSignin = null;

  }

  @ParameterizedTest
  @ValueSource(strings = {validPlayerString1, validPlayerString2, validPlayerString3,
      validPlayerString4, validPlayerString5})
  void ValidateUserTest_ValidUser(String user) {
    assertTrue(checkSignin.validateUser(user, users));
  }

  @ParameterizedTest
  @ValueSource(strings = {nonAlphabeticalString1, nonAlphabeticalString2})
  void ValidateUserTest_InvalidUser(String user) {
    assertFalse(checkSignin.validateUser(user, users));
  }

  @ParameterizedTest
  @ValueSource(strings = {emptyString, whiteSpace, tab, newLine})
  void ValidateUserTest_InvalidWhitespace(String user) {
    assertFalse(checkSignin.validateUser(user, users));
  }

  @Test
  void ValidateUserTest_AlreadyTaken() {
    users.add(new Player(validPlayerString1));
    assertFalse(checkSignin.validateUser(validPlayerString1, users));
  }
}
