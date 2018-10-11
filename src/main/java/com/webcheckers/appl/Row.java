package com.webcheckers.appl;

import java.util.ArrayList;

/**
 * Class for each row on the game board
 */
public class Row {

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
          row.add(new Space(rowNumber, i, Space.Color.BLACK));
        } else {
          Space space = new Space(rowNumber, i, Space.Color.WHITE);
          row.add(space);
          if (rowNumber >= 5 && rowNumber <= 7) {
            space.setPiece(new Piece("red", space));
          } else if (rowNumber >= 0 && rowNumber <= 2) {
            space.setPiece(new Piece("white", space));
          }
        }
      } else {
        if ((rowNumber + i) % 2 == 0) {
          row.add(new Space(rowNumber, i, Space.Color.BLACK));
        } else {
          Space space = new Space(rowNumber, i, Space.Color.WHITE);
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
  public int index() {
    return this.index;
  }

  /**
   * The iterator for the rows for the frontend to loop over
   *
   * @return an arraylist containing all of the spaces in the row
   */
  public ArrayList<Space> iterator() {
    return this.row;
  }
}
