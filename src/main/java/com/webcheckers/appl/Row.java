package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for each row on the game board
 */
public class Row {

  private HashMap<Space, Integer> row;
  private int index;

  /**
   * Constructor for the row class
   *
   * @param rowNumber the number of the row 
   * @param length the length of the side of the board
   */
  public Row(int rowNumber, int length) {
    this.row = new HashMap<>();
    this.index = rowNumber;
    for(int i = 0; i < length; i++) {
      if ((rowNumber + i) % 2 == 0) {
        row.put(new Space(rowNumber, i, Space.Color.BLACK), i);
      } else {
        row.put(new Space(rowNumber, i, Space.Color.WHITE), i);
      }
    }
  }

  /**
   * Retrieves the row of the game board in the form of a map
   *
   * @return the hashmap of spaces and the column of the space 
   */
  public HashMap<Space, Integer> getRow() {
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
    ArrayList<Space> spaces = new ArrayList<>();
    for (Space s: row.keySet()){
      spaces.add(s);
      }
    return spaces;
  }
}
