package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.google.gson.Gson;

import com.webcheckers.model.Message;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
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
  private Player playerTwo;
  private Gson gson = new Gson();
  private GameLobby gameLobby;
  private PlayerLobby playerLobby;

  //attributes holding mock objects (non-friendly)
  private Request request;
  private Response response;
  private TemplateEngine templateEngine;
  private Session session;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    playerOne = new Player("Ben");
    playerTwo = new Player("Steve");
    gameLobby = new GameLobby(playerOne, playerTwo);
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);
    playerLobby = new PlayerLobby();

    CuT = new PostResignGame(gson, playerLobby);
  }

  @Test
  void playerAlreadyMovedPiece() {
    gameLobby.pendingMove(new Move(new Position(5, 0), new Position(4, 1)));
    playerLobby.addPlayer("Ben");
    playerLobby.addPlayer("Steve");
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(playerOne.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and checking result
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.error, message.getType());
      assertEquals(PostResignGame.ERROR_RESIGN, message.getText());
    } else {
      fail();
    }
  }

  @Test
  void otherPlayerResignedAndSessionNull() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(playerOne.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and checking results
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, message.getType());
      assertEquals(PostResignGame.OTHER_PLAYER_RESIGN, message.getText());
    } else {
      fail();
    }
  }

  @Test
  void playerIsRedPlayer() {
    playerOne.setColor("Red");
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(playerOne.getName());
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    playerLobby.addPlayer(playerOne.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and checking results
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, message.getType());
      assertEquals(PostResignGame.SUCCESS_RESIGN, message.getText());
    } else {
      fail();
    }
  }

  @Test
  void playerIsWhitePlayer() {
    playerOne.setColor("White");
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(playerOne.getName());
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    playerLobby.addPlayer(playerOne.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and checking results
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, message.getType());
      assertEquals(PostResignGame.SUCCESS_RESIGN, message.getText());
    } else {
      fail();
    }
  }
}
