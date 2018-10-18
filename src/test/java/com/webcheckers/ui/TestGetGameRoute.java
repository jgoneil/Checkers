package com.webcheckers.ui;

import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Users;

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
  private static final BoardView NO_BOARD = null;
  
  //friendly objects
  private Users users;
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

    users = new Users();

    CuT = new GetGameRoute(templateEngine, users);
  }

  @Test 
  void newGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer); 
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(NO_BOARD);
    Set<String> players = new HashSet<>();
    players.add(redPlayer.getName());
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
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
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
    BoardView boardView = new BoardView(users.getSpecificPlayer(redPlayer.getName()),
            users.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    redPlayer.setColor("red", boardView);
    whitePlayer.setColor("white", boardView);
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
  void reloadSameGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(whitePlayer);
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
    BoardView boardView = new BoardView(users.getSpecificPlayer(redPlayer.getName()),
            users.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    redPlayer.setColor("red", boardView);
    whitePlayer.setColor("white", boardView);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardView);
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
  void playerAlreadyInGame() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(lonelyPlayer);
    users.addPlayer(whitePlayer.getName());
    users.addPlayer(redPlayer.getName());
    BoardView boardView = new BoardView(users.getSpecificPlayer(redPlayer.getName()),
            users.getSpecificPlayer(whitePlayer.getName()), 8, "red");
    redPlayer.setColor("red", boardView);
    whitePlayer.setColor("white", boardView);
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
}
