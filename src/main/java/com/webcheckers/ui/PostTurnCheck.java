package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * The UI Controller for handling the refresh to check the player turn
 */
public class PostTurnCheck implements Route {

  //Gson engine for receiving/sending JSON information
  private final Gson gson;

  /**
   * Main method for POST check of turn
   *
   * @param gson the json system for handling JQuery response/request methods
   */
  public PostTurnCheck(final Gson gson) {

    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /checkTurn}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's turn (true/false)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session session = request.session();
    String playerUsername = session.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    GameLobby gameLobby = session.attribute(GetGameRoute.GAMELOBBY);

    if (gameLobby.verifyInGame(playerUsername)) {
      if (gameLobby.checkRedPlayer(playerUsername)) {
        if (gameLobby.checkRedTurn()) {
          return gson.toJson(new Message(Message.Type.info, "true"));
        } else {
          return gson.toJson(new Message(Message.Type.info, "false"));
        }
      } else {
        if (gameLobby.checkRedTurn()) {
          return gson.toJson(new Message(Message.Type.info, "false"));
        } else {
          return gson.toJson(new Message(Message.Type.info, "true"));
        }
      }
    } else {
      return gson.toJson(new Message(Message.Type.info, "true"));
    }
  }
}
