package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import com.webcheckers.appl.BoardView;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import com.webcheckers.appl.Users;
import com.webcheckers.appl.Player;
import com.webcheckers.model.ModelBoard;

import static spark.Spark.halt;
import static spark.Spark.threadPool;

/**
 * UI class that handles all HTTP requests for the /game page
 */
public class GetGameRoute implements Route {

  public static final String VIEW = "game.ftl";
  public static final String MODEL_BOARD = "modelBoard";
  private final int LENGTH = 8;
  private TemplateEngine templateEngine;
  private Player currentPlayer;
  private BoardView boardView;
  private Users users;
  private String BOARD = "boardView";

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
    this.currentPlayer.setHasMoved(false);
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    if (httpSession.attribute(BOARD) == null && !currentPlayer.inGame()) {

      Object[] playerTwo = request.queryParams().toArray();
      Player player2 = users.getSpecificPlayer(playerTwo[0].toString());

      if (player2.inGame()) {
        httpSession.attribute("message", true);
        response.redirect("/");
        halt();
        return null;
      }

      this.boardView = new BoardView(currentPlayer, player2, LENGTH, "red");
      httpSession.attribute(BOARD, this.boardView);
      currentPlayer.setColor("Red", this.boardView);

      ModelBoard modelBoard = new ModelBoard(currentPlayer, player2, LENGTH);
      httpSession.attribute(MODEL_BOARD, modelBoard);
      currentPlayer.addModelBoard(modelBoard);
      player2.addModelBoard(modelBoard);

      player2.setColor("White", new BoardView(currentPlayer, player2, LENGTH, "white"));

      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", currentPlayer);
      vm.put("whitePlayer", player2);
      vm.put("activeColor", "RED");
      vm.put("board", this.boardView);
      return templateEngine.render(new ModelAndView(vm, VIEW));
    } else if (httpSession.attribute(BOARD) == null && currentPlayer.inGame()) {
      httpSession.attribute(BOARD, currentPlayer.getBoardView());
      httpSession.attribute(MODEL_BOARD, currentPlayer.getModelBoard());
      this.boardView = currentPlayer.getBoardView();

      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", this.boardView.getRedPlayer());
      vm.put("whitePlayer", this.boardView.getWhitePlayer());
      if (boardView.redTurn()) {
        vm.put("activeColor", "RED");
      } else {
        vm.put("activeColor", "WHITE");
      }
      vm.put("board", this.boardView);
      return templateEngine.render(new ModelAndView(vm, VIEW));
    } else {
      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", this.boardView.getRedPlayer());
      vm.put("whitePlayer", this.boardView.getWhitePlayer());
      if (boardView.redTurn()) {
        vm.put("activeColor", "RED");
      } else {
        vm.put("activeColor", "WHITE");
      }
      vm.put("board", currentPlayer.getBoardView());
      return templateEngine.render(new ModelAndView(vm, VIEW));
    }
  }
}
