package com.webcheckers.model;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

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

  private boolean kingCanJump(Piece piece, Player player) {
    if (player.isRed()) {
      if (piece.getSpace().getxCoordinate() + 2 <= 7
              && piece.getSpace().getCellIdx() - 2 >= 0) {
        Space bottomLeft = board.getSpace(piece.getSpace().getxCoordinate() + 2,
                piece.getSpace().getCellIdx() - 2);
        return pieceCanJump(piece.getSpace(), bottomLeft);
      }
      if (piece.getSpace().getxCoordinate() + 2 <= 7
              && piece.getSpace().getCellIdx() + 2 <= 7) {
        Space bottomRight = board.getSpace(piece.getSpace().getxCoordinate() + 2,
                piece.getSpace().getCellIdx() + 2);
        return pieceCanJump(piece.getSpace(), bottomRight);
      }
    } else {
      if (piece.getSpace().getxCoordinate() - 2 >= 0
              && piece.getSpace().getCellIdx() + 2 <= 7) {
        Space bottomLeft = board.getSpace(piece.getSpace().getxCoordinate() - 2,
                piece.getSpace().getCellIdx() + 2);
        return pieceCanJump(piece.getSpace(), bottomLeft);
      }
      if (piece.getSpace().getxCoordinate() - 2 >= 0
              && piece.getSpace().getCellIdx() - 2 >= 0) {
        Space bottomRight = board.getSpace(piece.getSpace().getxCoordinate() - 2,
                piece.getSpace().getCellIdx() - 2);
        return pieceCanJump(piece.getSpace(), bottomRight);
      }
    }
    return false;
  }

  /**
   * Check to see if any piece during the players turn can jump.
   *
   * @return a map of all of the spaces (last position) that can jump for the given board
   */
  private Map<Space, Space> canJump(Player player) {
    Map<Space, Space> jumps = new HashMap<>();
    //Checking to see if a player is red or not
    if (player.isRed()) {
      for (Piece redPiece : board.getRedPieces()) {
        //checking to see if a piece is within the bounds of the board (that it is not off the left or top sides)
        if (redPiece.getXCoordinate() - 2 >= 0
            && redPiece.getCellIdx() - 2 >= 0) {
          Space upperLeft = board.getSpace(redPiece.getXCoordinate() - 2,
              redPiece.getCellIdx() - 2);
          //checking to see if that specific piece can jump another piece
          if (pieceCanJump(redPiece.getSpace(), upperLeft)) {
            jumps.put(redPiece.getSpace(), upperLeft);
          }
        }
        //checking to see if a piece is within the bounds of the board (that it is not off the right or top sides)
        if (redPiece.getXCoordinate() -2 >=0
            && redPiece.getCellIdx() + 2 <= 7) {
          Space upperRight = board.getSpace(redPiece.getXCoordinate() - 2,
              redPiece.getCellIdx() + 2);
          //checking to see if that specific piece can jump another piece
          if (pieceCanJump(redPiece.getSpace(), upperRight)) {
            jumps.put(redPiece.getSpace(), upperRight);
          }
        }
        if (kingCanJump(redPiece, player)) {
          //jumps.put(redPiece.getSpace(), )
        }
      }
    } else {
      for (Piece whitePiece : board.getWhitePieces()) {
        //checking to see if a piece is within the bounds of the board (that it is not off the right or bottom sides)
        if (whitePiece.getXCoordinate() + 2 <= 7
            && whitePiece.getCellIdx() + 2 <= 7) {
          Space upperLeft = board.getSpace(whitePiece.getXCoordinate() + 2,
              whitePiece.getCellIdx() + 2);
          //checking to see if that specific piece can jump another piece
          if (pieceCanJump(whitePiece.getSpace(), upperLeft)) {
            jumps.put(whitePiece.getSpace(), upperLeft);
          }
        }
        //checking to see if a piece is within the bounds of the board (that it is not off the left or bottom sides)
        if (whitePiece.getXCoordinate() + 2 <= 7
            && whitePiece.getCellIdx() - 2 >= 0) {
          Space upperRight = board.getSpace(whitePiece.getXCoordinate() + 2,
              whitePiece.getCellIdx() - 2);
          //checking to see if that specific piece can jump another piece
          if (pieceCanJump(whitePiece.getSpace(), upperRight)) {
            jumps.put(whitePiece.getSpace(), upperRight);
          }
        }
      }
    }
    return jumps;
  }

  /**
   * Finds all of the potential jumps on the board to see if any mulit jumps can happen
   *
   * @param player the player attempting to make a move
   * @return a map containing the last move for all of the starting and ending spaces of all of the potential jumps
   */
  public Map<Space, Space> findJumps(Player player) {
    Map<Space, Space> jumps = new HashMap<>();
    Stack<Move> movesMade = new Stack<>();
    boolean canContinueJumping = true;
    while (canContinueJumping) {
      Map<Space, Space> temp  = canJump(player);
      if (temp.size() == 0) {
        canContinueJumping = false;
      } else {
        for (Space startingSpace : temp.keySet()) {
          Space endingSpace = temp.get(startingSpace);
          Move move = new Move(new Position(startingSpace.getxCoordinate(), startingSpace.getCellIdx()),
                  new Position(endingSpace.getxCoordinate(), endingSpace.getCellIdx()));
          movesMade.push(move);
          board.addPieceToSpace(startingSpace.getPiece(), endingSpace);
          if (jumps.size() == 0) {
            jumps.put(startingSpace, endingSpace);
          } else {
            if (jumps.containsValue(startingSpace)) {
              for (Space keySpace: jumps.keySet()) {
                if (jumps.get(keySpace).equals(startingSpace)) {
                  jumps.remove(keySpace);
                  break;
                }
              }
            }
            jumps.put(startingSpace, endingSpace);
          }
        }
      }
    }
    while (movesMade.size() != 0) {
      Move revertMove = movesMade.pop();
      Space startingSpace = board.getSpace(revertMove.getStartRow(), revertMove.getStartCell());
      Space endingSpace = board.getSpace(revertMove.getEndRow(), revertMove.getEndCell());
      board.removePieceFromSpace(startingSpace, endingSpace);
    }
    return jumps;
  }

  /**
   * Checks to see a single piece can jump.
   *
   * @param start Starting space that contains the piece
   * @param end   The end space that the piece could possibly jump to
   * @return true/false based on if the piece can jump or not.
   */
  private boolean pieceCanJump(Space start, Space end) {
    Space middle = board.getSpace((start.getxCoordinate() + end.getxCoordinate()) / 2,
        (start.getCellIdx() + end.getCellIdx()) / 2);

    if (isMovingTwo(start, end)) {
      if (!end.isOccupied()) {
        if (middle.isOccupied()) {
          if (board.checkRedTurn() && middle.pieceIsWhite()) {
            return true;
          }

          if (!board.checkRedTurn() && middle.pieceIsRed()) {
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
    if (player.isRed()) {
      //Sets the current and goal spaces for the potential move
      current = board.getSpace(start.getRow(), start.getCell());
      goal = board.getSpace(target.getRow(), target.getCell());
    } else {
      //Sets the current and goal spaces for the potential move (flipping since it is the white player)
      current = board.getSpace(7 - start.getRow(), 7 - start.getCell());
      goal = board.getSpace(7 - target.getRow(), 7 - target.getCell());
    }
    Map<Space, Space> validJumps = findJumps(player);
    //Checking to see if a player can preform a jump
    if (validJumps.size() != 0) {
      //Checking to see if the current piece is preforming a jump
      if (pieceCanJump(current, goal)) {
        board.isJumping(true);
        if (validJumps.containsKey(current)) {
          board.setSubmit(true);
          response.put(true, "This jump is valid.");
        } else {
          response.put(true, "Attempting to jump a single piece when a multi-jump is possible.");
        }
      } else {
        response.put(false, "Attempted to move when jump is possible.");
      }
    } else {
      //Checking to see if the goal space is a white space.
      if (goal.isWhite()) {
        response.put(false, "Attempted to move a piece to a white space.");
      } else if (goal.isOccupied()) {
        response.put(false, "Attempted to move a piece to an already occupied space");
      } else if (!isMovingOne(start, target)) {
        response.put(false, "Attempted to move piece too far.");
      } else if (!isMovingDiagonal(start, target)) {
        response.put(false, "Pieces can only move diagonally.");
      } else if (!isMovingForward(start, target) && current.isPieceKing()) {
        response.put(true, "This is a valid move for a King.");
        board.setSubmit(true);
      } else if (!isMovingForward(start, target)){
        response.put(false, "Single pieces can only move forward.");
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
          board.setSubmit(true);
          response.put(true, "This move is valid.");
        }
      }
    }
    return response;
  }
}
