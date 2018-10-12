package com.webcheckers.model;

import com.webcheckers.appl.Space;

public class CheckMove {
  /**
  * Sees if a space is valid for a piece to move onto
  * @param piece - piece to move
  * @param target - target space to move to
  * @return - validity of move to target space
  */
  public boolean validateMove(Space start, Space target){
    if(target.getColor().equals(Space.Color.WHITE)){
      return false;
    } else if(target.isOccupied()){
      return false;
    }
      return true;
  }
}
