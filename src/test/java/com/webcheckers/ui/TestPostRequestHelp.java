package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import spark.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Main test class for POST /requestHelp route
 */
@Tag("UI-tier")
public class TestPostRequestHelp {

  PostRequestHelp CuT;

  //Friendly Objects
  private Gson gson = new Gson();
  private Player redplayer;
  private Player whitePlayer;
  private GameLobby gameLobby;
  private PlayerLobby playerLobby;

  //Non-friendly objects
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);
    playerLobby = new PlayerLobby();
    redplayer = new Player("a");
    whitePlayer = new Player("b");

    CuT = new PostRequestHelp(gson, playerLobby);
  }

  @Test
  void playerNotInLobby() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redplayer.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals("Current Player Signed Out", respondedMessage.getText());
    }
  }

  @Test
  void otherPlayerResigned() {
    playerLobby.addPlayer(redplayer.getName());
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redplayer.getName());
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(null);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals(PostResignGame.OTHER_PLAYER_RESIGN, respondedMessage.getText());
    }
  }

  @Test
  void whiteBestMove() {
    playerLobby.addPlayer(redplayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.getSpecificPlayer(redplayer.getName()).setColor(GameLobby.RED);
    playerLobby.getSpecificPlayer(whitePlayer.getName()).setColor(GameLobby.WHITE);
    List<Piece> pieceList = new ArrayList<>();
    pieceList.add(new Piece(GameLobby.WHITE, new Space(0, 7, Space.Color.BLACK)));
    gameLobby = new GameLobby(redplayer, whitePlayer, pieceList);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer.getName());
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Move move = gson.fromJson(temporaryInfo, Move.class);
      assertEquals(1, move.getEnd().getRow());
      assertEquals(6, move.getEnd().getCell());
    }
  }

  @Test
  void redBestMove() {
    playerLobby.addPlayer(redplayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.getSpecificPlayer(redplayer.getName()).setColor(GameLobby.RED);
    playerLobby.getSpecificPlayer(whitePlayer.getName()).setColor(GameLobby.WHITE);
    List<Piece> pieceList = new ArrayList<>();
    pieceList.add(new Piece(GameLobby.RED, new Space(7, 0, Space.Color.BLACK)));
    gameLobby = new GameLobby(redplayer, whitePlayer, pieceList);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redplayer.getName());
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Move move = gson.fromJson(temporaryInfo, Move.class);
      assertEquals(6, move.getEnd().getRow());
      assertEquals(1, move.getEnd().getCell());
    }
  }
}
