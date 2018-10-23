package com.webcheckers.model;

import com.webcheckers.appl.Piece;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Space;

import java.util.List;
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
      List<Piece> redPieces = board.getRedPieces();
      for (int i = 0; i < redPieces.size(); i++) {
        Piece piece = redPieces.get(i);
        if(pieceCanJump(piece.getSpace(),player)){
          canJump = true;
        }
      }
    }
    else{
      List<Piece> whitePieces = board.getWhitePieces();
      for (int i = 0; i < whitePieces.size(); i++) {
        Piece piece = whitePieces.get(i);
        if(pieceCanJump(piece.getSpace(),player)){
          canJump = true;
        }
      }
    }
    return canJump;
  }

  private boolean pieceCanJump(Space start, Player player) {
    int pieceY = start.getxCoordinate();
    int pieceX = start.getCellIdx();
    if (player.getColor().equals("Red")) {
      System.out.println(pieceX + " " + pieceY);
      if (pieceX < 6 && pieceX > 1) {
        Space right1stSpace = board.getSpace(pieceX + 1, pieceY - 1);
        Space left1stSpace = board.getSpace(pieceX - 1, pieceY - 1);
        Space right2ndSpace = board.getSpace(pieceX + 2, pieceY - 2);
        Space left2ndSpace = board.getSpace(pieceX - 2, pieceY - 2);
        if (right1stSpace.getPiece() != null && right2ndSpace.isValid() && right1stSpace.getPiece().getColor().toString().equals("White")) {
          return true;
        } else if (left1stSpace.getPiece() != null && left2ndSpace.isValid()) {
          return true;
        }
      } else if (pieceX <= 1) {
        Space right1stSpace = board.getSpace(pieceX + 1, pieceY - 1);
        Space right2ndSpace = board.getSpace(pieceX + 2, pieceY - 2);
        if (right1stSpace.getPiece() != null && right2ndSpace.isValid()) {
          return true;
        }
      } else if (pieceX >= 6) {
        Space left1stSpace = board.getSpace(pieceX - 1, pieceY - 1);
        Space left2ndSpace = board.getSpace(pieceX - 2, pieceY - 2);
        if (left1stSpace.getPiece() != null && left2ndSpace.isValid()) {
          return true;
        }
      }
    } else{
      if (pieceX < 6 && pieceX > 1) {
        Space right1stSpace = board.getSpace(pieceX + 1, pieceY + 1);
        Space left1stSpace = board.getSpace(pieceX - 1, pieceY + 1);
        Space right2ndSpace = board.getSpace(pieceX + 2, pieceY + 2);
        Space left2ndSpace = board.getSpace(pieceX - 2, pieceY + 2);
        if (right1stSpace.getPiece() != null && right2ndSpace.isValid()) {
          return true;
        } else if (left1stSpace.getPiece() != null && left2ndSpace.isValid()) {
          return true;
        }
      } else if (pieceX <= 1) {
        Space right1stSpace = board.getSpace(pieceX + 1, pieceY + 1);
        Space right2ndSpace = board.getSpace(pieceX + 2, pieceY + 2);
        if (right1stSpace.getPiece() != null && right2ndSpace.isValid()) {
          return true;
        }
      } else if (pieceX >= 6) {
        Space left1stSpace = board.getSpace(pieceX - 1, pieceY + 1);
        Space left2ndSpace = board.getSpace(pieceX - 2, pieceY + 2);
        if (left1stSpace.getPiece() != null && left2ndSpace.isValid()) {
          return true;
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
    Space middle;
    if (player.getColor().equals("Red")) {
      current = board.getSpace(start.getRow(), start.getCell());
      goal = board.getSpace(target.getRow(), target.getCell());
    } else {
      current = board.getSpace(7 - start.getRow(), 7 - start.getCell());
      goal = board.getSpace(7 - target.getRow(), 7 - target.getCell());
    }
    if (canJump(player) && !pieceCanJump(current,player)){
      response.put(false, "Attempted to move when jump is possible.");
    } else if(canJump(player) && pieceCanJump(current,player)){
      board.addPieceToSpace(current.getPiece(), goal);
      current.unoccupy();
      middle = board.getSpace(Math.abs(current.getxCoordinate() - goal.getxCoordinate()),
              Math.abs(current.getCellIdx() - goal.getCellIdx()));
      middle.unoccupy();
      response.put(true, "This jump is valid.");
    }
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

      board.addPieceToSpace(current.getPiece(), goal);
      current.unoccupy();
      response.put(true, "This move is valid.");
    }
    return response;
  }
}
