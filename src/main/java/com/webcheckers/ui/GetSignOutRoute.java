package com.webcheckers.ui;


import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.PlayerBoardView;

import java.util.Objects;

import com.webcheckers.appl.PlayerLobby;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;
import spark.Session;

import static spark.Spark.halt;

import com.webcheckers.model.Player;

/**
 * This is {@code GETE /signout } route handler. Handles user signout. Logs user out of the game
 * lobby and returns them to the home screen.
 */
public class GetSignOutRoute implements Route {

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;

  /**
   * Constructor for class. Ensures both parameters are included in the declaration for use
   *
   * @param templateEngine the formatting definition for spark to java messaging
   * @param playerLobby the class holding all of the currently connected playerLobby
   */
  public GetSignOutRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {

    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(playerLobby, "playerLobby must not be null");

    this.templateEngine = templateEngine;
    this.playerLobby = playerLobby;
  }

  /**
   * Main connection for playerLobby attempting to sign out
   *
   * Remove player information from game and redirects to home
   *
   * @param request the messages coming from the from the frontend
   * @param response the messages the backend (this class) are responding with
   * @return null
   */
  @Override
  public String handle(Request request, Response response) {
    final Session httpSession = request.session();

    if (httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY) != null) {
      String playerUsername = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
      Player player = playerLobby.getSpecificPlayer(playerUsername);

      if (player.inGame()) {
        GameLobby gameLobby = httpSession.attribute(GetGameRoute.GAMELOBBY);
        if(gameLobby != null) {
          Player player2 = gameLobby.getOpponent(player);

          player2.gameEnd();
          httpSession.removeAttribute(GetGameRoute.GAMELOBBY);
        } else {
          httpSession.removeAttribute(GetGameRoute.GAMELOBBY);
        }
      }

      playerLobby.removeUser(player.getName());
      httpSession.removeAttribute(GetHomeRoute.PLAYERSERVICES_KEY);
      response.redirect(WebServer.HOME_URL);
      return null;
    } else {
      response.redirect(WebServer.HOME_URL);
      halt();
      return null;
    }
  }
}

