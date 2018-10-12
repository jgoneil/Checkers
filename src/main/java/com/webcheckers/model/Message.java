package com.webcheckers.model;

/**
 * Model class to define messages back to the Game 
 */
public class Message {

  public enum type {
    error, normal
  }

  private String message;
  private type messageType;

  /**
   * Constructor to create a new message for the game
   */
  public Message (type messageType, String message) {
    this.messageType = messageType;
    this.message = message;
  }
}
