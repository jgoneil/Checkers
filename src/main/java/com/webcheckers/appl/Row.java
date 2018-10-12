package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for each row on the game board
 */
public class Row implements Iterable{

  private ArrayList<Space> row;
  private int index;

  /**
   * Constructor for the row class
   *
   * @param rowNumber the number of the row 
   * @param length the length of the side of the board
   */
  public Row(int rowNumber, int length, String color) {
    this.row = new ArrayList<>();
    this.index = rowNumber;
    for(int i = 0; i < length; i++) {
      if(color.equals("red")) {
        if ((rowNumber + i) % 2 == 0) {
          row.add(new Space(rowNumber, i, Space.Color.WHITE));
        } else {
          Space space = new Space(rowNumber, i, Space.Color.BLACK);
          row.add(space);
          if (rowNumber >= 5 && rowNumber <= 7) {
            space.setPiece(new Piece("red", space));
          } else if (rowNumber >= 0 && rowNumber <= 2) {
            space.setPiece(new Piece("white", space));
          }
        }
      } else {
        if ((rowNumber + i) % 2 == 0) {
          row.add(new Space(rowNumber, i, Space.Color.WHITE));
        } else {
          Space space = new Space(rowNumber, i, Space.Color.BLACK);
          row.add(space);
          if (rowNumber >= 5 && rowNumber <= 7) {
            space.setPiece(new Piece("white", space));
          } else if (rowNumber >= 0 && rowNumber <= 2) {
            space.setPiece(new Piece("red", space));
          }
        }
      }
    }
  }

  /**
   * Retrieves the row of the game board in the form of a map
   *
   * @return the hashmap of spaces and the column of the space 
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
}
