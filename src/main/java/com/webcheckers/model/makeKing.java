package com.webcheckers.model;

import com.webcheckers.appl.Piece;

public class makeKing {
  public void kingPiece(Piece piece){
    if(piece.getColor() == Piece.Color.RED){
      if(piece.getSpace().getCellIdx() == 0){
          piece.King();
      }
    }
    else if(piece.getColor() == Piece.Color.WHITE){
      if(piece.getSpace().getCellIdx() == 7){
          piece.King();
      }
    }
  }
}
