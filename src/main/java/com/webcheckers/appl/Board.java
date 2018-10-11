package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that creates and handles the board for gameplay
 */
public class Board {

  private Player redPlayer;
  private Player whitePlayer;
  private HashMap<Row, Integer> board;
  private boolean redTurn;

  /**
   * Main constructor for class that establishes the two players and size of the board
   *
   * @param redPlayer   the player associated to the red checkers for the checkers game
   * @param whitePlayer the player associated to the white checkers for the checkers game
   * @param length      the size of the width and length of the board (assuming the board has to be a square)
   */
  public Board(Player redPlayer, Player whitePlayer, int length) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.board = new HashMap<>();
    this.redTurn = true;
    for (int i = 0; i < length; i++) {
      Row row = new Row(i, length);
      board.put(row, i);
    }
  }

  /**
   * Getter for the white player of the game
   *
   * @return the player associated to the white checkers for the checkers game
   */
  public Player getWhitePlayer() {
    return this.whitePlayer;
  }

  /**
   * Getter for the red player of the game
   *
   * @return the player associated to the red checkers for the checkers game
   */
  public Player getRedPlayer() {
    return this.redPlayer;
  }

  /**
   * Getter for the board (A set of a set of spaces)
   *
   * @return the set of a set of spaces for each board position
   */
  public HashMap<Row, Integer> getBoard() {
    return this.board;
  }

  /**
   * Boolean expression to determine who's turn it is
   *
   * @return a true or false statement based on if it is the red player's turn or not
   */
  public boolean redTurn() {
    return this.redTurn;
  }

  /**
   * Declares a turn switch for the board
   */
  public void turnEnded() {
    this.redTurn = !this.redTurn;
  }

  /**
   * Iterator declaration used for getting all of the spaces on the board
   *
   * @return an arraylist containing all of the rows on the board
   */
  public ArrayList<Row> iterator() {
    ArrayList<Row> rows = new ArrayList<>();
    for (Row row: board.keySet()){
      rows.add(row);
    }
    return rows;
  }
}
