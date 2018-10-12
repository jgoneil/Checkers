package com.webcheckers.model;

import com.webcheckers.appl.Space;
import com.webcheckers.model.ModelBoard;

/**
 * Model class to handle checking moves for validation
 */
public class CheckMove {

  private ModelBoard board;

  /**
   * Constructor for the CheckMove Class
   * @param board the board for the model (2-D Array of Spaces)
   */
  public CheckMove(ModelBoard board) {
    this.board = board; 
  }

  /**
  * Sees if a space is valid for a piece to move onto
  * @param start - space currently at
  * @param target - target space to move to
  * @return - validity of move to target space
  */
  public boolean validateMove(Space start, Space target){
    if(target.getColor().equals(Space.Color.WHITE)){
      return false;
    } else if(target.isOccupied()){
      return false;
    }
    /**
     * Moves piece on board
     * Commented out for now
    board.addPieceToSpace(start.piece(), target);
    target.setPiece(start.piece());
    start.unopccupy();
    */
    return true;
  }
}
