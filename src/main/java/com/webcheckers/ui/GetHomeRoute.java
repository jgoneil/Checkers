package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import com.webcheckers.appl.Users;
import com.webcheckers.appl.Player;
import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {

  static final String USERS = "users";
  static final String SIGNEDIN = "signedin";
  static final String ONLY_ONE = "onlyOne";
  static final String PLAYERSERVICES_KEY = "playerServices";
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
  private final TemplateEngine templateEngine;
  private final Users users;
  private Player player;

  /**
   * Create the Spark Route (UI controller) for the {@code GET /} HTTP request.
   *
   * @param templateEngine the HTML template rendering engine
   * @param users the users currently in the game
   */
  public GetHomeRoute(final TemplateEngine templateEngine, Users users) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");

    Objects.requireNonNull(users, "users must not be null");
    //
    this.templateEngine = templateEngine;

    this.users = users;
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
    this.player = httpSession.attribute(PLAYERSERVICES_KEY);
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    if(httpSession.attribute("message") != null){
      vm.put("message", true);
      httpSession.removeAttribute("message");
    }

    if (this.player == null) {
      vm.put(SIGNEDIN, false);
      vm.put(USERS, users.getAllPlayers().size());
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    } else if (users.getAllPlayers().size() == 1) {
      vm.put(SIGNEDIN, true);
      vm.put(ONLY_ONE, true);
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    } else if (player.getBoard() != null) {
      response.redirect("/game");
      halt();
      return null;
    } else {
      vm.put(SIGNEDIN, true);
      vm.put(ONLY_ONE, false);
      vm.put(USERS, users.getAllPlayersExceptUser(player.getUsername()));
      return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }
  }

}
