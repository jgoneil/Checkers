package com.webcheckers.model;

import com.webcheckers.appl.Piece;
import com.webcheckers.appl.Space;
import java.util.Map;
import java.util.HashMap;

/**
 * Model class to handle checking moves for validation
 */
public class CheckMove {

  private ModelBoard board;

  /**
   * Constructor for the CheckMove Class
   *
   * @param board the board for the model (2-D Array of Spaces)
   */
  public CheckMove(ModelBoard board) {
    this.board = board;
  }

  /**
   * Sees if a space is valid for a piece to move onto
   *
   * @param start - space currently at
   * @param target - target space to move to
   * @return - validity of move to target space
   */
  public Map<Boolean, String> validateMove(Position start, Position target) {
    Map<Boolean, String> response = new HashMap<>();
    Space current = board.getSpace(start.getRow(), start.getCell());
    Space goal = board.getSpace(target.getRow(), target.getCell());
    if (goal.getColor().equals(Space.Color.WHITE)) {
      response.put(false, "Attempted to move a piece to a white space.");
    } else if (goal.isOccupied()) {
      response.put(false, "Attempted to move a piece to an already occupied space");
    } else {
//    Moves piece on board
//    Commented out for now
//
//      board.addPieceToSpace(current.getPiece(), goal);
//      current.occupy(current.getPiece());
//      current.unoccupy();
      response.put(false, "This move is valid, but cannot be seen on the board yet.");
    }
    return response;
  }
}
