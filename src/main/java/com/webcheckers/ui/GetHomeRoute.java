package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;
import com.webcheckers.appl.Users;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  static final String USERS = "users";
  static final String USER_EXIST = "users_exist";

  private final TemplateEngine templateEngine;
  private final Users users;

  /**
   * Create the Spark Route (UI controller) for the
   * {@code GET /} HTTP request.
   *
   * @param templateEngine
   *   the HTML template rendering engine
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
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    if(users.getAllPlayers().length != 0){
      vm.put(USER_EXIST, true);
      vm.put(USERS, users.getAllPlayers());
    }
    else{
      vm.put(USER_EXIST, false);
    }
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }

}
