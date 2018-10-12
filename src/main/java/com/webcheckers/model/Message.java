package com.webcheckers.model;

/**
 * Model class to define messages back to the Game 
 */
public class Message {

  public enum Type {
    error, normal
  }

  public String message;
  public Type type;

  /**
   * Constructor to create a new message for the game
   */
  public Message (Type messageType, String message) {
    this.type = messageType;
    this.message = message;
  }

  public Type type() {
    return this.type;
  }

  public String text() {
    return this.message;
  }
}
