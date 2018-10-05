package com.webcheckers.appl;

public class Piece {
  private enum Color{
    RED, WHITE
  }
  private Space loc;
  private Color color;
  public Piece(Player p, Space s){
    this.loc = s;
    String tempColor = p.getColor();
    if(tempColor.equals("red")){
      this.color = Color.RED;
    }
    else{
      this.color = Color.WHITE;
    }
  }

    /**
     * Sees if a space is a valid location or not for a piece to move to.
     * @param space - space that is being checked
     * @return if space is valid or not
     */
  public boolean isValidMove(Space space){
    if(space.getColor() == Space.Color.WHITE){
      return false;
    }
    else if(space.isOccupied()){
      return false;
    }
    return true;
  }
}
