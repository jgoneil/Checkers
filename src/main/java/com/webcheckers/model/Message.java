package com.webcheckers.model;

/**
 * Model class to define messages back to the Game 
 */
public class Message {

  public enum Type {
    error, info
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

  public Type getType() {
    return this.type;
  }

  public String getText() {
    return this.message;
  }
}
