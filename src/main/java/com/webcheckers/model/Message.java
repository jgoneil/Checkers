package com.webcheckers.model;

/**
 * Model class to define messages back to the Game
 */
public class Message {

  //The text included in the message
  private String text;
  //The type of message being sent back to the frontend
  private Type type;

  /**
   * Enum for the message type
   */
  public enum Type {
    error, info
  }

  /**
   * Constructor to create a new message for the game
   *
   * @param message the message going to the system
   * @param messageType the type of the message response
   */
  public Message(Type messageType, String message) {
    this.type = messageType;
    this.text = message;
  }

  /**
   * Getter for the message type
   *
   * @return the type of message being sent back (ENUM type error or info)
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Getter for the text contained for the message
   *
   * @return the message string
   */
  public String getText() {
    return this.text;
  }
}
