package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.PlayerBoard;
import com.webcheckers.model.Player;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Space;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Tag("UI-tier")
class TestGetGameRoute {

  private static final PlayerBoard NO_LOBBY = null;
  private GetGameRoute CuT;
  //friendly objects
  private PlayerLobby playerLobby;
  private Player redPlayer;
  private Player whitePlayer;
  private Player lonelyPlayer;

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
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);

    redPlayer = new Player("nameA");
    whitePlayer = new Player("nameB");
    lonelyPlayer = new Player("nameC");

    playerLobby = new PlayerLobby();

    CuT = new GetGameRoute(templateEngine, playerLobby);
  }

  @Test
  void notInGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(redPlayer.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and Ensure Redirect
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }

  @Test
  void newGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(redPlayer.getName());
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(NO_LOBBY);
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    when(request.queryParams()).thenReturn(players);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    CuT.handle(request, response);

    //Ensure results
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute("currentPlayer", redPlayer);
    testHelper.assertViewModelAttribute("viewMode", "PLAY");
    testHelper.assertViewModelAttribute("redPlayer", redPlayer);
    testHelper.assertViewModelAttribute("activeColor", "RED");
  }

  @Test
  void secondPlayerConnect() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(whitePlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    GameLobby gameLobby = new GameLobby(playerLobby.getSpecificPlayer(redPlayer.getName()),
        playerLobby.getSpecificPlayer(whitePlayer.getName()));
    CuT.setGameLobby(gameLobby);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(NO_LOBBY);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    CuT.handle(request, response);

    //Ensure results
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute("viewMode", "PLAY");
    testHelper.assertViewModelAttribute("whitePlayer", whitePlayer);
    testHelper.assertViewModelAttribute("activeColor", "RED");
  }

  @Test
  void reloadSameGameRedPlayer() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(redPlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    GameLobby gameLobby = new GameLobby(playerLobby.getSpecificPlayer(redPlayer.getName()),
        playerLobby.getSpecificPlayer(whitePlayer.getName()));

    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    CuT.handle(request, response);

    //Ensure results
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute("viewMode", "PLAY");
    testHelper.assertViewModelAttribute("redPlayer", redPlayer);
    testHelper.assertViewModelAttribute("activeColor", "RED");
  }

  @Test
  void reloadSameGameWhitePlayer() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(whitePlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    GameLobby gameLobby = new GameLobby(playerLobby.getSpecificPlayer(redPlayer.getName()),
        playerLobby.getSpecificPlayer(whitePlayer.getName()));
    gameLobby.setMove(true);

    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    CuT.handle(request, response);

    //Ensure results
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute("viewMode", "PLAY");
    testHelper.assertViewModelAttribute("whitePlayer", whitePlayer);
    testHelper.assertViewModelAttribute("activeColor", "WHITE");
  }

  @Test
  void otherPlayerResigned() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(redPlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    GameLobby gameLobby = new GameLobby(redPlayer, whitePlayer);
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    when(request.queryParams()).thenReturn(players);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoking Test
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }

  @Test
  void playerAlreadyInGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(lonelyPlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    GameLobby gameLobby = new GameLobby(redPlayer, whitePlayer);
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    when(request.queryParams()).thenReturn(players);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }

  @Test
  void playerResigned() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(whitePlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    when(request.queryParams()).thenReturn(players);
    when(request.session().attribute(PostResignGame.RESIGNED_PLAYER)).thenReturn(redPlayer);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(NO_LOBBY);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }

  @Test
  void redPlayerWon() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(redPlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece(GameLobby.RED, new Space(7, 0, Space.Color.BLACK)));
    GameLobby gameLobby = new GameLobby(playerLobby.getSpecificPlayer(redPlayer.getName()),
        playerLobby.getSpecificPlayer(whitePlayer.getName()), pieces);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }

  @Test
  void whitePlayerLost() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(whitePlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece(GameLobby.RED, new Space(7, 0, Space.Color.BLACK)));
    GameLobby gameLobby = new GameLobby(playerLobby.getSpecificPlayer(redPlayer.getName()),
        playerLobby.getSpecificPlayer(whitePlayer.getName()), pieces);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }

  @Test
  void whitePlayerWin() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(whitePlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece(GameLobby.WHITE, new Space(0, 1, Space.Color.BLACK)));
    GameLobby gameLobby = new GameLobby(playerLobby.getSpecificPlayer(redPlayer.getName()),
        playerLobby.getSpecificPlayer(whitePlayer.getName()), pieces);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }

  @Test
  void redPlayerLost() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY))
        .thenReturn(redPlayer.getName());
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece(GameLobby.WHITE, new Space(0, 1, Space.Color.BLACK)));
    GameLobby gameLobby = new GameLobby(playerLobby.getSpecificPlayer(redPlayer.getName()),
        playerLobby.getSpecificPlayer(whitePlayer.getName()), pieces);
    when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    try {
      CuT.handle(request, response);
    } catch (HaltException e) {
      assertNotNull(e);
    }
  }
}
