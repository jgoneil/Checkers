package com.webcheckers.ui;

import com.webcheckers.appl.Player;
import com.webcheckers.appl.Users;
import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

@Tag("UI-tier")
class TestPostResignRoute {

  private PostResignGame CuT;
  
  //friendly objects
  private Player playerOne;
  
  //attributes holding mock objects (non-friendly)
  private Request request;
  private Response response;
  private TemplateEngine templateEngine;
  private Session session;
  private Gson gson;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    gson = mock(Gson.class);
    playerOne = new Player("Ben");
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);
    
    CuT = new PostResignGame(gson);
  }

  @Test
  void playerAlreadyMovedPiece() {
    playerOne.madeTurn();
    when(request.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerOne); 
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    
    //Invoke Testing
    CuT.handle(request, response);

    //Ensure Results
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("message", PostResignGame.ERROR_RESIGN);
  }

  @Test
  void otherPlayerResigned() {

  }

  @Test
  void playerIsRedPlayer() {

  }

  @Test
  void playerIsWhitePlayer() {

  }
}
