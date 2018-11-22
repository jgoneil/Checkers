package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Message;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;

/**
 * UI class that handles all HTTP requests for the {@code POST} /requestHelp page
 */
public class PostRequestHelp implements Route {

  //Gson controller for sending messages to the frontend
  private Gson gson;
  //Player lobby containing the player attempting to receive help
  private PlayerLobby playerLobby;

  /**
   * Creates a spark route for requesting help during a player's turn
   *
   * @param gson the gson parser for JQuery Requests/Responses
   * @param playerLobby the lobby of players currently connected to the game
   */
  public PostRequestHelp(Gson gson, PlayerLobby playerLobby) {
    Objects.requireNonNull(gson);
    Objects.requireNonNull(playerLobby);
    this.gson = gson;
    this.playerLobby = playerLobby;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /requestHelp}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return a error message or a list of moves a player can make in the game
   */
  @Override
  public Object handle(Request request, Response response) {
    Session HTTPSession = request.session();

    String playerUsername = HTTPSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    GameLobby gameLobby = HTTPSession.attribute(GetGameRoute.GAMELOBBY);
    Player currentPlayer = playerLobby.getSpecificPlayer(playerUsername);
    if (currentPlayer == null) {
      return gson.toJson(new Message(Message.Type.error, "Current Player Signed Out"));
    }

    if (gameLobby == null) {
      return gson.toJson(new Message(Message.Type.error, PostResignGame.OTHER_PLAYER_RESIGN));
    }

    Move bestMove = gameLobby.findBestMove(currentPlayer);
    Move[] tempHold = new Move[1];
    tempHold[0] = bestMove;
    return gson.toJson(tempHold);
  }

}
