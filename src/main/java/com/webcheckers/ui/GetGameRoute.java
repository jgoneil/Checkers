package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.PlayerBoard;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import com.webcheckers.model.Player;

import static spark.Spark.halt;

/**
 * UI class that handles all HTTP requests for the /game page
 */
public class GetGameRoute implements Route {

  //The template engine for rendering the freemarker html page
  private TemplateEngine templateEngine;
  //The player currently interacting with the game backend
  private Player currentPlayer;
  //The playerLobby connected to the system
  private PlayerLobby playerLobby;
  //The gameLobby for the active game session
  private GameLobby gameLobby;

  //Static final variables (Constants)
  public static final String GAMELOBBY = "gameLobby";
  public static final String VIEW = "game.ftl";

  /**
   * Creates a spark route that handles all {@code Get /game} HTTP requests
   *
   * @param templateEngine the html template engine
   */
  public GetGameRoute(TemplateEngine templateEngine, PlayerLobby playerLobby) {

    Objects.requireNonNull(templateEngine, "templateEngine cannot be null");
    Objects.requireNonNull(playerLobby, "playerLobby cannot be null");

    this.templateEngine = templateEngine;
    this.playerLobby = playerLobby;
  }

  /**
   * Setter for the game lobby for testing purposes
   *
   * @param gameLobby the game lobby being added to the game route class
   */
  public void setGameLobby(GameLobby gameLobby) {
    this.gameLobby = gameLobby;
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
    String player = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);
    this.currentPlayer = playerLobby.getSpecificPlayer(player);

    if (this.currentPlayer == null) {
      response.redirect(WebServer.HOME_URL);
      halt();
      return null;
    }

    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    if (httpSession.attribute(GAMELOBBY) == null && !currentPlayer.inGame()) {

      if (request.queryParams().size() == 0) {
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
      }

      Object[] playerTwo = request.queryParams().toArray();
      Player player2 = playerLobby.getSpecificPlayer(playerTwo[0].toString());

      if (httpSession.attribute(PostResignGame.RESIGNED_PLAYER) != null) {
        if(httpSession.attribute(PostResignGame.RESIGNED_PLAYER).equals(player2)) {
          httpSession.removeAttribute(PostResignGame.RESIGNED_PLAYER);
          response.redirect(WebServer.HOME_URL);
          halt();
          return null;
        }
      }

      if (player2.inGame()) {
        httpSession.attribute("message", new Message(Message.Type.error, "Player already in game!"));
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
      }

      this.gameLobby = new GameLobby(currentPlayer, player2);
      httpSession.attribute(GAMELOBBY, this.gameLobby);

      PlayerBoard playerBoard = gameLobby.getRedPlayerBoard();

      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", currentPlayer);
      vm.put("whitePlayer", player2);
      vm.put("activeColor", GameLobby.RED);
      vm.put("board", playerBoard);
      return templateEngine.render(new ModelAndView(vm, VIEW));
    } else if (httpSession.attribute(GAMELOBBY) == null && currentPlayer.inGame()) {
      httpSession.attribute(GAMELOBBY, this.gameLobby);

      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");
      vm.put("redPlayer", gameLobby.getRedPlayer());
      vm.put("whitePlayer", gameLobby.getWhitePlayer());
      vm.put("activeColor", gameLobby.getTurn());
      vm.put("board", gameLobby.getWhiteBoard());
      return templateEngine.render(new ModelAndView(vm, VIEW));
    } else if (httpSession.attribute(GAMELOBBY) != null && !currentPlayer.inGame()) {
      httpSession.attribute("message", new Message(Message.Type.info, PostResignGame.OTHER_PLAYER_RESIGN)); 
      response.redirect(WebServer.HOME_URL);
      halt();
      return null;
    } else {
      if (this.gameLobby != httpSession.attribute(GAMELOBBY)) {
        this.gameLobby = httpSession.attribute(GAMELOBBY);
      }

      if (!this.gameLobby.whiteCanPlay() || !this.gameLobby.redCanPlay()) {
        if (this.currentPlayer.isRed()) {
          if (!this.gameLobby.whiteCanPlay()) {
            httpSession.attribute("message", new Message(Message.Type.info, PostSubmitTurn.PLAYER_WON));
          } else {
            httpSession.attribute("message", new Message(Message.Type.info, PostSubmitTurn.PLAYER_LOSS));
          }
        } else {
          if (!this.gameLobby.redCanPlay()) {
            httpSession.attribute("message", new Message(Message.Type.info, PostSubmitTurn.PLAYER_WON));
          } else {
            httpSession.attribute("message", new Message(Message.Type.info, PostSubmitTurn.PLAYER_LOSS));
          }
        }
        this.currentPlayer.gameEnd();
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
      }

      gameLobby.setPendingMove(false);
      vm.put("currentPlayer", currentPlayer);
      vm.put("viewMode", "PLAY");

      vm.put("redPlayer", gameLobby.getRedPlayer());
      vm.put("whitePlayer", gameLobby.getWhitePlayer());
      vm.put("activeColor", gameLobby.getTurn());
      vm.put("board", gameLobby.getBoardForPlayer(currentPlayer));
      return templateEngine.render(new ModelAndView(vm, VIEW));
    }
  }
}
