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
import com.webcheckers.appl.Board;
import static spark.Spark.halt;

/**
 * UI class that handles all HTTP requests for the /game page
 */
public class GetGameRoute implements Route {

  private TemplateEngine templateEngine;
  private Player currentPlayer;
  private Board board;
  private Users users;

  public static final String VIEW = "game.ftl";
  private String BOARD = "board";
  private final int LENGTH = 8;

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
    this.currentPlayer = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    Map<String, Object> vm = new HashMap<>(); 
    vm.put("title", "Welcome!");

    if(httpSession.attribute(BOARD) == null && !currentPlayer.inGame()) {
      
      Object[] playerTwo = request.queryParams().toArray();
      Player player2 = users.getSpecificPlayer((String) playerTwo[0].toString());


      if(player2.inGame()) {
        response.redirect(PostSignInRoute.VIEW_NAME);
        halt();
        return null;
      }

      this.board = new Board(currentPlayer, player2, LENGTH);
      httpSession.attribute(BOARD, this.board);
      currentPlayer.setColor("Red", this.board);

      if(player2.inGame()) {
        response.redirect(PostSignInRoute.VIEW_NAME);
        halt();
        return null;
      }

      player2.setColor("White", this.board);

      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", currentPlayer);
      vm.put("whitePlayer", player2);
      vm.put("activeColor", "RED");
      vm.put("board", this.board);
      return templateEngine.render(new ModelAndView(vm, VIEW));
    } else if (httpSession.attribute(BOARD) == null && currentPlayer.inGame()) {
      httpSession.attribute(BOARD, currentPlayer.getBoard());
      this.board = currentPlayer.getBoard();

      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", this.board.getRedPlayer());
      vm.put("whitePlayer", this.board.getWhitePlayer());
      if(board.redTurn()) {
        vm.put("activeColor", "RED");
      } else {
        vm.put("activeColor", "WHITE");
      }
      vm.put("board", this.board);
      return templateEngine.render(new ModelAndView(vm, VIEW));
    } else {
      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", this.board.getRedPlayer());
      vm.put("whitePlayer", this.board.getWhitePlayer());
      if(board.redTurn()) {
        vm.put("activeColor", "RED");
      } else {
        vm.put("activeColor", "WHITE");
      }
      vm.put("board", this.board);
      return templateEngine.render(new ModelAndView(vm, VIEW));
    }
  }
}
