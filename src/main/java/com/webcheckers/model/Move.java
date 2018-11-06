package com.webcheckers.model;

/**
 * Model class for receiving JQuery requests for a move
 */
public class Move {

  //The starting location of a piece attempting to move
  private Position start;
  //The goal location that a piece is attempting to move to
  private Position end;

  /**
   * Constructor to create a new move
   *
   * @param start the position where the piece is currently at
   * @param end the position where the piece is attempting to move to
   */
  public Move(Position start, Position end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Getter for the starting position
   *
   * @return the position the piece is currently on
   */
  public Position getStart() {
    return this.start;
  }

  /**
   * Getter for the ending position
   *
   * @return the position the piece is attempting to move to
   */
  public Position getEnd() {
    return this.end;
  }

  /**
   * Gets the the row of the starting position in the move
   *
   * @return the row of the starting position in the move
   */
  int getStartRow() {
    return this.start.getRow();
  }

  /**
   * Gets the cell of the starting position in the move
   *
   * @return the cell of the starting position in the move
   */
  int getStartCell() {
    return this.start.getCell();
  }

  /**
   * Gets the row of the ending position in the move
   *
   * @return the row of the ending position in the move
   */
  int getEndRow() {
    return this.end.getRow();
  }

  /**
   * Gets the cell of the starting position in the move
   *
   * @return the cell of the ending position in the move
   */
  int getEndCell() {
    return this.end.getCell();
  }
}
