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

  private void canJumpMulti() {
    Map<Space, Space> multiJumps = new HashMap<>();
    for (Space startSpace : moves.keySet()) {
      Space endSpace = moves.get(startSpace);
      if (Math.abs(endSpace.getxCoordinate() - startSpace.getxCoordinate()) >= 4) {
        multiJumps.put(startSpace, endSpace);
      } else if (Math.abs(endSpace.getCellIdx() - startSpace.getCellIdx()) >= 4) {
        multiJumps.put(startSpace, endSpace);
      }
    }
    if (multiJumps.size() != 0) {
      this.moves = multiJumps;
      this.STATE = MULTI_JUMP;
    } else {
      this.STATE = FINAL_STATE;
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
      for (Piece redPiece: board.getRedPieces()) {
        Position start = new Position(redPiece.getXCoordinate(), redPiece.getCellIdx());
        Position right = new Position(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() + 1);
        Position left = new Position(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() - 1);
        if (checkMove.validateMove(start, right, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, left, player).containsKey(true)) {
          moves.put(redPiece.getSpace(), board.getSpace(redPiece.getXCoordinate() - 1, redPiece.getCellIdx() - 1));
        }
      }
    }
    else {
      for (Piece whitePiece: board.getWhitePieces()) {
        Position start = new Position(7 - whitePiece.getXCoordinate(), 7 - whitePiece.getCellIdx());
        Position right = new Position(7 - (whitePiece.getXCoordinate() + 1), 7 - (whitePiece.getCellIdx() + 1));
        Position left = new Position(7 - (whitePiece.getXCoordinate() + 1), 7 - (whitePiece.getCellIdx() - 1));
        if (checkMove.validateMove(start, right, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() + 1, whitePiece.getCellIdx() + 1));
        }
        if (checkMove.validateMove(start, left, player).containsKey(true)) {
          moves.put(whitePiece.getSpace(), board.getSpace(whitePiece.getXCoordinate() + 1, whitePiece.getCellIdx() - 1));
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

  private boolean willBeEaten(Piece piece) {
    int xCoordinate = piece.getXCoordinate();
    int cellIdx = piece.getCellIdx();
    if (xCoordinate - 1 >= 0 && cellIdx + 1 <= 7) {
      Space upperRight = board.getSpace(xCoordinate + 1, cellIdx + 1);
      if (upperRight.isOccupied()) {
        if (player.isRed()) {
          if (upperRight.pieceIsWhite()) {
            if (xCoordinate + 1 <= 7 && cellIdx - 1 >= 0) {
              Space bottomLeft = board.getSpace(xCoordinate + 1, cellIdx - 1);
              if (!bottomLeft.isOccupied()) {
                return true;
              }
            }
          }
        } else {
          if (upperRight.pieceIsRed() && upperRight.getPiece().isKing()) {
            if (xCoordinate + 1 <= 7 && cellIdx - 1 >= 0) {
              Space bottomLeft = board.getSpace(xCoordinate + 1, cellIdx - 1);
              if (!bottomLeft.isOccupied()) {
                return true;
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
              }
            }
          }
        } else {
          if (upperLeft.pieceIsRed() && upperLeft.getPiece().isKing()) {
            if (xCoordinate + 1 <= 7 && cellIdx + 1 <= 7) {
              Space bottomRight = board.getSpace(xCoordinate + 1, cellIdx + 1);
              if (!bottomRight.isOccupied()) {
                return true;
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
              }
            }
          }
        } else {
          if (bottomRight.pieceIsRed()) {
            if (xCoordinate - 1 >= 0 && cellIdx - 1 >= 0) {
              Space upperLeft = board.getSpace(xCoordinate - 1, cellIdx + 1);
              if (!upperLeft.isOccupied()) {
                return true;
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
      if (!willBeEaten(startSpace.getPiece())) {
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