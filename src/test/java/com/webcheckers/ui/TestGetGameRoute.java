package com.webcheckers.ui;

import com.webcheckers.model.PlayerBoardView;
import com.webcheckers.model.Player;
import com.webcheckers.appl.PlayerLobby;

import com.webcheckers.model.ModelBoard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

import java.util.HashSet;
import java.util.Set;

@Tag("UI-tier")
class TestGetGameRoute {

  private GetGameRoute CuT;
  private static final PlayerBoardView NO_BOARD = null;
  
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer); 
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(NO_BOARD);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer);
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    PlayerBoardView playerBoardView = new PlayerBoardView(playerLobby.getSpecificPlayer(redPlayer.getName()),
            playerLobby.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    redPlayer.setColor("red", playerBoardView);
    whitePlayer.setColor("white", playerBoardView);
    ModelBoard modelBoard = new ModelBoard(redPlayer, whitePlayer, 8);
    whitePlayer.addModelBoard(modelBoard);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(NO_BOARD);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer);
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    PlayerBoardView playerBoardView = new PlayerBoardView(playerLobby.getSpecificPlayer(redPlayer.getName()),
            playerLobby.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    redPlayer.setColor("red", playerBoardView);
    whitePlayer.setColor("white", playerBoardView);
    ModelBoard modelBoard = new ModelBoard(redPlayer, whitePlayer, 8);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(modelBoard);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(playerBoardView);
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
  void reloadSameGameWhitePlayer() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer);
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    PlayerBoardView playerBoardView = new PlayerBoardView(playerLobby.getSpecificPlayer(redPlayer.getName()),
            playerLobby.getSpecificPlayer(whitePlayer.getName()), 8, "white");
    redPlayer.setColor("red", playerBoardView);
    whitePlayer.setColor("white", playerBoardView);
    ModelBoard modelBoard = new ModelBoard(redPlayer, whitePlayer, 8);
    modelBoard.setMove(true);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(modelBoard);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(playerBoardView);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer);
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    PlayerBoardView playerBoardView = new PlayerBoardView(playerLobby.getSpecificPlayer(redPlayer.getName()),
            playerLobby.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    when(request.queryParams()).thenReturn(players);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(playerBoardView);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoking Test
    try{
      CuT.handle(request, response);
    } catch(HaltException e) {
      assertNotNull(e);
    }
  }

  @Test 
  void playerAlreadyInGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(lonelyPlayer);
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    PlayerBoardView playerBoardView = new PlayerBoardView(playerLobby.getSpecificPlayer(redPlayer.getName()),
            playerLobby.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    redPlayer.setColor("red", playerBoardView);
    whitePlayer.setColor("white", playerBoardView);
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    when(request.queryParams()).thenReturn(players);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(NO_BOARD);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer);
    playerLobby.addPlayer(whitePlayer.getName());
    playerLobby.addPlayer(redPlayer.getName());
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    when(request.queryParams()).thenReturn(players);
    when(request.session().attribute(PostResignGame.RESIGNED_PLAYER)).thenReturn(redPlayer);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(NO_BOARD);
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
