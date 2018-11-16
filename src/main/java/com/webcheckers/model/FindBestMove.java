package com.webcheckers.model;

public class FindBestMove {

    private ModelBoard board;

    private Player Player;

    private CheckMove checkMove;


    public FindBestMove(ModelBoard board, Player player) {
      this.board = board;
      this.Player = player;
      this.checkMove = new CheckMove(board);
    }



    public Move findMove(){
      return null;
    }




    



    private boolean canJumpMultiAndKing(Piece piece) {
      return false;
    }

    private boolean canJumpMulti(Piece piece) {
      return false;
    }

    private boolean canJumpMultiAndEaten(Piece piece) {
      return false;
    }

    private boolean canJumpAndKing(Piece piece) {
      return false;
    }

    private boolean canJump(Piece piece) {
      return false;
    }

    private boolean canJumpAndEaten(Piece piece) {
      return false;
    }

    private boolean canMoveAndKing(Piece piece) {
      return false;
    }

    private boolean canMove(Piece piece) {
      return false;
    }

    private boolean canMoveAndEaten(Piece piece) {
      return false;
    }

}
