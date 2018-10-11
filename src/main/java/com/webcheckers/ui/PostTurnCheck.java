package com.webcheckers.ui;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Board;
import static spark.Spark.halt;

import java.util.Objects;

/**
 * The UI Controller for handling the refresh to check the player turn
 */
public class PostTurnCheck implements Route {

  private final Gson gson;

  public PostTurnCheck(final Gson gson) {
    
    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  @Override
  public Object handle(Request request, Response response) {
    Session session = request.session();
    Player player = session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    Board board = player.getBoard();
    if(player.getColor().equals("red")) {
      if(board.redTurn()) {
        return gson.toJson(true);
      } else {
        return gson.toJson(false);
      }
    } else {
        if(board.redTurn()) {
          return gson.toJson(false);
        } else {
          return gson.toJson(true);
        }
    }
  }
}
