package com.webcheckers.appl;

import java.util.ArrayList;

/**
 * Class that creates and handles the board for gameplay
 */
public class Board {

  private Player redPlayer;
  private Player whitePlayer;
  private ArrayList<Row> board;
  private boolean redTurn;

  /**
   * Main constructor for class that establishes the two players and size of the board
   *
   * @param redPlayer   the player associated to the red checkers for the checkers game
   * @param whitePlayer the player associated to the white checkers for the checkers game
   * @param length      the size of the width and length of the board (assuming the board has to be a square)
   */
  public Board(Player redPlayer, Player whitePlayer, int length, String player) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.board = new ArrayList<>();
    this.redTurn = true;
    for (int i = 0; i < length; i++) {
      Row row = new Row(i, length, player);
      board.add(row);
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
   * @return an arraylist containing rows of spaces for each board position
   */
  public ArrayList<Row> getBoard() {
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
   * Getter for a specific row in the board
   *
   * @param row the row of the board being retrieved
   * @return the row associated to the row of the board (or null if non-existent)
   */ 
  public Row getRow(int row) {
    return this.board.get(row);
  }

  /**
   * Iterator declaration used for getting all of the spaces on the board
   *
   * @return an arraylist containing all of the rows on the board
   */
  public ArrayList<Row> iterator() {
    return this.board;
  }
}
