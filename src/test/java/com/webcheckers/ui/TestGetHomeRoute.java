package com.webcheckers.ui;

import com.webcheckers.model.PlayerBoardView;
import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

import com.webcheckers.model.Player;

import java.util.logging.Logger;

@Tag("UI-tier")
class TestGetHomeRoute {

  private GetHomeRoute CuT;
  private static final Player NO_PLAYER = null;
  private static final Player LEGIT_PLAYER = new Player("bob");
  private static final Player LEGIT_PLAYER_NO_BOARD = new Player("steve");
  private static final String TEMP_USERNAME = "Joe";
  private static final String MESSAGE = "Testing";
  private static final PlayerBoardView BOARD = new PlayerBoardView(LEGIT_PLAYER, new Player("Joe"), 8, "red");

  //friendly objects
  private Player player1;
  private PlayerLobby playerLobby;

  // attributes holding mock objects (non-friendly)
  private Request request;
  private Session session;
  private TemplateEngine templateEngine;
  private Response response;
  private Logger logger;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);

    logger = Logger.getLogger(GetHomeRoute.class.getName());
    playerLobby = new PlayerLobby();

    //Creating a unique CuT for each test
    CuT = new GetHomeRoute(templateEngine, playerLobby);
  }

  @Test
  void checkLogger() {
    assertNotNull(logger);
  }

  @Test
  void noPlayerNoSignIn() {
    player1 = NO_PLAYER;
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing
    CuT.handle(request, response);

    //Ensure results
    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute(GetHomeRoute.SIGNEDIN, false);
    testHelper.assertViewModelAttribute(GetHomeRoute.USERS, 0);
  }

  @Test
  void onePlayerNoSignIn() {
    player1 = NO_PLAYER;
    playerLobby.addPlayer(TEMP_USERNAME);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());


    CuT.handle(request, response);

    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute(GetHomeRoute.SIGNEDIN, false);
    testHelper.assertViewModelAttribute(GetHomeRoute.USERS, 1);
  }

  @Test
  void noPlayerSignedIn() {
    player1 = LEGIT_PLAYER;
    playerLobby.addPlayer(LEGIT_PLAYER.getName());
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());


    CuT.handle(request, response);

    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute(GetHomeRoute.SIGNEDIN, true);
    testHelper.assertViewModelAttribute(GetHomeRoute.ONLY_ONE, true);
  }

  @Test
  void onePlayerSignedIn() {
    player1 = LEGIT_PLAYER_NO_BOARD;
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
    playerLobby.addPlayer(LEGIT_PLAYER_NO_BOARD.getName());
    playerLobby.addPlayer(TEMP_USERNAME);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());


    CuT.handle(request, response);

    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute(GetHomeRoute.SIGNEDIN, true);
    testHelper.assertViewModelAttribute(GetHomeRoute.USERS, playerLobby.getAllPlayersExceptUser(LEGIT_PLAYER_NO_BOARD.getName()));
  }

  @Test
  void messageTest() {
    when(request.session().attribute("message")).thenReturn(MESSAGE);
    player1 = NO_PLAYER;
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    CuT.handle(request, response);

    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute(GetHomeRoute.SIGNEDIN, false);
    testHelper.assertViewModelAttribute("message", MESSAGE);
  }

  @Test
  void redirect() {
    player1 = LEGIT_PLAYER;
    player1.setColor("red", BOARD);
    playerLobby.addPlayer(LEGIT_PLAYER.getName());
    playerLobby.addPlayer(TEMP_USERNAME);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);

    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    try {
      CuT.handle(request, response);
    } catch (spark.HaltException e){
      assertNotNull(e);
    }
  }
}
