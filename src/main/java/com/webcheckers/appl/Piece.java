package com.webcheckers.appl;

/**
 * Defines a piece for use on the board
 */
public class Piece {

  private enum Color {
    RED, WHITE
  }

  private enum Type {
    SINGLE, KING
  }

  private Space loc;
  private Color color;
  private Type type;

  /**
   * Constructor for the piece class
   * @param tempColor the color passed in for the piece
   * @param s the space the piece starts out on
   */
  public Piece(String tempColor, Space s){
    this.loc = s;
    if(tempColor.equals("red")){
      this.color = Color.RED;
    }
    else{
      this.color = Color.WHITE;
    }
    this.type = Type.SINGLE;
  }

  /**
   * Setter for the space the piece occupies when the player moves the piece
   * @param space the space the player moves the piece to
   */
  public void move(Space space) {
    this.loc = space;
  }

  /**
   * Getter for the space the piece is currenly on
   */
  public Space getSpace() {
    return this.loc;
  }

  /**
   * Getter for the type of piece
   * @return the enum for the type of the piece
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Getter for the color of the piece
   * @return the enum for the color of the piece
   */
  public Color getColor() {
    return this.color;
  }
}
