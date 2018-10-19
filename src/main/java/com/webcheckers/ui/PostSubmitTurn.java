package com.webcheckers.ui;

import com.webcheckers.model.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.BoardView;

import java.util.Objects;

/**
 * The UI controller for handling the submission of a turn for a player
 */
public class PostSubmitTurn implements Route {

  //GSON engine for recieving/sending JSON information
  private final Gson gson;

  /**
   * Main method for POST submit move 
   *
   * @param gson the json system for handling JQuery response/request methods
   */
  public PostSubmitTurn(final Gson gson) {
    
    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /submitMove}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's turn (true/false)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session session = request.session();

    return null;
  }
}
