package com.webcheckers.ui;

<<<<<<< HEAD
import com.webcheckers.appl.*;
=======
import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.PlayerBoardView;
import com.webcheckers.model.Player;
import com.webcheckers.appl.PlayerLobby;
>>>>>>> 341cb3a868ec2ee95b3286ce8c9f85e56d0f2a16

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Tag("UI-tier")
class TestGetGameRoute {

  private GetGameRoute CuT;
  private static final PlayerBoardView NO_LOBBY = null;
  
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer.getName());
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer.getName());
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer.getName());
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer.getName());
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
    testHelper.assertViewModelAttribute("whitePlayer", whitePlayer);
    testHelper.assertViewModelAttribute("activeColor", "RED");
  }

  @Test
  void reloadSameGameWhitePlayer() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer.getName());
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer.getName());
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
    try{
      CuT.handle(request, response);
    } catch(HaltException e) {
      assertNotNull(e);
    }
  }

  @Test 
  void playerAlreadyInGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(lonelyPlayer.getName());
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer.getName());
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer);
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
    BoardView boardView = new BoardView(users.getSpecificPlayer(redPlayer.getName()),
            users.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardView);
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece("red", new Space(7, 0, Space.Color.BLACK)));
    ModelBoard tempBoard = new ModelBoard(redPlayer, whitePlayer, 8, pieces);
    redPlayer.setColor("Red", boardView);
    redPlayer.addModelBoard(tempBoard);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(tempBoard);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer);
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
    BoardView boardView = new BoardView(users.getSpecificPlayer(redPlayer.getName()),
            users.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardView);
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece("red", new Space(7, 0, Space.Color.BLACK)));
    ModelBoard tempBoard = new ModelBoard(redPlayer, whitePlayer, 8, pieces);
    whitePlayer.setColor("White", boardView);
    whitePlayer.addModelBoard(tempBoard);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(tempBoard);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer);
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
    BoardView boardView = new BoardView(users.getSpecificPlayer(redPlayer.getName()),
            users.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardView);
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece("white", new Space(0, 1, Space.Color.BLACK)));
    ModelBoard tempBoard = new ModelBoard(redPlayer, whitePlayer, 8, pieces);
    whitePlayer.setColor("White", boardView);
    whitePlayer.addModelBoard(tempBoard);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(tempBoard);
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
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer);
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
    BoardView boardView = new BoardView(users.getSpecificPlayer(redPlayer.getName()),
            users.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardView);
    List<Piece> pieces = new ArrayList<>();
    pieces.add(new Piece("white", new Space(0, 1, Space.Color.BLACK)));
    ModelBoard tempBoard = new ModelBoard(redPlayer, whitePlayer, 8, pieces);
    redPlayer.setColor("Red", boardView);
    redPlayer.addModelBoard(tempBoard);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(tempBoard);
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
