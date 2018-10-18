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

import com.webcheckers.appl.Users;
import com.webcheckers.appl.Player;

/*
 * This is {@code POST /signin } route handler. Handles user signin.
 * Checks user input to ensure the username is not already in use
 */
public class PostSignInRoute implements Route {

  static final String VIEW_NAME = "home.ftl";
  static final String REVERT_VIEW = "signin.ftl";
  static final String USER_PARAM = "username";
  static final String USERNAME_ERROR = "invalid username";
  static final String RETRY = "Please try again";
  static final String PLAYER = "player";

  private final TemplateEngine templateEngine;
  private final Users users;
  private Player player;

  /*
   * Constructor for class. Ensures both parameters are included in the declaration for use
   * @param templateEngine the formatting definition for spark to java messaging
   * @param users the class holding all of the currently connected users
   */
  public PostSignInRoute(final TemplateEngine templateEngine, final Users users) {

    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(users, "users must not be null");

    this.templateEngine = templateEngine;
    this.users = users;
  }

  /*
   * Main connection for users attempting to sign in
   * Handles error checking on input to ensure validity and that the input follows guidelines
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

      if (users.addPlayer(username)) {
        this.player = users.getSpecificPlayer(username.trim());
        httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY, player);
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
