package com.webcheckers.ui;

import com.webcheckers.appl.BoardView;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;
import spark.Session;

import static spark.Spark.halt;

import com.webcheckers.appl.Users;
import com.webcheckers.appl.Player;

/**
 * This is {@code GETE /signout } route handler. Handles user signout. Logs user out of the game
 * lobby and returns them to the home screen.
 */
public class GetSignOutRoute implements Route {

  private final TemplateEngine templateEngine;
  private final Users users;

  /**
   * Constructor for class. Ensures both parameters are included in the declaration for use
   *
   * @param templateEngine the formatting definition for spark to java messaging
   * @param users the class holding all of the currently connected users
   */
  public GetSignOutRoute(final TemplateEngine templateEngine, final Users users) {

    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(users, "users must not be null");

    this.templateEngine = templateEngine;
    this.users = users;
  }

  /**
   * Main connection for users attempting to sign out
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
      Player player = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

      if (player.inGame()) {
        Player player2;
        BoardView boardView = httpSession.attribute(GetGameRoute.BOARD);
        if (player.getColor().equals("Red")) {
          player2 = boardView.getWhitePlayer();
        } else {
          player2 = boardView.getRedPlayer();
        }

        player2.gameEnd();
        httpSession.removeAttribute(GetGameRoute.BOARD);
        httpSession.removeAttribute(GetGameRoute.MODEL_BOARD);
      }
      users.removeUser(player.getName());
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
