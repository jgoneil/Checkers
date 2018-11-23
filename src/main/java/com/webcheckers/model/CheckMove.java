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
  private Piece piece;

  //Static (constant) variables for response
  private static final String jumpPossible = "Attempted to move when jump is possible.";
  public static final String multiJumpPossible = "Attempting to jump a single piece when a multi-jump is possible.";

  /**
   * Constructor for the CheckMove Class
   *
   * @param board the board for the model (2-D Array of Spaces)
   */
  public CheckMove(ModelBoard board) {
    this.board = board;
  }

  /**
   * Checks to see if a player still has moves possible to make
   *
   * @param player the player checking to see if there are moves possible
   * @return true/false if a player does/doesn't have any moves left to make
   */
  public boolean moveAvailable(Player player) {
    if (this.board.checkPendingMove()) {
      return true;
    }
    if (player.isRed()) {
      for (Piece piece : board.getRedPieces()) {
        int row = piece.getXCoordinate();
        int col = piece.getCellIdx();
        Position current = new Position(row, col);
        //Checking to see if the piece can move to the left or right forwards
        Map<Boolean, String> moveUpperLeft = validateMove(current, new Position(row - 1, col - 1), player);
        Map<Boolean, String> moveUpperRight = validateMove(current, new Position(row - 1, col + 1), player);
        if (moveUpperLeft.containsKey(true) || moveUpperRight.containsKey(true)) {
          return true;
        }
        //If they can't move to the left or right, checking for a potential jump
        if (moveUpperLeft.containsValue(jumpPossible) || moveUpperRight.containsValue(jumpPossible)) {
          return true;
        } else if (moveUpperLeft.containsValue(multiJumpPossible) || moveUpperRight.containsValue(multiJumpPossible)) {
          return true;
        }
        if (piece.isKing()) {
          //If the piece is a king, checking to see if it can move left or right backwards
          Map<Boolean, String> moveLowerLeft = validateMove(current, new Position(row + 1, col - 1), player);
          Map<Boolean, String> moveLowerRight = validateMove(current, new Position(row + 1, col + 1), player);
          if (moveLowerLeft.containsKey(true) || moveLowerRight.containsKey(true)) {
            return true;
          }
        }
      }
    } else {
      for (Piece piece : board.getWhitePieces()) {
        int row = piece.getXCoordinate();
        int col = piece.getCellIdx();
        Position current = new Position(7 - row, 7 - col);
        //Checking to see if the piece can move to the left or right forwards
        Map<Boolean, String> moveUpperLeft = validateMove(current, new Position(7 -
                (row + 1), 7 - (col - 1)), player);
        Map<Boolean, String> moveUpperRight = validateMove(current, new Position(7 -
                (row + 1), 7 - (col + 1)), player);
        if (moveUpperLeft.containsKey(true) || moveUpperRight.containsKey(true)) {
          return true;
        }
        //If they can't move to the left or right, checking for a potential jump
        if (moveUpperLeft.containsValue(jumpPossible) || moveUpperRight.containsValue(jumpPossible)) {
          return true;
        } else if (moveUpperLeft.containsValue(multiJumpPossible) || moveUpperRight.containsValue(multiJumpPossible)) {
          return true;
        }
        if (piece.isKing()) {
          //If the piece is a king, checking to see if it can move left or right backwards
          Map<Boolean, String> moveLowerLeft = validateMove(current, new Position(7 -
                  (row - 1), 7 - (col - 1)), player);
          Map<Boolean, String> moveLowerRight = validateMove(current, new Position(7 -
                  (row - 1), 7 - (col + 1)), player);
          if (moveLowerLeft.containsKey(true) || moveLowerRight.containsKey(true)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks if the piece is moving diagonally
   *
   * @param start the starting position of the checker
   * @param end   the ending position of the checker
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
   * @param end   the ending position of the checker
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
   * @param end   the ending position of the checker
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
   * @param end   the ending position of the checker
   * @return true/false based on whether checker is moving forward
   */
  private boolean isMovingForward(Position start, Position end) {
    return end.getRow() < start.getRow();
  }

  /**
   * Checks to see if a piece that is a king can jump
   *
   * @param piece the piece attempting to make a jump
   * @param player the player attempting to make a jump
   * @return a map of moves that a king piece can make
   */
  private Map<Space, Space> kingCanJump(Piece piece, Player player) {
    Map<Space, Space> kingJumps = new HashMap<>();

    if (player.isRed() && piece.isKing()) {
      if (piece.getSpace().getxCoordinate() + 2 <= 7
              && piece.getSpace().getCellIdx() - 2 >= 0) {
        Space bottomLeft = board.getSpace(piece.getSpace().getxCoordinate() + 2,
                piece.getSpace().getCellIdx() - 2);
        if (pieceCanJump(piece.getSpace(), bottomLeft, player, piece)) {
          kingJumps.put(bottomLeft, piece.getSpace());
        }
      }
      if (piece.getSpace().getxCoordinate() + 2 <= 7
              && piece.getSpace().getCellIdx() + 2 <= 7) {
        Space bottomRight = board.getSpace(piece.getSpace().getxCoordinate() + 2,
                piece.getSpace().getCellIdx() + 2);
        if (pieceCanJump(piece.getSpace(), bottomRight, player, piece)) {
          kingJumps.put(bottomRight, piece.getSpace());
        }
      }
    } else if (player.isWhite() && piece.isKing()){
      if (piece.getSpace().getxCoordinate() - 2 >= 0
              && piece.getSpace().getCellIdx() + 2 <= 7) {
        Space bottomLeft = board.getSpace(piece.getSpace().getxCoordinate() - 2,
                piece.getSpace().getCellIdx() + 2);
        if (pieceCanJump(piece.getSpace(), bottomLeft, player, piece)) {
          kingJumps.put(bottomLeft, piece.getSpace());
        }
      }
      if (piece.getSpace().getxCoordinate() - 2 >= 0
              && piece.getSpace().getCellIdx() - 2 >= 0) {
        Space bottomRight = board.getSpace(piece.getSpace().getxCoordinate() - 2,
                piece.getSpace().getCellIdx() - 2);
        if (pieceCanJump(piece.getSpace(), bottomRight, player, piece)) {
          kingJumps.put(bottomRight, piece.getSpace());
        }
      }
    }
    return kingJumps;
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
          if (pieceCanJump(redPiece.getSpace(), upperLeft, player, redPiece)) {
            jumps.put(redPiece.getSpace(), upperLeft);
          }
        }
        //checking to see if a piece is within the bounds of the board (that it is not off the right or top sides)
        if (redPiece.getXCoordinate() - 2 >= 0
                && redPiece.getCellIdx() + 2 <= 7) {
          Space upperRight = board.getSpace(redPiece.getXCoordinate() - 2,
                  redPiece.getCellIdx() + 2);
          //checking to see if that specific piece can jump another piece
          if (pieceCanJump(redPiece.getSpace(), upperRight, player, redPiece)) {
            jumps.put(redPiece.getSpace(), upperRight);
          }
        }
        if (redPiece.isKing()) {
          Map<Space, Space> kingJumps = kingCanJump(redPiece, player);
          if (kingJumps.size() != 0) {
            for (Space end : kingJumps.keySet()) {
              jumps.put(redPiece.getSpace(), end);
            }
          }
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

          if (pieceCanJump(whitePiece.getSpace(), upperLeft, player, whitePiece)) {
            jumps.put(whitePiece.getSpace(), upperLeft);
          }
        }
        //checking to see if a piece is within the bounds of the board (that it is not off the left or bottom sides)
        if (whitePiece.getXCoordinate() + 2 <= 7
                && whitePiece.getCellIdx() - 2 >= 0) {
          Space upperRight = board.getSpace(whitePiece.getXCoordinate() + 2,
                  whitePiece.getCellIdx() - 2);
          //checking to see if that specific piece can jump another piece
          if (pieceCanJump(whitePiece.getSpace(), upperRight, player, whitePiece)) {
            jumps.put(whitePiece.getSpace(), upperRight);
          }
        }
        if (whitePiece.isKing()) {
          Map<Space, Space> kingJumps = kingCanJump(whitePiece, player);
          if (kingJumps.size() != 0) {
            for (Space end : kingJumps.keySet()) {
              jumps.put(whitePiece.getSpace(), end);
            }
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
      Map<Space, Space> temp = canJump(player);
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
              Map<Space, Space> tempHold = new HashMap<>();
              for (Space keySpace : jumps.keySet()) {
                if (!jumps.get(keySpace).equals(startingSpace)) {
                  tempHold.put(keySpace, jumps.get(keySpace));
                }
              }
              jumps = tempHold;
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
  private boolean pieceCanJump(Space start, Space end, Player player, Piece piece) {
    Space middle = board.getSpace((start.getxCoordinate() + end.getxCoordinate()) / 2,
            (start.getCellIdx() + end.getCellIdx()) / 2);

    if (player.isWhite()) {
      if (start.getxCoordinate() - end.getxCoordinate() > 0) {
        if (!piece.isKing()) {
          return false;
        }
      }
    }

    if (player.isRed()) {
      if (start.getxCoordinate() - end.getxCoordinate() < 0) {
        if (!piece.isKing()) {
          return false;
        }
      }
    }

    if (isMovingTwo(start, end)) {
      if (!end.isOccupied()) {
        if (middle.isOccupied()) {
          if (player.isRed() && middle.pieceIsWhite()) {
            return true;
          }
          if (player.isWhite() && middle.pieceIsRed()) {
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
   * @param start  - space currently at
   * @param target - target space to move to
   * @return - validity of move to target space
   */
  public Map<Boolean, String> validateMove(Position start, Position target, Player player) {
    Map<Boolean, String> response = new HashMap<>();
    Space current;
    Space goal;
    if (target.getRow() < 0 || target.getRow() > 7) {
      response.put(false, "Move out of bounds");
      return response;
    }
    if (target.getCell() < 0 || target.getCell() > 7) {
      response.put(false, "Move out of bounds");
      return response;
    }

    if (player.isRed()) {
      //Sets the current and goal spaces for the potential move
      current = board.getSpace(start.getRow(), start.getCell());
      goal = board.getSpace(target.getRow(), target.getCell());
    } else {
      //Sets the current and goal spaces for the potential move (flipping since it is the white player)
      current = board.getSpace(7 - start.getRow(), 7 - start.getCell());
      goal = board.getSpace(7 - target.getRow(), 7 - target.getCell());
    }

    if (this.piece == null || this.piece != current.getPiece()) {
      if (current.getPiece() != null) {
        this.piece = current.getPiece();
      }
    }

    Map<Space, Space> validJumps = findJumps(player);
    //Checking to see if a player can preform a jump
    if (validJumps.size() != 0) {
      //Checking to see if the current piece is preforming a jump
      if (pieceCanJump(current, goal, player, this.piece)) {
        board.isJumping(true);
        if (validJumps.containsKey(current)) {
          board.setSubmit(true);
          response.put(true, "This jump is valid.");
        } else {
          response.put(true, multiJumpPossible);
          board.setSubmit(false);
        }
      } else {
        response.put(false, jumpPossible);
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
      } else if (!isMovingForward(start, target)) {
        response.put(false, "Single pieces can only move forward.");
      } else if (board.checkPendingMove()) {
          response.put(false, "Already made a move");
      } else {
        board.setSubmit(true);
        response.put(true, "This move is valid.");
      }
    }
    return response;
  }
}
