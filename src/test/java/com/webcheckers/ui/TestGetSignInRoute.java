package com.webcheckers.ui;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.logging.Logger;

import spark.*;

public class TestGetSignInRoute {

  //Instance of GetSigninRoute
  GetSigninRoute CuT;
  //Friendly Objects

  //Mock Objects
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;
  private Logger logger;

  @BeforeEach
  void setUp() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);
    logger = Logger.getLogger(GetSigninRoute.class.getName());
    templateEngine = mock(TemplateEngine.class);


    CuT = new GetSigninRoute(templateEngine);

  }

  @Test
  void loggerTest() {assertNotNull(logger);}

  @Test
  void signIn(){
    when(request.session().attribute(GetSigninRoute.VIEW_NAME)).thenReturn(null);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    CuT.handle(request, response);

    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute("header", "Please Sign In Below.");
    testHelper.assertViewModelAttribute("attemptFailed", false);
  }
}
