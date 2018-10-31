package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Player;
import com.webcheckers.appl.PlayerLobby;
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
    private static final GameLobby gamelobby = new GameLobby(LEGIT_PLAYER, new Player("Joe"));

    //Friendly Objects
    Player player1;
    private PlayerLobby users;

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

        users = new PlayerLobby();
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
        users.addPlayer(player1.getName());
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1.getName());
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);
    }

    @Test
    void redPlayerInGameSignOut(){
        player1 = LEGIT_PLAYER;
        users.addPlayer(LEGIT_PLAYER.getName());
        users.addPlayer(TEMP_USERNAME);
        users.getSpecificPlayer(LEGIT_PLAYER.getName()).setColor(GameLobby.RED);
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1.getName());
        when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gamelobby);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Object result  = CuT.handle(request,response);
        assertNull(result);
    }

    @Test
    void whitePlayerInGameSignOut(){
        player1 = LEGIT_PLAYER;
        users.addPlayer(LEGIT_PLAYER.getName());
        users.addPlayer(TEMP_USERNAME);
        users.getSpecificPlayer(LEGIT_PLAYER.getName()).setColor(GameLobby.WHITE);
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(player1.getName());
        when(request.session().attribute(GetGameRoute.GAMELOBBY)).thenReturn(gamelobby);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Object result = CuT.handle(request,response);
        assertNull(result);
    }
}
