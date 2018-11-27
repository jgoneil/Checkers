package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import com.webcheckers.appl.GameLobby;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class AIPlayerTest {

  private AiPlayer aiPlayer;

  @BeforeEach
  void setUp() {
    aiPlayer = new AiPlayer();
  }

  @AfterEach
  void tearDown() {
    aiPlayer = null;
  }

//  @Test
//  void constructorTest(){
//    assertEquals(aiPlayer.getName(), "F@ke 1");
//    aiPlayer = new AiPlayer();
//    assertEquals(aiPlayer.getName(), "F@ke 2");
//  }

}
