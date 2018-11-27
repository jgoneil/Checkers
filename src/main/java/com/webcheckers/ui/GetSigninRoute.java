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

/**
 * {@code get /signin} Class that controls main connection to signin page. Completes error checking
 * based on if the session already has a player attribute or not
 */
public class GetSigninRoute implements Route {

  //Static final variables (Constants)
  static final String TITLE = "Welcome!";
  static final String HEADER = "Please Sign In Below.";
  static final String VIEW_NAME = "signin.ftl";
  static final String ATTEMPT_FAILED = "attemptFailed";

  //Logger for recording events
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  //HTML template loader for the freemarker pages
  private TemplateEngine templateEngine;

  /**
   * Constructor for class
   *
   * @param templateEngine the template in which all messages for the system are handled between
   * spark and the java backend
   */
  public GetSigninRoute(final TemplateEngine templateEngine) {
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");

    this.templateEngine = templateEngine;
  }

  /**
   * Handle method for signin connections Preforms error handling for users already signed into the
   * system (need to implement)
   *
   * @param request the information being passed from the frontend
   * @param response the information being sent from the backend (this class)`
   */
  @Override
  public String handle(Request request, Response response) {
    LOG.finer("GetSigninRoute is invoked.");

    final Map<String, Object> vm = new HashMap<>();
    vm.put("title", TITLE);
    vm.put("header", HEADER);
    vm.put(ATTEMPT_FAILED, false);
    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
  }
}
