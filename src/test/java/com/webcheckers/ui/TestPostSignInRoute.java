package com.webcheckers.ui;


import com.webcheckers.model.Player;
import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static com.webcheckers.ui.PostSignInRoute.USER_PARAM;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for PostSignInRoute functionality
 */
public class TestPostSignInRoute {

  PostSignInRoute route;

  private Response response;
  private Request request;
  private Session session;
  private TemplateEngine templateEngine;

  private Player player;
  private PlayerLobby users;

  /**
   * Setup for tests
   */
  @BeforeEach
  void setup() {
    response = mock(Response.class);
    request = mock(Request.class);
    session = mock(Session.class);
    templateEngine = mock(TemplateEngine.class);
    users = new PlayerLobby();
    users.addPlayer("Example");
    when(request.session()).thenReturn(session);

    route = new PostSignInRoute(templateEngine, users);
  }

  /**
   * Test where username is null
   */
  @Test
  void nullUsernameTest() {
    when(request.queryParams(USER_PARAM)).thenReturn(null);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    try {
      route.handle(request, response);
    } catch (HaltException ex) {
      assertNotNull(ex);
    }
  }

  /**
   * Test where username is already in use
   */
  @Test
  void badUsernameTest() {
    when(request.queryParams(USER_PARAM)).thenReturn("Example");
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    assertNull(route.handle(request, response));
  }

  /**
   * Test where user should be added properly
   */
  @Test
  void goodTest() {
    when(request.queryParams(USER_PARAM)).thenReturn("NoName");
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    assertNull(route.handle(request, response));
  }
}