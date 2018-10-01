package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import com.webcheckers.appl.Player;

/**
 * UI class that handles all /postGame HTTP requests
 */
public class PostGameRoute implements Route {

  private TemplateEngine templateEngine;
  private Player redPlayer;
  private Player whitePlayer;

  /**
   * Creates spark route for all {@code POST /postGame} HTTP requests
   * @param templateEngine the html template engine
   */
  public PostGameRoute(TemplateEngine templateEngine) {
    Objects.requireNonNull(templateEngine, "templateEngine cannot be null");
    this.templateEngine = templateEngine;
  }

  /**
   * Handles user interactions to start a game 
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return null 
   * (Completes a redirect to the /Game page or /Home page dependent on if an error occurs or not)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session httpSession = request.session(); 
    httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    System.out.print("Other player: " + request.attribute(GetGameRoute.OTHER_PLAYER));
    response.redirect("/game");
    return null;
  }
}
