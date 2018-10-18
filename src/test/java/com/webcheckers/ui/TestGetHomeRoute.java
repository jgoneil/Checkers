package com.webcheckers.ui;

import com.webcheckers.appl.BoardView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

import com.webcheckers.appl.Player;
import com.webcheckers.appl.Users;
import java.util.logging.Logger;

@Tag("UI-tier")
class TestGetHomeRoute {

  private GetHomeRoute CuT;
  private static final Player NO_PLAYER = null;
  private static final Player LEGIT_PLAYER = new Player("bob");
  private static final Player LEGIT_PLAYER_NO_BOARD = new Player("steve");
  private static final String TEMP_USERNAME = "Joe";
  private static final String MESSAGE = "Testing";
  private static final BoardView BOARD = new BoardView(LEGIT_PLAYER, new Player("Joe"), 8, "red");

  //friendly objects
  private Player player1;
  private Users users;

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
    users = new Users();

    //Creating a unique CuT for each test
    CuT = new GetHomeRoute(templateEngine, users);
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
    users.addPlayer(TEMP_USERNAME);
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
    users.addPlayer(LEGIT_PLAYER.getName());
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
    users.addPlayer(LEGIT_PLAYER_NO_BOARD.getName());
    users.addPlayer(TEMP_USERNAME);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());


    CuT.handle(request, response);

    testHelper.assertViewModelExists();
    testHelper.assertViewModelIsaMap();

    testHelper.assertViewModelAttribute("title", "Welcome!");
    testHelper.assertViewModelAttribute(GetHomeRoute.SIGNEDIN, true);
    testHelper.assertViewModelAttribute(GetHomeRoute.USERS, users.getAllPlayersExceptUser(LEGIT_PLAYER_NO_BOARD.getName()));
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
    testHelper.assertViewModelAttribute("message", true);
  }

  @Test
  void redirect() {
    player1 = LEGIT_PLAYER;
    player1.setColor("red", BOARD);
    users.addPlayer(LEGIT_PLAYER.getName());
    users.addPlayer(TEMP_USERNAME);
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
