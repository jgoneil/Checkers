package com.webcheckers.model;

/**
 * Model class to define messages back to the Game 
 */
public class Message {

  public enum Type {
    error, info
  }

  private String text;
  private Type type;

  /**
   * Constructor to create a new message for the game
   */
  public Message (Type messageType, String message) {
    this.type = messageType;
    this.text = message;
  }

  /**
   * Getter for the message type
   * @return the type of message being sent back (ENUM type error or info)
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Getter for the text contained for the message
   * @return the message string
   */
  public String getText() {
    return this.text;
  }
}
