package com.webcheckers.model;

/**
 * Class that establishes spaces on the game board
 */
public class Space {

  //The column that the space is located at
  private int cellIdx;
  //The row that the space is located at
  private int xCoordinate;
  //The color of the space
  private Color color;
  //Boolean condition based on if the space is occupied or not
  private boolean occupied;
  //The piece occupying the space or null if no piece is currently occupying the space
  private Piece piece;

  /**
   * Constructor for the space on the game board
   *
   * @param xCoordinate the row of the space on the board (the y-coordinate of the space)
   * @param cellIdx the column of the space on the board (x-coordinate of the space)
   * @param color the color the space is
   */
  public Space(int xCoordinate, int cellIdx, Color color) {
    this.cellIdx = cellIdx;
    this.xCoordinate = xCoordinate;
    this.color = color;
    this.occupied = false;
  }

  /**
   * Getter for the color of the given space
   *
   * @return - the color of the space
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Sets occupied value to true for space with a piece
   *
   * @param piece the piece now occupying the space
   */
  public void occupy(Piece piece) {
    this.occupied = true;
    this.piece = piece;
  }

  /**
   * Sets occupied value to false when a piece has moved off of it
   */
  public void unoccupy() {
    this.occupied = false;
    this.piece = null;
  }

  /**
   * Checks to see if the piece is white
   *
   * @return true/false if a piece on the space is white;
   */
  public boolean pieceIsRed() {
    if (this.piece != null) {
      return piece.isRed();
    }
    return false;
  }

  /**
   * Checks to see if the piece is white
   *
   * @return true/false if a piece on the space is white;
   */
  public boolean pieceIsWhite() {
    if (this.piece != null) {
      return piece.isWhite();
    }
    return false;
  }

  /**
   * Sees if this space is occupied
   *
   * @return boolean for if the space is occupied
   */
  public boolean isOccupied() {
    return this.occupied;
  }

  /**
   * Getter for the piece on the space
   *
   * @return the piece on the space (null if no piece exists)
   */
  public Piece getPiece() {
    return this.piece;
  }

  /**
   * Getter for the location of the space on the game board
   *
   * @return the location of the space
   */
  public int getCellIdx() {
    return this.cellIdx;
  }

  /**
   * Getter for the x-coordinate for the space on the game board
   *
   * @return the xCoordinate of the space on the board
   */
  public int getxCoordinate() {
    return this.xCoordinate;
  }

  /**
   * Checks to see if a space is a valid location to move a piece to
   *
   * @return a boolean expression based on if a piece can or can not be moved to the given space
   */
  public boolean isValid() {
    return this.piece == null && this.color == Color.BLACK;
  }

  /**
   * Checks to see if the piece on the space is of type KING or not
   *
   * @return true/false if the piece on this space is of type KING
   */
  public boolean isPieceKing() {
    if (this.piece != null) {
      return this.piece.getType() == Piece.Type.KING;
    }
    return false;
  }

  /**
   * Checks to see if a space is white or not
   *
   * @return true/false if the space is white
   */
  public boolean isWhite() {
    return color == Color.WHITE;
  }

  /**
   * Checks to see if a space is equal to another object or not
   *
   * @param space the space being checked to see if it is equal or not
   * @return a boolean condition based on if the spaces are equal to one another or not
   */
  @Override
  public boolean equals(Object space) {
    if (space instanceof Space) {
      Space checkSpace = (Space) space;
      if (checkSpace.getCellIdx() == this.cellIdx
          && checkSpace.getxCoordinate() == this.xCoordinate
          && checkSpace.getColor().equals(this.color)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Enum for the color of the space (white/black)
   */
  public enum Color {
    BLACK, WHITE
  }
}
