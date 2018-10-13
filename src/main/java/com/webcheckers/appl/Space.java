package com.webcheckers.appl;

/**
 * Class that establishes spaces on the game board
 */
public class Space {
  public enum Color{
    BLACK, WHITE
  }
  private int cellIdx;
  private int xCoordinate;
  private Color color;
  private boolean occupied;
  private Piece piece;

  /**
   * Constructor for the space on the game board
   * @param cellIdx the idex of the space on the board (y-coordinate of the space)
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
   * @return - the color of the space
   */
  public Color getColor(){
    return this.color;
  }

  /**
   * Sets occupied value to true for space with a piece
   */
  public void occupy(){
    this.occupied = true;
  }

  /**
   * Sets occupied value to false when a piece has moved off of it
   */
  public void unopccupy(){
    this.occupied = false;
    this.piece = null;
  }

  /**
   * Sees if this space is occupied
   * @return boolean for if the space is occupied
   */
  public boolean isOccupied(){
    return this.occupied;
  }

  /**
   * Setter for a piece that occupies this space on the board
   * @param piece the piece being added to the space
   */
  public void setPiece(Piece piece) {
    this.piece = piece;
    this.occupied = true;
  }

  /**
   * Getter for the piece on the space
   * @return the piece on the space (null if no piece exists)
   */
  public Piece getPiece() {
    return this.piece;
  }

  /**
   * Getter for the location of the space on the game board
   * @return the location of the space
   */
  public int getCellIdx() {
    return this.cellIdx;
  }

  /**
   * Getter for the x-coordinate for the space on the game board
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
    if(this.piece == null && this.color == Color.BLACK){
      return true;
    }
    return false;
  }
}
