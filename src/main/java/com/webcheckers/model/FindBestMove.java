package com.webcheckers.model;

import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.util.HashMap;

public class FindBestMove {

  private ModelBoard board;
  private Player player;
  private CheckMove checkMove;
  private Map<Space, Space> moves;
  private String STATE;

  private static final String INITIAL_STATE = "START";
  private static final String JUMP = "JUMP";
  private static final String MULTI_JUMP = "MULTI_JUMP";
  private static final String JUMP_NO_EAT = "JUMP_NO_EAT";
  private static final String JUMP_KING = "JUMP_KING";
  private static final String MOVE = "MOVE";
  private static final String MOVE_NO_EAT = "MOVE_NO_EAT";
  private static final String FINAL_STATE = "END";

  public FindBestMove(ModelBoard board, Player player) {
    this.board = board;
    this.player = player;
    this.checkMove = new CheckMove(board);
    this.STATE = INITIAL_STATE;
  }

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
    if (moves.size() <= 0) {
      return null;
    }

    Random r = new Random();
    int random;
    if (moves.size() == 1) {
      random = 0;
    } else {
      random = r.nextInt(moves.size());
    }
    Set<Space> results = this.moves.keySet();
    Object[] objectSpaces = results.toArray();
    Object objectSpace = objectSpaces[random];
    if (objectSpace instanceof Space) {
      try {
        Space bestMoveStart = (Space) objectSpace;
        Space bestMoveEnd = this.moves.get(bestMoveStart);
        Position startPosition = new Position(bestMoveStart.getxCoordinate(),
                bestMoveStart.getCellIdx());
        Position endPositon = new Position(bestMoveEnd.getxCoordinate(),
                bestMoveEnd.getCellIdx());
        return new Move(startPosition, endPositon);
      } catch (ClassCastException e) {
        System.out.println(e);
      }
    }
    return null;
  }

  private Map<Space, Space> multiJump(Map<Space, Space> jumps, Piece piece) {
    for (Space startSpace : moves.keySet()) {
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
          jumps.put(piece.getSpace(), moves.get(startSpace));
        }
      }
    }
    return jumps;
  }

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
      this.moves = multiJumps;
      this.STATE = MULTI_JUMP;
    } else {
      this.STATE = JUMP_NO_EAT;
    }
  }

  private Map<Space, Space> willBecomeKing() {
    Map<Space, Space> kingJumps = new HashMap<>();
    for (Space startSpace : this.moves.keySet()) {
      Space endSpace = this.moves.get(startSpace);
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

  private void canJumpAndKing() {
    Map<Space, Space> kingJumps = willBecomeKing();
    if (kingJumps.size() != 0) {
      this.moves = kingJumps;
    }
    this.STATE = FINAL_STATE;
  }

  private void findMoves() {
    Map<Space, Space> moves = new HashMap<>();
    if (player.isRed()) {
      for (Piece redPiece : board.getRedPieces()) {
        Position start = new Position(redPiece.getXCoordinate(), redPiece.getCellIdx());
        Position right = new Position(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() + 1);
        Position left = new Position(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() - 1);
        if (checkMove.validateMove(start, right, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() - 1,
                  redPiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, left, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() - 1,
                  redPiece.getCellIdx() - 1));
        }
      }
    } else {
      for (Piece whitePiece : board.getWhitePieces()) {
        Position start = new Position(7 - whitePiece.getXCoordinate(), 7 - whitePiece.getCellIdx());
        Position right = new Position(7 - (whitePiece.getXCoordinate() + 1), 7 - (whitePiece.getCellIdx() + 1));
        Position left = new Position(7 - (whitePiece.getXCoordinate() + 1), 7 - (whitePiece.getCellIdx() - 1));
        if (checkMove.validateMove(start, right, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() + 1,
                  whitePiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, left, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() + 1,
                  whitePiece.getCellIdx() - 1));
        }
      }
    }
    this.moves = moves;
  }

  private void canJump() {
    Map<Space, Space> jumps = checkMove.findJumps(player);
    if (jumps.size() == 0) {
      findMoves();
      this.STATE = MOVE;
    } else {
      this.moves = jumps;
      this.STATE = JUMP;
    }
  }

  private boolean jumped(Space startingSpace, Space endingSpace, Space potentialJumped, Piece piece) {
    if (endingSpace.getxCoordinate() - startingSpace.getxCoordinate() == 2 ||
            endingSpace.getxCoordinate() - startingSpace.getxCoordinate() == -2) {
      if (endingSpace.getCellIdx() - startingSpace.getCellIdx() == 2 ||
              endingSpace.getCellIdx() - startingSpace.getCellIdx() == -2) {
        if (startingSpace.getxCoordinate() + 1 == potentialJumped.getxCoordinate() ||
                startingSpace.getxCoordinate() - 1 == potentialJumped.getxCoordinate()) {
          if (startingSpace.getCellIdx() - 1 == potentialJumped.getCellIdx() ||
                  startingSpace.getCellIdx() + 1 == potentialJumped.getCellIdx()) {
            return true;
          }
        }
      }
    } else {
      Position startingPosition = new Position(startingSpace.getxCoordinate(), startingSpace.getCellIdx());
      Position upperLeft;
      Position upperRight;
      Position bottomLeft;
      Position bottomRight;
      if (player.isRed()) {
        upperLeft = new Position(startingSpace.getxCoordinate() - 2, startingSpace.getCellIdx() - 2);
        upperRight = new Position(startingSpace.getxCoordinate() - 2, startingSpace.getCellIdx() + 2);
        if (piece.isKing()) {
          bottomLeft = new Position(startingSpace.getxCoordinate() + 2, startingSpace.getCellIdx() - 2);
          bottomRight = new Position(startingSpace.getxCoordinate() + 2, startingSpace.getCellIdx() + 2);
        } else {
          bottomLeft = null;
          bottomRight = null;
        }
      } else {
        upperLeft = new Position(startingSpace.getxCoordinate() + 2, startingSpace.getCellIdx() - 2);
        upperRight = new Position(startingSpace.getxCoordinate() + 2, startingSpace.getCellIdx() + 2);
        if (piece.isKing()) {
          bottomLeft = new Position(startingSpace.getxCoordinate() - 2, startingSpace.getCellIdx() - 2);
          bottomRight = new Position(startingSpace.getxCoordinate() - 2, startingSpace.getCellIdx() + 2);
        } else {
          bottomLeft = null;
          bottomRight = null;
        }
      }
      Map<Boolean, String> canMoveULeft = checkMove.validateMove(startingPosition, upperLeft, player);
      Map<Boolean, String> canMoveURight = checkMove.validateMove(startingPosition, upperRight, player);
      Map<Boolean, String> canMoveBLeft = null;
      Map<Boolean, String> canMoveBRight = null;
      while (canMoveULeft.containsKey(true) || canMoveURight.containsKey(true)) {
        if (canMoveULeft.containsKey(true) && canMoveURight.containsKey(true)) {
          Position oldUpperLeft = upperLeft;
          Position oldUpperRight = upperRight;
          if (player.isRed()) {
            upperLeft = new Position(startingPosition.getRow() - 2, startingPosition.getCell() - 2);
            upperRight = new Position(startingPosition.getRow() - 2, startingPosition.getCell() + 2);
          } else {
            upperLeft = new Position(startingPosition.getRow() + 2, startingPosition.getCell() - 2);
            upperRight = new Position(startingPosition.getRow() + 2, startingPosition.getCell() + 2);
          }
          canMoveULeft = checkMove.validateMove(oldUpperLeft, upperLeft, player);
          canMoveURight = checkMove.validateMove(oldUpperRight, upperRight, player);
        } else if (canMoveULeft.containsKey(true)) {
          return jumped(board.getSpace(upperLeft.getRow(), upperLeft.getCell()), endingSpace, potentialJumped, piece);
        } else if (canMoveURight.containsKey(true)) {
          return jumped(board.getSpace(upperRight.getRow(), upperRight.getCell()), endingSpace, potentialJumped, piece);
        } else {
          if (bottomLeft != null) {
            canMoveBLeft = checkMove.validateMove(startingPosition, bottomLeft, player);
          }
          if (bottomRight != null) {
            canMoveBRight = checkMove.validateMove(startingPosition, bottomRight, player);
          }
          if (canMoveBLeft != null || canMoveBRight != null) {
            if (canMoveBLeft == null) {
              if (canMoveBRight.containsKey(true)) {
                return jumped(board.getSpace(bottomRight.getRow(), bottomRight.getCell()),
                        endingSpace, potentialJumped, piece);
              }
            } else if (canMoveBRight == null) {
              if (canMoveBLeft.containsKey(true)) {
                return jumped(board.getSpace(bottomLeft.getRow(), bottomLeft.getCell()),
                        endingSpace, potentialJumped, piece);
              }
            } else {
              Position oldBottomLeft = bottomLeft;
              Position oldBottomRight = bottomRight;
              if (player.isRed()) {
                bottomLeft = new Position(startingPosition.getRow() + 2, startingPosition.getCell() - 2);
                bottomRight = new Position(startingPosition.getRow() + 2, startingPosition.getCell() + 2);
              } else {
                bottomLeft = new Position(startingPosition.getRow() - 2, startingPosition.getCell() - 2);
                bottomRight = new Position(startingPosition.getRow() - 2, startingPosition.getCell() + 2);
              }
              canMoveBLeft = checkMove.validateMove(oldBottomLeft, bottomLeft, player);
              canMoveBRight = checkMove.validateMove(oldBottomRight, bottomRight, player);
            }
          }
        }
      }
    }
    return false;
  }

  private boolean willBeEaten(Space startingSpace, Space endingSpace) {
    int xCoordinate = endingSpace.getxCoordinate();
    int cellIdx = endingSpace.getCellIdx();
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
                if (jumped(startingSpace, endingSpace, bottomLeft, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        }
      }
    }


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
                if (jumped(startingSpace, endingSpace, bottomRight, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        }
      }
    }


    if (xCoordinate + 1 <= 7 && cellIdx + 1 <= 7) {
      Space bottomRight = board.getSpace(xCoordinate + 1, cellIdx + 1);
      if (bottomRight.isOccupied()) {
        if (player.isRed()) {
          if (bottomRight.pieceIsWhite() && bottomRight.getPiece().isKing()) {
            if (xCoordinate - 1 >= 0 && cellIdx - 1 >= 0) {
              Space upperLeft = board.getSpace(xCoordinate - 1, cellIdx + 1);
              if (!upperLeft.isOccupied()) {
                return true;
              } else {
                if (jumped(startingSpace, endingSpace, upperLeft, startingSpace.getPiece())) {
                  return true;
                }
              }
            }
          }
        } else {
          if (bottomRight.pieceIsRed()) {
            if (xCoordinate - 1 >= 0 && cellIdx - 1 >= 0) {
              Space upperLeft = board.getSpace(xCoordinate - 1, cellIdx + 1);
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
                if (jumped(startingSpace, endingSpace, upperRight, startingSpace.getPiece())) {
                  return true;
                }
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
    return false;
  }

  private Map<Space, Space> wontBeEaten() {
    Map<Space, Space> nonEaten = new HashMap<>();
    for (Space startSpace : this.moves.keySet()) {
      if (!willBeEaten(startSpace, moves.get(startSpace))) {
        nonEaten.put(startSpace, this.moves.get(startSpace));
      }
    }
    return nonEaten;
  }

  private void canJumpAndEaten() {
    Map<Space, Space> nonEaten = wontBeEaten();
    if (nonEaten.size() != 0) {
      this.moves = nonEaten;
      this.STATE = JUMP_NO_EAT;
    } else {
      this.STATE = FINAL_STATE;
    }
  }

  private void canMoveAndEaten() {
    Map<Space, Space> nonEaten = wontBeEaten();
    if (nonEaten.size() != 0) {
      this.moves = nonEaten;
      this.STATE = MOVE_NO_EAT;
    } else {
      this.STATE = FINAL_STATE;
    }
  }
}