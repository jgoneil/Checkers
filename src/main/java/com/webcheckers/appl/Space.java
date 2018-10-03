package com.webcheckers.appl;

/**
 * Class that establishes spaces on the game board
 */
public class Space {

  private Location location;

  /**
   * Class that establishes the location of a space
   */
  public class Location {
    
    private int xCoordinate;
    private int yCoordinate;

    /**
     * Constructor that establishes a location on the gameboard
     * @param x the x-coordinate of the 
     */
    public Location(int x, int y) {
      this.xCoordinate = x;
      this.yCoordinate = y;

    }
    
    /** 
     * Getter for the x-coordinate of the location
     * @return the integer for the x-coordinate of the space
     */
    public int getXCoordinate() {
      return this.xCoordinate;
    }

    /**
     * Getter for the y-coordinate of the location
     * @return the integer for the y-coordinate of the space
     */
    public int getYCoordinate() {
      return this.yCoordinate;
    }
  }

  /**
   * Constructor for the space on the game board
   * @param x the x-coordinate of the space
   * @param y the y-coordinate of the space
   */
  public Space(int x, int y) {
    this.location = new Location(x, y);
  }

  /**
   * Getter for the location of the space on the game board
   * @return the location of the space
   */
  public Location cellIdx() {
    return this.location;
  }
}
