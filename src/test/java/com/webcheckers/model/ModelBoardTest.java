package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.webcheckers.appl.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
class ModelBoardTest {

  private Player player1Mock;
  private Player player2Mock;
  private ModelBoard modelBoard;

  @BeforeEach
  void setUp() {
    player1Mock = mock(Player.class);
    player2Mock = mock(Player.class);
    modelBoard = new ModelBoard(player1Mock, player2Mock, 8);

  }

  @AfterEach
  void tearDown() {
    player1Mock = null;
    player2Mock = null;

  }

  @Test
  void getSpace() {
  }

  @Test
  void addPieceToSpace() {
  }
}