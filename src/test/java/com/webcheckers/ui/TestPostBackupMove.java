package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.Message;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

public class TestPostBackupMove {

  //Instance of PostBackupMove
  PostBackupMove Cut;

  //Friendly
  Player redPlayer;
  Player whitePlayer;
  GameLobby gameLobby;
  Gson gson;

  //Mock Objects
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    templateEngine = mock(TemplateEngine.class);
    redPlayer = new Player("red");
    whitePlayer = new Player("white");
    gameLobby = new GameLobby(redPlayer, whitePlayer);
    when(request.session()).thenReturn(session);

    gson = new Gson();
    Cut = new PostBackupMove(gson);
  }

  @Test
  void moveMade() {
    gameLobby.madeMove(new Move(new Position(5, 0), new Position(6, 1)));
    gameLobby.setPendingMove(true);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object result = Cut.handle(request, response);
    if (result instanceof String) {
      String temp = (String) result;
      Message respondedMessage = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, respondedMessage.getType());
      assertEquals(PostBackupMove.SUCCESS_BACKUP_MOVE, respondedMessage.getText());
    }
  }

  @Test
  void moveNotMade() {
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object result = Cut.handle(request, response);
    if (result instanceof String) {
      String temp = (String) result;
      Message respondedMessage = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals(PostBackupMove.ERROR_BACKUP_MOVE, respondedMessage.getText());
    }
  }
}
