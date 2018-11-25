package com.webcheckers.ui;

import static spark.Spark.halt;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.AbstractPlayer;
import com.webcheckers.model.AiPlayer;
import java.util.Objects;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

/**
 * UI class that handles all HTTP request for the /playAI route
 */
public class GetPlayAIRoute implements Route {
  //The template engine for rendering the freemarker html page
  private TemplateEngine templateEngine;
  //The player currently interacting with the game backend
  private AbstractPlayer currentPlayer;
  //The playerLobby connected to the system
  private PlayerLobby playerLobby;

  /**
   * Creates a spark route that handles all {@code Get /game} HTTP requests
   *
   * @param templateEngine the html template engine
   */
  public GetPlayAIRoute(TemplateEngine templateEngine, PlayerLobby playerLobby) {

    Objects.requireNonNull(templateEngine, "templateEngine cannot be null");
    Objects.requireNonNull(playerLobby, "playerLobby cannot be null");

    this.templateEngine = templateEngine;
    this.playerLobby = playerLobby;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    AiPlayer aiPlayer = new AiPlayer();
    playerLobby.addPlayer(aiPlayer);

    response.redirect(String.format("%s?%s",WebServer.GAME_URL, aiPlayer.getName()));
    halt();
    return null;
  }
}
