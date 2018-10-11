package com.webcheckers.appl;

/**
 * Class that establishes spaces on the game board
 */
public class Space {
  public enum Color{
    BLACK, WHITE
  }
  private Location location;
  private Color color;
  private boolean occupied;

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

    /**
     * Equals method for Location
     * @param o object to compare to
     * @return boolean corresponding to equality between objects
     */
    public boolean equals(Object o) {
      if (o instanceof Location){
        Location obj = (Location) o;
        return (obj.xCoordinate == this.xCoordinate && obj.yCoordinate == this.yCoordinate);
      }
      return false;
    }
  }

  /**
   * Constructor for the space on the game board
   * @param x the x-coordinate of the space
   * @param y the y-coordinate of the space
   */
  public Space(int x, int y, Color c) {
    this.location = new Location(x, y);
    this.color = c;
    this.occupied = false;
  }

  /**
   * Getter for the color of the given space
   * @return - the color of the space
   */
  public Color getColor(){
    return this.color;
  }

  /**
   * Sets occupied value to true for space with a piece
   */
  public void occupy(){
    this.occupied = true;
  }

  /**
   * Sets occupied value to false when a piece has moved off of it
   */
  public void unopccupy(){
    this.occupied = false;
  }

  /**
   * Sees if this space is occupied
   * @return boolean for if the space is occupied
   */
  public boolean isOccupied(){
    return this.occupied;
  }

  /**
   * Getter for the location of the space on the game board
   * @return the location of the space
   */
  public Location cellIdx() {
    return this.location;
  }

  /**
   * Checks to see if a space is a valid location to move a piece to 
   *
   * @return a boolean expression based on if a piece can or can not be moved to the given space
   */
  public boolean isValid() {
    return true;
  }
}
