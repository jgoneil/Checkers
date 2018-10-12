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
 * The UI Controller for handling the refresh to check the player turn
 */
public class PostTurnCheck implements Route {

  private final Gson gson;

  /**
   * Main method for POST check of turn
   */
  public PostTurnCheck(final Gson gson) {
    
    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }
  
  /**
   * Method to render AJAX response via gson for {@code POST /checkturn}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's turn (true/false)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session session = request.session();
    Player player = session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

    BoardView boardView;

    if(player.getBoardView() != null) {
      boardView = player.getBoardView();
      if (player.getColor().equals("red")) {
        if (boardView.redTurn()) {
          return gson.toJson(new Message(Message.Type.info, "true"));
        } else {
          return gson.toJson(new Message(Message.Type.info, "false"));
        }
      } else {
        if (boardView.redTurn()) {
          return gson.toJson(new Message(Message.Type.info, "false"));
        } else {
          return gson.toJson(new Message(Message.Type.info, "true"));
        }
      }
    }
    return "";
  }
}
