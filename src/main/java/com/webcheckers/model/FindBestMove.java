package com.webcheckers.model;

import java.util.*;

/**
 * Class to find the best move for a player
 */
public class FindBestMove {

  //Static final (constant) variables
  private static final String INITIAL_STATE = "START";
  private static final String JUMP = "JUMP";
  private static final String MULTI_JUMP = "MULTI_JUMP";
  private static final String JUMP_NO_EAT = "JUMP_NO_EAT";
  private static final String JUMP_KING = "JUMP_KING";
  private static final String MOVE = "MOVE";
  private static final String MOVE_NO_EAT = "MOVE_NO_EAT";
  private static final String FINAL_STATE = "END";
  //The model board for the game
  private ModelBoard board;
  //The player looking for the best move
  private AbstractPlayer player;
  //Check move to ensure bestMoves are valid for the system
  private CheckMove checkMove;
  //The list of best bestMoves possible for the player to make
  private Map<Space, Space> bestMoves;
  //The state the game is currently in (for state machine)
  private String STATE;

  /**
   * Constructor for finding the best move possible in the system
   *
   * @param board the model board for the game being played
   * @param player the player looking for their best move
   */
  public FindBestMove(ModelBoard board, AbstractPlayer player) {
    this.board = board;
    this.player = player;
    this.checkMove = new CheckMove(board);
    this.STATE = INITIAL_STATE;
  }

  /**
   * Main state controller for finding the best move in the system given the different states the
   * move can be in
   *
   * @return the best move for the player to make
   */
  public Move findMove() {
    while (!this.STATE.equals(FINAL_STATE)) {
      switch (this.STATE) {
        case INITIAL_STATE:
          canJump();
          break;
        case JUMP:
          canJumpMulti();
          break;
        case MULTI_JUMP:
          canJumpAndEaten();
          break;
        case JUMP_NO_EAT:
          canJumpAndEaten();
        case JUMP_KING:
          canJumpAndKing();
          break;
        case MOVE:
          canMoveAndEaten();
          break;
        case MOVE_NO_EAT:
          canJumpAndKing();
          break;
      }
    }
    if (bestMoves.size() <= 0) {
      return null;
    }

    Random r = new Random();
    int random;
    if (bestMoves.size() == 1) {
      random = 0;
    } else {
      random = r.nextInt(bestMoves.size());
    }
    Set<Space> results = this.bestMoves.keySet();
    Object[] objectSpaces = results.toArray();
    Object objectSpace = objectSpaces[random];
    if (objectSpace instanceof Space) {
      try {
        Space bestMoveStart = (Space) objectSpace;
        Space bestMoveEnd = this.bestMoves.get(bestMoveStart);
        Position startPosition;
        Position endPositon;
        if (player.isRed()) {
          startPosition = new Position(bestMoveStart.getxCoordinate(),
              bestMoveStart.getCellIdx());
          endPositon = new Position(bestMoveEnd.getxCoordinate(),
              bestMoveEnd.getCellIdx());
        } else {
          startPosition = new Position(7 - bestMoveStart.getxCoordinate(),
              7 - bestMoveStart.getCellIdx());
          endPositon = new Position(7 - bestMoveEnd.getxCoordinate(),
              7 - bestMoveEnd.getCellIdx());
        }
        this.STATE = INITIAL_STATE;
        return new Move(startPosition, endPositon);
      } catch (ClassCastException e) {
        System.out.println(e);
      }
    }
    return null;
  }


  /**
   * Gathers all of the multi-jumps a player can make
   *
   * @param jumps the list of jumps (single or multi) that the player has made
   * @param piece the piece attempting to make the jump
   * @return a map of spaces containing all of the starting and ending spaces of a multi-jump
   */
  private Map<Space, Space> multiJump(Map<Space, Space> jumps, Piece piece) {
    for (Space startSpace : bestMoves.keySet()) {
      Position startPosition;
      Position endPosition;
      if (player.isRed()) {
        startPosition = new Position(piece.getXCoordinate(), piece.getCellIdx());
        endPosition = new Position(startSpace.getxCoordinate(), startSpace.getCellIdx());
      } else {
        startPosition = new Position(7 - piece.getXCoordinate(), 7 - piece.getCellIdx());
        endPosition = new Position(7 - startSpace.getxCoordinate(), 7 - startSpace.getCellIdx());
      }
      if (!startPosition.equals(endPosition)) {
        if (checkMove.validateMove(startPosition, endPosition, player).containsKey(true)) {
          jumps.put(piece.getSpace(), bestMoves.get(startSpace));
        }
      }
    }
    return jumps;
  }

  /**
   * Checks to see if a multi-jump can happen for the system.
   */
  private void canJumpMulti() {
    Map<Space, Space> multiJumps = new HashMap<>();
    if (player.isRed()) {
      for (Piece piece : board.getRedPieces()) {
        multiJumps = multiJump(multiJumps, piece);
      }
    }
    if (player.isWhite()) {
      for (Piece piece : board.getWhitePieces()) {
        multiJumps = multiJump(multiJumps, piece);
      }
    }
    if (multiJumps.size() != 0) {
      this.bestMoves = multiJumps;
      this.STATE = MULTI_JUMP;
    } else {
      this.STATE = JUMP_NO_EAT;
    }
  }

  /**
   * Checks to see if a piece will become a king at the end of the turn
   *
   * @return the map of starting and ending spaces where the move results in a king
   */
  private Map<Space, Space> willBecomeKing() {
    Map<Space, Space> kingJumps = new HashMap<>();
    for (Space startSpace : this.bestMoves.keySet()) {
      Space endSpace = this.bestMoves.get(startSpace);
      if (player.isRed()) {
        if (endSpace.getxCoordinate() == 0 && !startSpace.getPiece().isKing()) {
          kingJumps.put(startSpace, endSpace);
        }
      } else {
        if (endSpace.getxCoordinate() == 7 && !startSpace.getPiece().isKing()) {
          kingJumps.put(startSpace, endSpace);
        }
      }
    }
    return kingJumps;
  }

  /**
   * Checks to see all of the moves that result in a king happening
   */
  private void canJumpAndKing() {
    Map<Space, Space> kingJumps = willBecomeKing();
    if (kingJumps.size() != 0) {
      this.bestMoves = kingJumps;
    }
    this.STATE = FINAL_STATE;
  }

  /**
   * Finds all of the moves for a given player in the system
   */
  private void findMoves() {
    Map<Space, Space> moves = new HashMap<>();
    if (player.isRed()) {
      for (Piece redPiece : board.getRedPieces()) {
        Position start = new Position(redPiece.getXCoordinate(), redPiece.getCellIdx());
        Position rightUpper = new Position(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() + 1);
        Position leftUpper = new Position(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() - 1);
        Position rightLower = new Position(redPiece.getXCoordinate() + 1, redPiece.getCellIdx() + 1);
        Position leftLower = new Position(redPiece.getXCoordinate() + 1, redPiece.getCellIdx() - 1);
        if (checkMove.validateMove(start, rightUpper, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() - 1,
              redPiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, leftUpper, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() - 1,
              redPiece.getCellIdx() - 1));
        }
        if (checkMove.validateMove(start, rightLower, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() + 1,
                  redPiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, leftLower, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() + 1,
                  redPiece.getCellIdx() - 1));
        }
      }
    } else {
      for (Piece whitePiece : board.getWhitePieces()) {
        Position start = new Position(7 - whitePiece.getXCoordinate(), 7 - whitePiece.getCellIdx());
        Position rightUpper = new Position(7 - (whitePiece.getXCoordinate() + 1), 7 - (whitePiece.getCellIdx() + 1));
        Position leftUpper = new Position(7 - (whitePiece.getXCoordinate() + 1), 7 - (whitePiece.getCellIdx() - 1));
        Position rightLower = new Position(7 - (whitePiece.getXCoordinate() - 1), 7 - (whitePiece.getCellIdx() + 1));
        Position leftLower = new Position(7 - (whitePiece.getXCoordinate() - 1), 7 - (whitePiece.getCellIdx() - 1));
        if (checkMove.validateMove(start, rightUpper, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() + 1,
              whitePiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, leftUpper, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() + 1,
              whitePiece.getCellIdx() - 1));
        }
        if (checkMove.validateMove(start, rightLower, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() - 1,
                  whitePiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, leftLower, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() - 1,
                  whitePiece.getCellIdx() - 1));
        }
      }
    }
    this.bestMoves = moves;
  }

  /**
   * Checks to see if a piece is able to complete a jump or not
   */
  private void canJump() {
    Map<Space, Space> jumps = checkMove.findJumps(player);
    if (jumps.size() == 0) {
      findMoves();
      this.STATE = MOVE;
    } else {
      this.bestMoves = jumps;
      this.STATE = JUMP;
    }
  }

  /**
   * Checks to see if a piece is removed by a jump. Used to ensure a future jump can or cannot
   * happen over a piece
   *
   * @param startingSpace the space the move starts at
   * @param endingSpace the space the move ends at
   * @param potentialJumped the space containing the piece potentially jumped by the move
   * @param piece the piece making the move
   * @return true/false based on if the piece was jumped by the move or not
   */
  private boolean jumped(Space startingSpace, Space endingSpace, Space potentialJumped,
      Piece piece) {
    if (Math.abs(endingSpace.getxCoordinate() - startingSpace.getxCoordinate()) % 2 == 0) {
      if (Math.abs(endingSpace.getCellIdx() - startingSpace.getCellIdx()) % 2 == 0) {
        if (endingSpace.getxCoordinate() + 1 == potentialJumped.getxCoordinate() ||
            endingSpace.getxCoordinate() - 1 == potentialJumped.getxCoordinate()) {
          if (endingSpace.getCellIdx() - 1 == potentialJumped.getCellIdx() ||
              endingSpace.getCellIdx() + 1 == potentialJumped.getCellIdx()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks to see if a piece will be eaten after completing a move
   *
   * @param startingSpace the space the piece starts at
   * @param endingSpace the space the piece would end up at
   * @return true/false if a piece is eaten or not
   */
  boolean willBeEaten(Space startingSpace, Space endingSpace) {
    int xCoordinate = endingSpace.getxCoordinate();
    int cellIdx = endingSpace.getCellIdx();

    //Checking to see if there is a piece to the upper right of the ending space and, if so, it can jump the moving piece
    if (xCoordinate - 1 >= 0 && cellIdx + 1 <= 7) {
      Space upperRight = board.getSpace(xCoordinate - 1, cellIdx + 1);
      if (upperRight.isOccupied()) {
        if (player.isRed()) {
          if (upperRight.pieceIsWhite()) {
            if (xCoordinate + 1 <= 7 && cellIdx - 1 >= 0) {
              Space bottomLeft = board.getSpace(xCoordinate + 1, cellIdx - 1);
              if (!bottomLeft.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, bottomLeft, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        } else {
          if (upperRight.pieceIsRed() && upperRight.getPiece().isKing()) {
            if (xCoordinate + 1 <= 7 && cellIdx - 1 >= 0) {
              Space bottomLeft = board.getSpace(xCoordinate + 1, cellIdx - 1);
              if (!bottomLeft.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, upperRight, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        }
      }
    }

    //Checking to see if there is a piece to the upper left of the ending space and, if so, it can jump the moving piece
    if (xCoordinate - 1 >= 0 && cellIdx - 1 >= 0) {
      Space upperLeft = board.getSpace(xCoordinate - 1, cellIdx - 1);
      if (upperLeft.isOccupied()) {
        if (player.isRed()) {
          if (upperLeft.pieceIsWhite()) {
            if (xCoordinate + 1 <= 7 && cellIdx + 1 <= 7) {
              Space bottomRight = board.getSpace(xCoordinate + 1, cellIdx + 1);
              if (!bottomRight.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, bottomRight, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        } else {
          if (upperLeft.pieceIsRed() && upperLeft.getPiece().isKing()) {
            if (xCoordinate + 1 <= 7 && cellIdx + 1 <= 7) {
              Space bottomRight = board.getSpace(xCoordinate + 1, cellIdx + 1);
              if (!bottomRight.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, upperLeft, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        }
      }
    }

    //Checking to see if there is a piece to the bottom right of the ending space and, if so, it can jump the moving piece
    if (xCoordinate + 1 <= 7 && cellIdx + 1 <= 7) {
      Space bottomRight = board.getSpace(xCoordinate + 1, cellIdx + 1);
      if (bottomRight.isOccupied()) {
        if (player.isRed()) {
          if (bottomRight.pieceIsWhite() && bottomRight.getPiece().isKing()) {
            if (xCoordinate - 1 >= 0 && cellIdx - 1 >= 0) {
              Space upperLeft = board.getSpace(xCoordinate - 1, cellIdx - 1);
              if (!upperLeft.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, bottomRight, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        } else {
          if (bottomRight.pieceIsRed()) {
            if (xCoordinate - 1 >= 0 && cellIdx - 1 >= 0) {
              Space upperLeft = board.getSpace(xCoordinate - 1, cellIdx - 1);
              if (!upperLeft.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, upperLeft, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        }
      }
    }

    //Checking to see if there is a piece to the bottom left of the ending space and, if so, it can jump the moving piece
    if (xCoordinate + 1 <= 7 && cellIdx - 1 >= 0) {
      Space bottomLeft = board.getSpace(xCoordinate + 1, cellIdx - 1);
      if (bottomLeft.isOccupied()) {
        if (player.isRed()) {
          if (bottomLeft.pieceIsWhite() && bottomLeft.getPiece().isKing()) {
            if (xCoordinate - 1 >= 0 && cellIdx + 1 <= 7) {
              Space upperRight = board.getSpace(xCoordinate - 1, cellIdx + 1);
              if (!upperRight.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, bottomLeft, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        } else {
          if (bottomLeft.pieceIsRed()) {
            if (xCoordinate - 1 >= 0 && cellIdx + 1 <= 7) {
              Space upperRight = board.getSpace(xCoordinate - 1, cellIdx + 1);
              if (!upperRight.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, upperRight, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks to see if there are any pieces that wont be eaten as a result of a move
   *
   * @return the map of all of the pieces not eaten as a result of the move they are attempting to
   * make
   */
  private Map<Space, Space> wontBeEaten() {
    Map<Space, Space> nonEaten = new HashMap<>();
    for (Space startSpace : this.bestMoves.keySet()) {
      if (!willBeEaten(startSpace, bestMoves.get(startSpace))) {
        nonEaten.put(startSpace, this.bestMoves.get(startSpace));
      }
    }
    return nonEaten;
  }

  /**
   * Checks to see if a jump happens and if the result ends in the piece moved being eaten by
   * another piece
   */
  private void canJumpAndEaten() {
    Map<Space, Space> nonEaten = wontBeEaten();
    if (nonEaten.size() > 1) {
      this.bestMoves = nonEaten;
      this.STATE = JUMP_KING;
    } else {
      if (nonEaten.size() != 0) {
        this.bestMoves = nonEaten;
      }
      this.STATE = FINAL_STATE;
    }
  }

  /**
   * Checks to see if a move happens and if the result ends in the piece moved being eaten by
   * another piece
   */
  private void canMoveAndEaten() {
    Map<Space, Space> nonEaten = wontBeEaten();
    if (nonEaten.size() != 0) {
      this.bestMoves = nonEaten;
      this.STATE = MOVE_NO_EAT;
    } else {
      this.STATE = FINAL_STATE;
    }
  }

  /**
   * Finds all of the potential jumps a player can make given a starting position
   *
   * @param start the position the jump starts at
   * @param player the player attempting to make the jump
   * @return a list of all of the ending positions of the jump
   */
  private List<Position> findAllJumps(Position start, AbstractPlayer player) {
    List<Position> moves = new ArrayList<>();
    if (player.isWhite()) {
      start = new Position(7 - start.getRow(), 7 - start.getCell());
    }
    Position upperRight = new Position(start.getRow() - 2, start.getCell() - 2);
    Position upperLeft = new Position(start.getRow() - 2, start.getCell() + 2);
    Position lowerRight = new Position(start.getRow() + 2, start.getCell() - 2);
    Position lowerLeft = new Position(start.getRow() + 2, start.getCell() + 2);
    if (checkMove.validateMove(start, upperRight, player).containsKey(true)) {
      if (player.isWhite()) {
        moves.add(new Position(7 - upperRight.getRow(), 7 - upperRight.getCell()));
      } else {
        moves.add(upperRight);
      }
    }
    if (checkMove.validateMove(start, upperLeft, player).containsKey(true)) {
      if (player.isWhite()) {
        moves.add(new Position(7 - upperLeft.getRow(), 7 - upperLeft.getCell()));
      } else {
        moves.add(upperLeft);
      }
    }
    if (checkMove.validateMove(start, lowerRight, player).containsKey(true)) {
      if (player.isWhite()) {
        moves.add(new Position(7 - lowerRight.getRow(), 7 - lowerRight.getCell()));
      } else {
        moves.add(lowerRight);
      }
    }
    if (checkMove.validateMove(start, lowerLeft, player).containsKey(true)) {
      if (player.isWhite()) {
        moves.add(new Position(7 - lowerLeft.getRow(), 7 - lowerLeft.getCell()));
      } else {
        moves.add(lowerLeft);
      }
    }
    return moves;
  }

  /**
   * Finds the path to the end of a multi-jump that a player is making
   *
   * @param start the starting position of the multi-jump
   * @param end the ending position of the multi-jump
   * @param player the player making the multi-jump
   * @return a map of all of the positions a jump moves to as the key and the list of positions that move can move to
   */
  private Map<Position, List<Position>> findEnd(Position start, Position end, AbstractPlayer player) {
    Map<Position, List<Position>> moves = new HashMap<>();
    List<Position> endingJumps = findAllJumps(start, player);
    moves.put(start, endingJumps);
    while(!moves.containsKey(end)) {
      List<Position> newEndJumps = new ArrayList<>();
      boolean found = false;
      for (Position endOfJump : endingJumps) {
        if (endOfJump.equals(end)) {
          moves.put(endOfJump, null);
          found = true;
          break;
        }
        //Attempting to find, for the current position in the loop, if there are more jumps possible
        Space startingSpace = board.getSpace(start.getRow(), start.getCell());
        Space movingSpace = board.getSpace(endOfJump.getRow(), endOfJump.getCell());
        board.addPieceToSpace(startingSpace.getPiece(), movingSpace);
        List<Position> potentialJumps = findAllJumps(endOfJump, player);
        board.removePieceFromSpace(startingSpace, movingSpace);
        if (potentialJumps.size() != 0) {
          newEndJumps.addAll(potentialJumps);
          moves.put(endOfJump, potentialJumps);
        }
      }
      if (found) {
        break;
      }
      endingJumps = newEndJumps;
      if (newEndJumps.size() == 0) {
        break;
      }
    }
    return moves;
  }

  /**
   * Using a backtracking system, finds the start of a multi-jump. This finds the complete path for a multi-jump
   *
   * @param positions the map of positions visited during a jump along with a list of positions the key position can move to
   * @param start the start position of the multi-jump
   * @param end the end position of the multi-jump
   * @return the list of positions a player visits during a mulit-jump (unordered)
   */
  private List<Position> findStart(Map<Position, List<Position>> positions, Position start, Position end) {
    List<Position> moves = new ArrayList<>();
    moves.add(end);
    Position current = end;
    while (current != start) {
      for (Position pos: positions.keySet()) {
        boolean found = false;
        if (positions.get(pos) != null) {
          for (Position position : positions.get(pos)) {
            if (position.equals(current)) {
              current = pos;
              found = true;
              break;
            }
          }
        }
        if (found) {
          moves.add(pos);
          positions.remove(pos);
          break;
        }
      }
    }
    return moves;
  }

  /**
   * System for finding the complete path of a multi-jump
   *
   * @param start the starting position of the multi-jump
   * @param end the ending position of the multi-jump
   * @return a ordered list of all of the positions a piece visits during a multi-jump
   */
  public List<Position> findMiddle(Space start, Space end) {
    Position startingPosition = new Position(start.getxCoordinate(), start.getCellIdx());
    Position endingPosition = new Position(end.getxCoordinate(), end.getCellIdx());
    Map<Position, List<Position>> foundEnd = findEnd(startingPosition, endingPosition, player);
    if (foundEnd.size() == 1) {
      return null;
    }
    List<Position> foundStart = findStart(foundEnd, startingPosition, endingPosition);
    PositionComparator comparator = new PositionComparator(startingPosition, endingPosition);
    foundStart.sort(comparator);
    return foundStart;
  }
}