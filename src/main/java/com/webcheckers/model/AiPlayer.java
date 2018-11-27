package com.webcheckers.model;

public class AiPlayer extends AbstractPlayer {

  public static int fakePlayerID = 1;


  /**
   * Constructor for player class
   */
  public AiPlayer() {
    super(String.format("F@ke %d", fakePlayerID++));
  }

}
