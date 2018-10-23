package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for each row on the game board
 */
public class Row implements Iterable {

  //The list of spaces that make up the row
  private ArrayList<Space> row;
  //The index location of where on the board the row is
  private int index;

  /**
   * Constructor for the row class
   *
   * @param xCoordinate the xCoordinate of each space for row of the board
   * @param length the length of the side of the board
   * @param color the color of the player for the row being generated
   */
  public Row(int xCoordinate, int length, String color) {
    this.row = new ArrayList<>();
    this.index = xCoordinate;
    fillRow(length, color);
  }

  /**
   * Fills the arrayList of rows for the row being created
   *
   * @param length the length of the side of the board
   * @param color the color of the Player the row is being created for
   */
  private void fillRow(int length, String color) {
    for (int i = 0; i < length; i++) {
      if (color.equals("red")) {
        //Checking to see if a space should be white or not
        if ((this.index + i) % 2 == 0) {
          row.add(new Space(this.index, i, Space.Color.WHITE));
        } else {
          Space space = new Space(this.index, i, Space.Color.BLACK);
          row.add(space);
          //Checking to see if a space should be occupied or not
          if (this.index >= 5 && this.index <= 7) {
            space.occupy(new Piece("red", space));
          } else if (this.index >= 0 && this.index <= 2) {
            space.occupy(new Piece("white", space));
          }
        }
      } else {
        //Checking to see if a space should be white or not
        if ((this.index + i) % 2 == 0) {
          row.add(new Space(this.index, i, Space.Color.WHITE));
        } else {
          Space space = new Space(this.index, i, Space.Color.BLACK);
          row.add(space);
          //Checks to see if a space should be occupied or not
          if (this.index >= 5 && this.index <= 7) {
            space.occupy(new Piece("white", space));
          } else if (this.index >= 0 && this.index <= 2) {
            space.occupy(new Piece("red", space));
          }
        }
      }
    }
  }

  /**
   * Retrieves the row of the game board in the form of a map
   *
   * @return the array list of spaces and the column of the space
   */
  public ArrayList<Space> getRow() {
    return this.row;
  }

  /**
   * Getter for the index of the row
   *
   * @return the integer for the row number
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * Getter for a specified space in the row
   *
   * @param spaceIDX the index of the space
   * @return the space for the given index (or null if non-existent)
   */
  public Space getSpace(int spaceIDX) {
    return this.row.get(spaceIDX);
  }

  /**
   * The iterator for the rows for the frontend to loop over
   *
   * @return an iterator for the rows
   */
  public Iterator iterator() {
    return this.row.iterator();
  }

  /**
   * Updates the row to remove a piece from a space in the row
   *
   * @param spaceLoc the space that the piece is being removed from
   */
  public Piece removePieceFromSpace(int spaceLoc) {
    Space space = row.get(spaceLoc);
    Piece piece = space.getPiece();
    space.unoccupy();
    return piece;
  }

  /**
   * Updates the row to add a piece to a row
   *
   * @param spaceLoc the space that the piece is moving to
   */
  public void addPieceToSpace(int spaceLoc, Piece piece) {
    Space space = row.get(spaceLoc);
    space.occupy(piece);
  }
}
