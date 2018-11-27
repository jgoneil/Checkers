package com.webcheckers.model;

/**
 * Model class for the position a space is at
 */
public class Position {

  //The y-coordinate of the position
  private int row;
  //The x-coordinate of the position
  private int cell;

  /**
   * Constructor for the location of a position
   *
   * @param row the y-coordinate of the position
   * @param cell the x-coordinate of the position
   */
  public Position(int row, int cell) {
    this.row = row;
    this.cell = cell;
  }

  /**
   * Getter for the row the space is located at
   *
   * @return the integer (between 0-7) for the y-coordinate of the space
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Getter for the column the space is located at
   *
   * @return the integer (between 0-7) for the x-coordinate of the space
   */
  public int getCell() {
    return this.cell;
  }

  /**
   * Equals method for checking if two positions are equivalent to one another
   *
   * @param obj the potential position being checked for equivalence
   * @return true/false based on if the positions are equal to one another or not
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Position) {
      Position check = (Position) obj;
      if (check.getRow() == this.getRow()) {
        if (check.getCell() == this.getCell()) {
          return true;
        }
      }
    }
    return false;
  }
}
