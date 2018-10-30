package com.webcheckers.model;

import com.webcheckers.appl.Piece;
import com.webcheckers.appl.Piece.Color;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Space;

import java.util.Map;
import java.util.HashMap;

/**
 * Model class to handle checking moves for validation
 */
public class CheckMove {

  //The board holding the logic for checking if a piece is valid or not (the model board)
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
   * Checks if the piece is moving diagonally
   *
   * @param start the starting position of the checker
   * @param end the ending position of the checker
   * @return true/false based on whether the move is diagonal
   */
  private boolean isMovingDiagonal(Position start, Position end) {
    int xDiff = Math.abs(start.getRow() - end.getRow());
    int yDiff = Math.abs(start.getCell() - end.getCell());
    return xDiff == yDiff;
  }

  /**
   * Checks to see if the distance for the column or row is too large
   *
   * @param start the starting position of the checker
   * @param end the ending position of the checker
   * @return true/false based on if the distance is too large or not
   */
  private boolean isMovingOne(Position start, Position end) {
    int xDiff = Math.abs(start.getRow() - end.getRow());
    int yDiff = Math.abs(start.getCell() - end.getCell());
    return xDiff == 1 && yDiff == 1;
  }

  /**
   * Checks to see if the distance for the column or row is two
   *
   * @param start the starting position of the checker
   * @param end the ending position of the checker
   * @return true/false based on if the distance is two or not
   */
  private boolean isMovingTwo(Space start, Space end) {
    int xDiff = Math.abs(start.getxCoordinate() - end.getxCoordinate());
    int yDiff = Math.abs(start.getCellIdx() - end.getCellIdx());
    return xDiff == 2 && yDiff == 2;
  }

  /**
   * Check to see if checker is moving forward
   *
   * @param start the starting position of the checker
   * @param end the ending position of the checker
   * @return true/false based on whether checker is moving forward
   */
  private boolean isMovingForward(Position start, Position end) {
    return end.getRow() < start.getRow();
  }

  /**
   * Check to see if any piece during the players turn can jump.
   *
   * @param player current player
   * @return true/false based on if the player has a piece that can jump.
   */
  private boolean canJump(Player player) {
    boolean canJump = false;
    if (player.getColor().equals("Red")) {
      for (Piece redPiece : board.getRedPieces()) {
        if (redPiece.getSpace().getxCoordinate() - 2 >= 0
            && redPiece.getSpace().getCellIdx() - 2 >= 0) {
          Space upperLeft = board.getSpace(redPiece.getSpace().getxCoordinate() - 2,
              redPiece.getSpace().getCellIdx() - 2);
          if (pieceCanJump(redPiece.getSpace(), upperLeft, player)) {
            canJump = true;
          }
        }

        if (redPiece.getSpace().getxCoordinate() -2 >=0
            && redPiece.getSpace().getCellIdx() + 2 <= 7) {
          Space upperRight = board.getSpace(redPiece.getSpace().getxCoordinate() - 2,
              redPiece.getSpace().getCellIdx() + 2);
          if (pieceCanJump(redPiece.getSpace(), upperRight, player)) {
            canJump = true;
          }
        }
      }
    } else {
      for (Piece whitePiece : board.getWhitePieces()) {
        if (whitePiece.getSpace().getxCoordinate() + 2 <= 7
            && whitePiece.getSpace().getCellIdx() + 2 <= 7) {
          Space upperLeft = board.getSpace(whitePiece.getSpace().getxCoordinate() + 2,
              whitePiece.getSpace().getCellIdx() + 2);
          if (pieceCanJump(whitePiece.getSpace(), upperLeft, player)) {
            canJump = true;
          }
        }

        if (whitePiece.getSpace().getxCoordinate() + 2 <= 7
            && whitePiece.getSpace().getCellIdx() - 2 >= 0) {
          Space upperRight = board.getSpace(whitePiece.getSpace().getxCoordinate() + 2,
              whitePiece.getSpace().getCellIdx() - 2);
          if (pieceCanJump(whitePiece.getSpace(), upperRight, player)) {
            canJump = true;
          }
        }
      }
    }
    return canJump;
  }

  /**
   * Checks to see if a piece is able to jump another piece or not
   *
   * @param start the space the jump is starting at
   * @param end the space the jump is attempting to move to
   * @param player the player attempting to make the jump
   * @return true/false if a jump can occur or not
   */
  private boolean pieceCanJump(Space start, Space end, Player player) {
    Space middle = board.getSpace((start.getxCoordinate() + end.getxCoordinate()) / 2,
        (start.getCellIdx() + end.getCellIdx()) / 2);

    if (isMovingTwo(start, end)) {
      if (!end.isOccupied()) {
        if (middle.isOccupied()) {
          if (player.getColor().equals("Red") && middle.getPiece().getColor().equals(Color.WHITE)) {
            return true;
          }

          if (player.getColor().equals("White") && middle.getPiece().getColor().equals(Color.RED)) {
            return true;
          }
        }
      }
    }

    return false;

  }

  /**
   * Sees if a space is valid for a piece to move onto
   *
   * @param start - space currently at
   * @param target - target space to move to
   * @return - validity of move to target space
   */
  public Map<Boolean, String> validateMove(Position start, Position target, Player player) {
    Map<Boolean, String> response = new HashMap<>();
    Space current;
    Space goal;
    if (player.getColor().equals("Red")) {
      current = board.getSpace(start.getRow(), start.getCell());
      goal = board.getSpace(target.getRow(), target.getCell());
    } else {
      current = board.getSpace(7 - start.getRow(), 7 - start.getCell());
      goal = board.getSpace(7 - target.getRow(), 7 - target.getCell());
    }
    if (canJump(player)) {
      if (pieceCanJump(current, goal, player)) {
        board.isJumping(true);
        response.put(true, "This jump is valid.");
      } else {
        response.put(false, "Attempted to move when jump is possible.");
      }
    } else {
      if (goal.getColor().equals(Space.Color.WHITE)) {
        response.put(false, "Attempted to move a piece to a white space.");
      } else if (goal.isOccupied()) {
        response.put(false, "Attempted to move a piece to an already occupied space");
      } else if (!isMovingOne(start, target)) {
        response.put(false, "Attempted to move piece too far.");
      } else if (!isMovingDiagonal(start, target)) {
        response.put(false, "Pieces can only move diagonally.");
      } else if (!isMovingForward(start, target)) {
        response.put(false, "Piece can only move forward");
      } else {
        response.put(true, "This move is valid.");
      }
    }
    return response;
  }
}
