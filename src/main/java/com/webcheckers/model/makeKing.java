package com.webcheckers.model;
import com.webcheckers.appl.Piece;
import com.webcheckers.appl.Player;

/**
 * Class to Convert a Single to a King
 */
public class makeKing {
    /**
     * Checks if a piece has made it to the other end of the Board so that it could become
     * King or not.
     * @param piece - Piece that is possibly being Kinged
     * @param player - Player controlling the piece that is being Kinged
     */
  public void kingPiece(Piece piece, Player player) {
    if (!player.getHasMoved()) {
      if (piece.getColor() == Piece.Color.RED) {
        if (piece.getSpace().getCellIdx() == 0) {
          piece.King();
          player.setHasMoved(true);
        }
      }
      else if (piece.getColor() == Piece.Color.WHITE) {
        if (piece.getSpace().getCellIdx() == 7) {
          piece.King();
          player.setHasMoved(true);
        }
      }
    }
  }
}
