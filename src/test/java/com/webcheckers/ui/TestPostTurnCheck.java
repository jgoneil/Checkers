package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests to make sure that PostTurnCheck is working as intended
 */
public class TestPostTurnCheck {
  //Instance of PostTurnCheck
  PostTurnCheck check;
  //Created friendly objects
  private Gson gson = new Gson();
  private Player goodRedPlayer;
  private Player goodWhitePlayer;
  private Player badPlayer;
  private GameLobby gameLobby;
  //Mock objects
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;

    /**
     * Sets up each attribute as needed
     */
  @BeforeEach
  void setup(){
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    templateEngine = mock(TemplateEngine.class);
    goodRedPlayer = new Player("Red");
    goodWhitePlayer = new Player("White");
    badPlayer = new Player("Bad");
    gameLobby = new GameLobby(goodRedPlayer, goodWhitePlayer);
    when(request.session()).thenReturn(session);
    when(session.attribute(GetGameRoute.GAMELOBBY)).thenReturn(gameLobby);
    check = new PostTurnCheck(gson);
  }

  /**
   * Tests if a Player with a null ModelBoard is handled properly
   */
  @Test
  void nullPlayerTest(){
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(badPlayer.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    Object result = check.handle(request, response);
    if(result instanceof String){
      String temp = (String) result;
      Message fakeResponse = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, fakeResponse.getType());
      assertEquals("true", fakeResponse.getText());
    }
  }

  /**
  * Tests instance of Red Player on the Red Turn
  */
  @Test
  void redsTurnTest(){
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(goodRedPlayer.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    Object result = check.handle(request, response);
    if(result instanceof String){
      String temp = (String) result;
      Message fakeResponse = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, fakeResponse.getType());
      assertEquals("true", fakeResponse.getText());
    }
  }

  /**
   * Tests instance of the White player on the Red turn
   */
  @Test
  void notWhitesTurnTest(){
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(goodWhitePlayer.getName());
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    Object result = check.handle(request, response);
    if(result instanceof String){
      String temp = (String) result;
      Message fakeResponse = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, fakeResponse.getType());
      assertEquals("false", fakeResponse.getText());
    }
  }

  /**
   *  Tests the instance of the Red player on the White turn
   */
  @Test
  void notRedsTurnTest(){
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(goodRedPlayer.getName());
    gameLobby.setMove(true);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    Object result = check.handle(request, response);
    if(result instanceof String){
      String temp = (String) result;
      Message fakeResponse = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, fakeResponse.getType());
      assertEquals("false", fakeResponse.getText());
    }
  }

  /**
   * Tests the instance of the White player on the White turn
   */
  @Test
  void whitesTurnTest(){
    when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(goodWhitePlayer.getName());
    gameLobby.setMove(true);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    Object result = check.handle(request, response);
    if(result instanceof String){
      String temp = (String) result;
      Message fakeResponse = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, fakeResponse.getType());
      assertEquals("true", fakeResponse.getText());
    }
  }
}
