package com.webcheckers.model;

import java.util.Comparator;

/**
 * Comparator class used for sorting positions in order of the direction the move goes in
 */
public class PositionComparator implements Comparator<Position> {

  //Static final constants used for state
  private static final String LEFT = "MOVE TO LEFT";
  private static final String RIGHT = "MOVE TO RIGHT";
  private static final String UP = "MOVE UPWARDS";
  private static final String DOWN = "MOVE DOWNWARDS";

  String direction;

  /**
   * Constructor for the Position Comparator. Takes in the starting and ending positions to figure out the direction
   * of the move.
   *
   * @param start the starting position of the move
   * @param end the ending position of the move
   */
  public PositionComparator(Position start, Position end) {
    this.direction = findDirection(start, end);
  }

  /**
   * Finds the direction for the given move
   *
   * @param start the starting position for the move
   * @param end the ending position for the move
   * @return a string representing the type of move made
   */
  private String findDirection(Position start, Position end) {
    if (start.getCell() - end.getCell() > 0) {
      return LEFT;
    } else if (start.getCell() - end.getCell() < 0) {
      return RIGHT;
    } else {
      if (start.getRow() - end.getRow() > 0) {
        return DOWN;
      } else {
        return UP;
      }
    }
  }

  /**
   * Comparision function for ordering positions. Based on the direction of the move
   *
   * @param first the first position for comparison
   * @param second the second position for comparison
   * @return an integer representing if the first position is greater than, less than, or equal to the second position
   */
  @Override
  public int compare(Position first, Position second) {
    if (direction.equals(RIGHT)) {
      if (first.getCell() > second.getCell()) {
        return 1;
      } else if (first.getCell() < second.getCell()) {
        return -1;
      }
      return 0;
    } else if(direction.equals(LEFT)) {
      if (first.getCell() > second.getCell()) {
        return -1;
      } else if (first.getCell() < second.getCell()) {
        return 1;
      }
      return 0;
    } else if (direction.equals(UP)) {
      if (first.getRow() > second.getRow()) {
        return 1;
      } else if (first.getRow() < second.getRow()) {
        return -1;
      }
      return 0;
    } else {
      if (first.getRow() > second.getRow()) {
        return -1;
      } else if (first.getRow() < second.getRow()) {
        return 1;
      }
      return 0;
    }
  }
}
