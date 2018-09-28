package com.webcheckers.appl;

import static org.junit.jupiter.api.Assertions.*;

public class TestUser {

  private String player1String = "ABC";
  private String player2String = "DEF";
  private String player3String = "$$$";
  private Users users;

  public TestUser(){
    users = new Users();
  }

  public void assertAddPlayer(){
    assertFalse(users.addPlayer(player3String));
    assertTrue(users.addPlayer(player1String));
    assertTrue(users.addPlayer(player2String));
  }

  public void assertGetSpecificPlayer(){
    assertEquals(users.getSpecificPlayer(player2String).getUsername(), player2String);
  }

  public void assertgetAllPlayers(){
    assertEquals(users.getAllPlayers()[0], player2String);
  }

  public void assertgetAllPlayersExceptUser(){
    assertEquals(users.getAllPlayersExceptUser(player2String).length, 1);
  }
}
