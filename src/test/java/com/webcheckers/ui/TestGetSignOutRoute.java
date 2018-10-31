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

import java.util.logging.Logger;

import spark.*;

@Tag("UI-tier")
public class TestGetSignOutRoute {

    //Instance of GetSignOutRoute
    private GetSignOutRoute CuT;
    private static final Player NO_PLAYER = null;
    private static final Player LEGIT_PLAYER = new Player("bob");
    private static final String TEMP_USERNAME = "Joe";
    private static final BoardView BOARD = new BoardView(LEGIT_PLAYER, new Player("Joe"), 8, "red");

    //Friendly Objects
    Player player1;
    private Users users;

    //Mock Objects
    private Request request;
    private Response response;
    private Session session;
    private TemplateEngine templateEngine;
    private Logger logger;

    @BeforeEach
    void setUp() {
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        logger = Logger.getLogger(GetSignOutRoute.class.getName());
        templateEngine = mock(TemplateEngine.class);

        users = new Users();
        CuT = new GetSignOutRoute(templateEngine,users);
    }

    @Test
    void loggerTest() {assertNotNull(logger);}

    @Test
    void noPlayerSignOut() {
        player1 = NO_PLAYER;
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        try {
            CuT.handle(request, response);
        } catch (spark.HaltException e) {
            assertNotNull(e);
        }
    }

    @Test
    void playerNotInGameSignOut() {
        player1 = LEGIT_PLAYER;
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);
    }

    @Test
    void redPlayerInGameSignOut(){
        player1 = LEGIT_PLAYER;
        player1.setColor("Red",BOARD);
        users.addPlayer(LEGIT_PLAYER.getName());
        users.addPlayer(TEMP_USERNAME);
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
        when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(BOARD);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request,response);
    }

    @Test
    void whitePlayerInGameSignOut(){
        player1 = LEGIT_PLAYER;
        player1.setColor("White",BOARD);
        users.addPlayer(LEGIT_PLAYER.getName());
        users.addPlayer(TEMP_USERNAME);
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1);
        when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(BOARD);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request,response);
    }
}
