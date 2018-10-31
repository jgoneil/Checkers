package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that creates and handles the board for gameplay
 */
public class PlayerBoardView implements Iterable {

  //Players for the current game view
  private Player redPlayer;
  private Player whitePlayer;
  //The list of rows for the board view
  private ArrayList<Row> board;

  /**
   * Main constructor for class that establishes the two players and size of the board
   *
   * @param redPlayer the player associated to the red checkers for the checkers game
   * @param whitePlayer the player associated to the white checkers for the checkers game
   * @param length the size of the width and length of the board (assuming the board has to be a
   * square)
   * @param color the color of the player
   */
  public PlayerBoardView(Player redPlayer, Player whitePlayer, int length, String color) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.board = new ArrayList<>();
    //Generating the rows for the board
    for (int i = 0; i < length; i++) {
      Row row = new Row(i, length, color);
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
   * @return an array list containing rows of spaces for each board position
   */
  public ArrayList<Row> getBoard() {
    return this.board;
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
   * Setter for a move made on the board
   *
   * @param move the move on the board
   */
  public void makeMove(Move move, boolean isKinging) {
    Row startingRow = board.get(move.getStart().getRow());
    Piece piece = startingRow.removePieceFromSpace(move.getStart().getCell());
    if (isKinging){
      piece.King();
    }
    Row endingRow = board.get(move.getEnd().getRow());
    endingRow.addPieceToSpace(move.getEnd().getCell(), piece);
  }

  /**
   * Iterator declaration used for getting all of the spaces on the board
   *
   * @return the iterator for the board
   */
  @Override
  public Iterator iterator() {
    return this.board.iterator();
  }

  /**
   * Removes a piece from the playerBoard
   *
   * @param row the row the piece is in
   * @param column the column the piece is in
   */
  public void eatPiece(int row, int column) {
    board.get(row).removePieceFromSpace(column);
  }
}
