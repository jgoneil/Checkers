package com.webcheckers.ui;

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

import com.webcheckers.appl.PlayerLobby;

/**
 * This is {@code POST /signin } route handler. Handles user signin. Checks user input to ensure the
 * username is not already in use
 */
public class PostSignInRoute implements Route {

  //Static final variables (constants)
  static final String REVERT_VIEW = "signin.ftl";
  static final String USER_PARAM = "username";

  //HTML template loader for freemarker pages
  private final TemplateEngine templateEngine;
  //List of playerLobby connected to the game
  private final PlayerLobby playerLobby;

  /**
   * Constructor for class. Ensures both parameters are included in the declaration for use
   *
   * @param templateEngine the formatting definition for spark to java messaging
   * @param playerLobby the class holding all of the currently connected playerLobby
   */
  public PostSignInRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {

    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(playerLobby, "playerLobby must not be null");

    this.templateEngine = templateEngine;
    this.playerLobby = playerLobby;
  }

  /**
   * Main connection for playerLobby attempting to sign in Handles error checking on input to ensure
   * validity and that the input follows guidelines
   *
   * @param request the messages coming from the from the frontend
   * @param response the messages the backend (this class) are responding with
   * @return the rendered view page for the user
   */
  @Override
  public String handle(Request request, Response response) {
    final Map<String, Object> vm = new HashMap<>();
    final String username = request.queryParams(USER_PARAM);
    final Session httpSession = request.session();

    if (username != null) {
      ModelAndView mv;

      if (playerLobby.addPlayer(username)) {
        httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY, username.trim());
        response.redirect(WebServer.HOME_URL);
        return null;
      } else {
        vm.put("title", GetSigninRoute.TITLE);
        vm.put("header", GetSigninRoute.HEADER);
        vm.put(GetSigninRoute.ATTEMPT_FAILED, true);
        mv = new ModelAndView(vm, REVERT_VIEW);
        return templateEngine.render(mv);
      }
    } else {
      response.redirect(WebServer.HOME_URL);
      halt();
      return null;
    }
  }
}
