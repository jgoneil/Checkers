package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.AbstractPlayer;
import com.webcheckers.model.Message;
import com.webcheckers.model.Move;
import spark.*;

import java.util.Objects;
import java.util.Map;

import com.google.gson.Gson;
import com.webcheckers.model.Player;

/**
 * The UI Controller for handling the check to see if a move is valid or not
 */
public class PostMoveCheck implements Route {

  //Gson controller for reading and sending JSON information
  private final Gson gson;
  //The player that resigned for the game
  private AbstractPlayer resignedPlayer;

  /**
   * Main method for POST check of a valid move
   *
   * @param gson the gson parser for JQuery Requests/Responses
   */
  public PostMoveCheck(final Gson gson) {

    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /validateMove}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's ability to move the piece (true/false)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session HTTPSession = request.session();

    String playerUsername = HTTPSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    GameLobby gameLobby = HTTPSession.attribute(GetGameRoute.GAMELOBBY);

    if (gameLobby == null) {
      return gson.toJson(new Message(Message.Type.error, PostResignGame.OTHER_PLAYER_RESIGN));
    }

    if (!gameLobby.verifyInGame(playerUsername)) {
      if (this.resignedPlayer == null) {
        if (gameLobby.checkRedPlayer(playerUsername)) {
          this.resignedPlayer = gameLobby.getWhitePlayer();
        } else {
          this.resignedPlayer = gameLobby.getRedPlayer();
        }
      }
      HTTPSession.removeAttribute(GetGameRoute.GAMELOBBY);
      HTTPSession.attribute(PostResignGame.RESIGNED_PLAYER, resignedPlayer);
      return gson.toJson(new Message(Message.Type.error, PostResignGame.OTHER_PLAYER_RESIGN));
    }

    if (gameLobby.checkMadeMove()) {
      return gson.toJson(new Message(Message.Type.error, "Can only do one move per turn"));
    }

    String customJson = request.body();
    Move move = gson.fromJson(customJson, Move.class);

    Map<Boolean, String> resultFromCheck;
    if (gameLobby.checkRedPlayer(playerUsername)) {
      resultFromCheck = gameLobby
          .validateMove(move.getStart(), move.getEnd(), gameLobby.getRedPlayer());
    } else {
      resultFromCheck = gameLobby
          .validateMove(move.getStart(), move.getEnd(), gameLobby.getWhitePlayer());
    }
    if (resultFromCheck.containsKey(true)) {
      gameLobby.pendingMove(move);
      Message message = new Message(Message.Type.info, resultFromCheck.get(true));
      return gson.toJson(message);
    } else {
      Message message = new Message(Message.Type.error, resultFromCheck.get(false));
      return gson.toJson(message);
    }

  }
}
