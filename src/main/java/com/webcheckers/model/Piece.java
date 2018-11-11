package com.webcheckers.model;

import com.webcheckers.appl.GameLobby;

/**
 * Defines a piece for use on the board
 */
public class Piece {

  //The space the piece is occupying
  private Space loc;
  //The color of the piece
  private Color color;
  //The type that the piece is
  private Type type;

  /**
   * Enum for the color of the piece (red/white)
   */
  public enum Color {
    RED, WHITE
  }

  /**
   * Enum for the type of the piece (King/Single)
   */
  public enum Type {
    SINGLE, KING
  }

  /**
   * Constructor for the piece class
   *
   * @param tempColor the color passed in for the piece
   * @param s the space the piece starts out on
   */
  public Piece(String tempColor, Space s) {
    this.loc = s;
    if (tempColor.equals(GameLobby.RED)) {
      this.color = Color.RED;
    } else {
      this.color = Color.WHITE;
    }
    this.type = Type.SINGLE;
  }

  /**
   * Setter for the space the piece occupies when the player moves the piece
   *
   * @param space the space the player moves the piece to
   */
  public void move(Space space) {
    this.loc = space;
  }

  /**
   * Getter for the space the piece is currently on
   */
  public Space getSpace() {
    return this.loc;
  }

  /**
   * Gets the xCoordinate of the space the piece is on
   *
   * @return the xCoordinae of the space the piece is on
   */
  public int getXCoordinate() {
    return this.loc.getxCoordinate();
  }

  /**
   * Gets the cellIdx of the space the piece is on
   *
   * @return the cellIdx of the space the piece is on
   */
  public int getCellIdx() {
    return this.loc.getCellIdx();
  }

  /**
   * Getter for the type of piece
   *
   * @return the enum for the type of the piece
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Getter for the color of the piece
   *
   * @return the enum for the color of the piece
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Checks to see if the piece is red
   *
   * @return true/false to see if a piece is red or not
   */ 
  public boolean isRed() {
    return this.color == Color.RED;
  }

  /**
   * Checks to see if the piece is white
   *
   * @return true/false to see if a piece is white or not
   */
  public boolean isWhite() {
    return this.color == Color.WHITE;
  }

  /**
   * Kings a piece
   */
  public void King(){this.type = Type.KING;}

  /**
   * Checks to see if a piece is of type KING
   *
   * @return true/false if the type of the piece is a KING or not
   */
  public boolean isKing() {
    return this.type == Type.KING;
  }
}
