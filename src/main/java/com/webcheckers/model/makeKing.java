package com.webcheckers.model;
import com.webcheckers.appl.Piece;

/**
 * Class to Convert a Single to a King
 */
public class makeKing {
    /**
     * Checks if a piece has made it to the other end of the Board so that it could become
     * King or not.
     * @param piece - Piece that is possibly being Kinged
     */
  public void kingPiece(Piece piece) {
    if (piece.getColor() == Piece.Color.RED) {
      if (piece.getSpace().getCellIdx() == 0) {
        piece.King();
      }
    }
    else if (piece.getColor() == Piece.Color.WHITE) {
      if (piece.getSpace().getCellIdx() == 7) {
        piece.King();
      }
    }
  }
}
