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
import static spark.Spark.halt;

import com.webcheckers.appl.Users;

/*
 * This is {@code POST /signin } route handler. Handles user signin.
 * Checks user input to ensure the username is not already in use
 */
public class PostSignInRoute implements Route {
  
  static final String VIEW_NAME = "home.ftl";
  static final String REVERT_VIEW = "sigin.ftl";
  static final String USER_PARAM = "username";
  static final String USERNAME_ERROR = "invalid username";
  static final String RETRY = "Please try again";

  private final TemplateEngine templateEngine;
  private final Users users;

  public PostSignInRoute(final TemplateEngine templateEngine, final Users users) {

    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    Objects.requireNonNull(users, "users must not be null");

    this.templateEngine = templateEngine;
    this.users = users;
  }

  @Override
  public String handle(Request request, Response response) {
    final Session httpSession = request.session();
    final Map<String, Object> vm = new HashMap<>();
    final String username = request.queryParams(USER_PARAM);
  
    ModelAndView mv;

    if(users.addPlayer(username)){
      vm.put(GetHomeRoute.USERS, users);
      mv = new ModelAndView(vm, VIEW_NAME);
      return templateEngine.render(mv);
    } else {
      vm.put(USERNAME_ERROR, RETRY);
      mv = new ModelAndView(vm, REVERT_VIEW);  
      return templateEngine.render(mv);
    }
  }
}
