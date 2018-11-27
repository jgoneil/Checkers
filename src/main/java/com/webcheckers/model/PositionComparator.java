package com.webcheckers.model;

import java.util.Comparator;

public class PositionComparator implements Comparator<Position> {

  private static final String LEFT = "MOVE TO LEFT";
  private static final String RIGHT = "MOVE TO RIGHT";
  private static final String UP = "MOVE UPWARDS";
  private static final String DOWN = "MOVE DOWNWARDS";

  String direction;

  public PositionComparator(Position start, Position end) {
    this.direction = findDirection(start, end);
  }

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
