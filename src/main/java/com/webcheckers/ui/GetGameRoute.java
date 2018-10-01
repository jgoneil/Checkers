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
import com.webcheckers.appl.Users;
import com.webcheckers.appl.Player;

/**
 * UI class that handles all HTTP requests for the /game page
 */
public class GetGameRoute implements Route {

  private TemplateEngine templateEngine;
  private Player redPlayer;
  private Player whitePlayer;

  private String VIEW = "game.ftl";
  public static final String OTHER_PLAYER = "otherPlayer";

  /**
   * Creates a spark route that handles all {@code Get /game} HTTP requests
   *
   * @param templateEngine the html template engine
   */
  public GetGameRoute(TemplateEngine templateEngine) {
    
    Objects.requireNonNull(templateEngine, "templateEngine cannot be null");

    this.templateEngine = templateEngine;
  }

  /**
   * Render the Game page for the WebCheckers Application
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the rendered HTML page
   */
  @Override
  public Object handle(Request request, Response response) {
    Session httpSession = request.session();
    this.redPlayer = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    System.out.println("Other Player: " + request.body());
    Map<String, Object> vm = new HashMap<>(); 
    vm.put("title", "Welcome!");
    return templateEngine.render(new ModelAndView(vm, VIEW));
  }
}
