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
  private Player currentPlayer;
  private Users users;

  private String VIEW = "game.ftl";
  public static final String OTHER_PLAYER = "otherPlayer";
  public static final String RED_PLAYER = "redPlayer";
  public static final String WHITE_PLAYER = "whitePlayer";

  /**
   * Creates a spark route that handles all {@code Get /game} HTTP requests
   *
   * @param templateEngine the html template engine
   */
  public GetGameRoute(TemplateEngine templateEngine, Users users) {
    
    Objects.requireNonNull(templateEngine, "templateEngine cannot be null");
    Objects.requireNonNull(users, "users cannot be null");

    this.templateEngine = templateEngine;
    this.users = users;
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
    
    if(httpSession.attribute(RED_PLAYER) == null){
      if(request.queryParams().size() > 0){
        Object[] playerTwo = request.queryParams().toArray();
        this.whitePlayer = users.getSpecificPlayer((String) playerTwo[0].toString());
        this.currentPlayer = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
        this.redPlayer = currentPlayer;
        httpSession.attribute(RED_PLAYER, redPlayer);
        httpSession.attribute(WHITE_PLAYER, whitePlayer);

        Map<String, Object> vm = new HashMap<>(); 
        vm.put("title", "Welcome!");
        vm.put("currentPlayer", currentPlayer);
        vm.put("viewMode", "standard");
        vm.put("redPlayer", this.redPlayer);
        vm.put("whitePlayer", this.whitePlayer);
        vm.put("activeColor", "red");
        return templateEngine.render(new ModelAndView(vm, VIEW));
      }
    }
    return null; //temp hold. fix with error reponses
  }
}
