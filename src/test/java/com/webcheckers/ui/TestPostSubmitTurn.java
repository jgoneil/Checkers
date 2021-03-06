package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for submitting a turn
 */
public class TestPostSubmitTurn {

  PostSubmitTurn CuT;

  //friendly objects
  private Gson gson = new Gson();
  private Player redPlayer;
  private Player whitePlayer;

  //attributes holding mock objects (non-friendly)
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;
  private GameLobby gameLobby;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    redPlayer = new Player("a");
    whitePlayer = new Player("b");
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);
    gameLobby = new GameLobby(redPlayer, whitePlayer);
    CuT = new PostSubmitTurn(gson);
  }

  @Test
  void successfulSubmitRed() {
    gameLobby.pendingMove(new Move(new Position(5, 0), new Position(6, 1)));
    gameLobby.setPendingMove(true);
    gameLobby.setCanSubmit(true);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.info, respondedMessage.getType());
      assertEquals(PostSubmitTurn.SUCCESS_SUBMIT_TURN, respondedMessage.getText());
    }
  }

  @Test
  void successfulSubmitWhite() {
    gameLobby.changeTurn();
    gameLobby.pendingMove(new Move(new Position(5, 0), new Position(6, 1)));
    gameLobby.setPendingMove(true);
    gameLobby.setCanSubmit(true);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.info, respondedMessage.getType());
      assertEquals(PostSubmitTurn.SUCCESS_SUBMIT_TURN, respondedMessage.getText());
    }
  }

  @Test
  void unsuccessfulSubmitJump() {
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece(GameLobby.RED, new Space(7, 0, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(6, 1, Space.Color.BLACK)));
    pieces.add(new Piece(GameLobby.WHITE, new Space(4, 1, Space.Color.BLACK)));

    GameLobby fixedGameLobby = new GameLobby(redPlayer, whitePlayer, pieces);
    fixedGameLobby.pendingMove(new Move(new Position(7, 0), new Position(5, 2)));
    gameLobby.setPendingMove(true);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(fixedGameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals("Multi-Jump possible. Cannot submit", respondedMessage.getText());
    }
  }

  @Test
  void unsuccessfulSubmit() {
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals(PostSubmitTurn.ERROR_SUBMIT_TURN, respondedMessage.getText());
    }
  }
}
