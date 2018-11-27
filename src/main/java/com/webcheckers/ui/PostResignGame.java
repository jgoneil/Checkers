package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.AbstractPlayer;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;

import com.google.gson.Gson;

/**
 * Class for resigning a game {@code postResign}
 */
public class PostResignGame implements Route {

  //Static final variables (constants)
  static final String ERROR_RESIGN = "Cannot resign due a move being made. Reverse the move if you would like to truly resign.";
  static final String SUCCESS_RESIGN = "You have successfully resigned from the game. You lost.";
  static final String OTHER_PLAYER_RESIGN = "Your opponent resigned from the game. You win!";
  static final String RESIGNED_PLAYER = "resignedPlayer";

  //Gson controller for reading and sending JSON information
  private final Gson gson;
  //Player that disconnected from the system
  private AbstractPlayer resignedPlayer;
  //Player lobby for the current session
  private PlayerLobby playerLobby;

  /**
   * Main method for POST resign game
   *
   * @param gson the gson parser for JQuery Requests/Responses
   */
  public PostResignGame(final Gson gson, final PlayerLobby playerLobby) {

    Objects.requireNonNull(gson, "Gson cannot be null.");
    Objects.requireNonNull(playerLobby, "Player lobby cannot be null");

    this.gson = gson;
    this.playerLobby = playerLobby;
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
    Session httpSession = request.session();

    String playerUsername = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    AbstractPlayer player = playerLobby.getSpecificPlayer(playerUsername);
    GameLobby gameLobby = httpSession.attribute(GetGameRoute.GAMELOBBY);

    if (gameLobby == null) {
      Message message = new Message(Message.Type.info, OTHER_PLAYER_RESIGN);
      httpSession.attribute(RESIGNED_PLAYER, resignedPlayer);
      httpSession.attribute("message", message);
      return gson.toJson(message);
    }

    if (gameLobby.checkPendingMove()) {
      Message message = new Message(Message.Type.error, ERROR_RESIGN);
      return gson.toJson(message);
    } else {
      AbstractPlayer player2 = gameLobby.getOpponent(player);
      player.gameEnd();
      player2.gameEnd();
      this.resignedPlayer = player;
      httpSession.removeAttribute(GetGameRoute.GAMELOBBY);
      Message message = new Message(Message.Type.info, SUCCESS_RESIGN);
      httpSession.attribute(RESIGNED_PLAYER, resignedPlayer);
      httpSession.attribute("message", message);
      return gson.toJson(message);
    }
  }
}
