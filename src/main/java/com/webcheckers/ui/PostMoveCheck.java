package com.webcheckers.ui;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;

import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Board;
import com.webcheckers.model.CheckMove;

/**
 * The UI Controller for handling the check to see if a move is valid or not
 */
public class PostMoveCheck implements Route {

  private final Gson gson;
  private Player player;
  private CheckMove checkMove;

  private static final String MOVE = "move";

  /**
   * Main method for POST check of a valid move
   */
  public PostMoveCheck(final Gson gson) {

    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /validateMove}
   *
   * @param request the HTTP request
   * @param request the HTTP response
   * @return the AJAX response for the player's ability to move the piece (true/false)
   */ 
  @Override
  public Object handle(Request request, Response response) {
    System.out.println("POSTMOVECHECK is being called from route /validateMove");
    Session HTTPSession = request.session();
    
    this.player = HTTPSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

    if (HTTPSession.attribute(MOVE) == null) {
      this.checkMove = new CheckMove();
      HTTPSession.attribute(MOVE, checkMove);
    } else {
      this.checkMove = HTTPSession.attribute(MOVE);
    }
    return gson.toJson(false);
  }
}
