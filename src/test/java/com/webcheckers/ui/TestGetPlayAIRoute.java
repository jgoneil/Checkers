package com.webcheckers.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.PlayerBoard;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

@Tag("UI-tier")
public class TestGetPlayAIRoute {

  private GetPlayAIRoute CuT;

  //friendly objects
  private PlayerLobby playerLobby;
  private Player redPlayer;

  //attributes holding mock objects (non-friendly)
  private Request request;
  private Response response;
  private Session session;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);

    redPlayer = new Player("nameA");


    playerLobby = new PlayerLobby();


    CuT = new GetPlayAIRoute(playerLobby);
  }

  @Test
  void newAIGame(){
    final TemplateEngineTester testHelper = new TemplateEngineTester();

    //Invoke Testing
    CuT.handle(request, response);
    assertNotNull(playerLobby.getSpecificPlayer("F@ke 1"));

  }
}
