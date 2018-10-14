package com.webcheckers.model;

/**
 * Model class for receiving JQuery requests for a move
 */
public class Move {

  private Position start;
  private Position end;

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
}
