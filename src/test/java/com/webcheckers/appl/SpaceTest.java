package com.webcheckers.appl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {

  private Space space1;
  private Space space2;

  @BeforeEach
  void setUp() {
    space1 = new Space(1,2, Space.Color.BLACK);
  }

  @Test
  void getColorTest() {
    space2 = new Space(3,2, Space.Color.WHITE);
    assert (space1.getColor() == Space.Color.BLACK);
    assert (space2.getColor() == Space.Color.WHITE);
  }

  @Test
  void occupyTest() {
    space1.occupy();
    assert (space1.isOccupied());
  }

  @Test
  void isOccupiedTest() {

  }

  @Test
  void unoccupyTest() {
    space1.occupy();
    assert (space1.isOccupied());
    space1.unopccupy();
    assert (!space1.isOccupied());
  }

  @Test
  void cellIdxTest() {
    space2 = new Space(1,2, Space.Color.WHITE);
    assert (space1.cellIdx().equals(space2.cellIdx()));
  }


}