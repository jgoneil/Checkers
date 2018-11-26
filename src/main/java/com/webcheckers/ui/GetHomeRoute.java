package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.AbstractPlayer;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {

  //Static final variables (Constants)
  static final String USERS = "playerLobby";
  static final String USER = "user";
  static final String SIGNEDIN = "signedin";
  static final String ONLY_ONE = "onlyOne";
  static final String PLAYERSERVICES_KEY = "playerServices";

  //The logger for the system messages
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  //The HTML template for rendering the freemarker pages
  private final TemplateEngine templateEngine;
  //The playerLobby connected to the system
  private final PlayerLobby playerLobby;
  //The player signed into the game
  private AbstractPlayer player;

  /**
   * Create the Spark Route (UI controller) for the {@code GET /} HTTP request.
   *
   * @param templateEngine the HTML template rendering engine
   * @param playerLobby the playerLobby currently in the game
   */
  public GetHomeRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");

    Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    //
    this.templateEngine = templateEngine;

    this.playerLobby = playerLobby;
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    final Session httpSession = request.session();
    LOG.finer("GetHomeRoute is invoked.");
    //
    String player = httpSession.attribute(PLAYERSERVICES_KEY);
    this.player = playerLobby.getSpecificPlayer(player);
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    if (httpSession.attribute("message") != null) {
      vm.put("message", httpSession.attribute("message"));
      httpSession.removeAttribute("message");
    }

    if (player != null) {
      if (!this.player.inGame()) {
        if (httpSession.attribute(GetGameRoute.GAMELOBBY) != null) {
          httpSession.removeAttribute(GetGameRoute.GAMELOBBY);
        }
        if (httpSession.attribute(PostResignGame.RESIGNED_PLAYER) != null) {
          httpSession.removeAttribute(PostResignGame.RESIGNED_PLAYER);
        }
      }
    }

    if (this.player == null) {
      vm.put(SIGNEDIN, false);
      vm.put(USERS, playerLobby.getNumberOfPlayers());
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    } else if (playerLobby.getNumberOfPlayers() == 1) {
      vm.put(SIGNEDIN, true);
      vm.put(ONLY_ONE, true);
      vm.put(USER, this.player.getName());
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    } else if (this.player.inGame()) {
      response.redirect(WebServer.GAME_URL);
      halt();
      return null;
    } else {
      vm.put(SIGNEDIN, true);
      vm.put(ONLY_ONE, false);
      vm.put(USER, this.player.getName());
      vm.put(USERS, playerLobby.getAllPlayersExceptUser(this.player.getName()));
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }
  }

}
