package com.webcheckers.model;

/**
 * Model class for the position a space is at
 */
public class Position {

  private int row;
  private int cell;

  public Position(int row, int cell) {
    this.row = row;
    this.cell = cell;
  }

  /**
   * Getter for the row the space is located at
   *
   * @return the integer (between 0-7) for the x-coordinate of the space
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Getter for the column the space is located at
   *
   * @return the integer (between 0-7) for the y-coordinate of the space
   */
  public int getCell() {
    return this.cell;
  }
}
