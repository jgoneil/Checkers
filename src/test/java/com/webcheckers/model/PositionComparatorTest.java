package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class PositionComparatorTest {

  @Test
  void checkRightLarger() {
    Position first = new Position(1, 0);
    Position last = new Position(1, 1);
    Position test = new Position(1, 2);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(1, positionComparator.compare(test, last));
  }

  @Test
  void checkRightSmaller() {
    Position first = new Position(1, 0);
    Position last = new Position(1, 1);
    Position test = new Position(1, 2);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(-1, positionComparator.compare(first, test));
  }

  @Test
  void checkRightEqual() {
    Position first = new Position(1, 0);
    Position last = new Position(1, 1);
    Position test = new Position(1, 2);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(0, positionComparator.compare(test, test));
  }

  @Test
  void checkLeftLarger() {
    Position first = new Position(1, 1);
    Position last = new Position(1, 0);
    Position test = new Position(1, 2);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(1, positionComparator.compare(first, test));
  }

  @Test
  void checkLeftSmaller() {
    Position first = new Position(1, 1);
    Position last = new Position(1, 0);
    Position test = new Position(1, 2);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(-1, positionComparator.compare(test, last));
  }

  @Test
  void checkLeftEqual() {
    Position first = new Position(1, 1);
    Position last = new Position(1, 0);
    Position test = new Position(1, 2);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(0, positionComparator.compare(test, test));
  }

  @Test
  void checkDownLarger() {
    Position first = new Position(1, 1);
    Position last = new Position(0, 1);
    Position test = new Position(2, 1);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(1, positionComparator.compare(first, test));
  }

  @Test
  void checkDownSmaller() {
    Position first = new Position(1, 1);
    Position last = new Position(0, 1);
    Position test = new Position(2, 1);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(-1, positionComparator.compare(test, last));
  }

  @Test
  void checkDownEqual() {
    Position first = new Position(1, 1);
    Position last = new Position(0, 1);
    Position test = new Position(0, 1);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(0, positionComparator.compare(test, test));
  }

  @Test
  void checkUpLarger() {
    Position first = new Position(0, 1);
    Position last = new Position(1, 1);
    Position test = new Position(2, 1);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(1, positionComparator.compare(test, last));
  }

  @Test
  void checkUpSmaller() {
    Position first = new Position(0, 1);
    Position last = new Position(1, 1);
    Position test = new Position(0, 1);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(-1, positionComparator.compare(test, last));
  }

  @Test
  void checkUpEqual() {
    Position first = new Position(0, 1);
    Position last = new Position(1, 1);
    Position test = new Position(2, 1);
    PositionComparator positionComparator = new PositionComparator(first, last);
    assertEquals(0, positionComparator.compare(test, test));
  }


}
