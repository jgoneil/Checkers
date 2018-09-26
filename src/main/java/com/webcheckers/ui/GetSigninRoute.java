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

public class GetSigninRoute implements Route {

  static final String TITLE = "Welcome! Please Sign In Below";
  static final String VIEW_NAME = "signin_form.ftl";
  
  private TemplateEngine templateEngine;
  
  public GetSigninRoute(final TemplateEngine templateEngine){
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");

    this.templateEngine = templateEngine;
  }

  @Override
  public String handle(Request request, Response response) {
    final Session httpSession = request.session();

    final Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE, TITLE);
    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
  }
}
