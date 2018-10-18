package com.webcheckers.ui;

import com.webcheckers.model.Message;
import com.webcheckers.appl.Player;
import spark.*;

import java.util.Objects;
import java.util.Map;

import com.google.gson.Gson;

/**
 *
 */
public class PostResignGame implements Route {

  //Gson controller for reading and sending JSON information
  private final Gson gson;
  
  /**
   * Main method for POST resign game
   *
   * @param gson the gson parser for JQuery Requests/Responses
   */
  public PostResignGame(final Gson gson) {
    
    Objects.requireNonNull(gson, "Gson cannot be null.");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /resignGame}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's resignation
   */
  @Override
  public Object handle(Request request, Response response) {
    return null;
  }

}
