package com.webcheckers.ui;

import com.webcheckers.model.BoardView;
import com.webcheckers.model.Player;
import com.google.gson.Gson;

import com.webcheckers.model.Message;
import com.webcheckers.model.ModelBoard;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

@Tag("UI-tier")
class TestPostResignRoute {

  private PostResignGame CuT;
  
  //friendly objects
  private Player playerOne;
  private Player playerTwo;
  private Gson gson = new Gson();
  private BoardView boardView;
  private ModelBoard modelBoard;
  
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
    playerOne = new Player("Ben");
    playerTwo = new Player("Steve");
    modelBoard = new ModelBoard(playerOne, playerTwo, 8);
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);
    
    CuT = new PostResignGame(gson);
  }

  @Test
  void playerAlreadyMovedPiece() {
    modelBoard.madeMove(new Move(new Position(5, 0), new Position(4, 1)));
    playerOne.addModelBoard(modelBoard);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerOne);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
    
    //Invoke Testing and checking result
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.error, message.getType());
      assertEquals(PostResignGame.ERROR_RESIGN, message.getText());
    } else {
      fail();
    }
  }

  @Test
  void otherPlayerResignedAndSessionNull() {
    ModelBoard modelBoard = new ModelBoard(playerOne, playerTwo, 8);
    playerOne.addModelBoard(modelBoard);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerOne);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and checking results
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, message.getType());
      assertEquals(PostResignGame.OTHER_PLAYER_RESIGN, message.getText());
    } else {
      fail();
    }
  }

  @Test
  void playerIsRedPlayer() {
    boardView = new BoardView(playerOne, playerTwo, 8, "red");
    playerOne.setColor("Red", boardView);
    ModelBoard modelBoard = new ModelBoard(playerOne, playerTwo, 8);
    playerOne.addModelBoard(modelBoard);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerOne);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardView);

    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and checking results
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, message.getType());
      assertEquals(PostResignGame.SUCCESS_RESIGN, message.getText());
    } else {
      fail();
    }
  }

  @Test
  void playerIsWhitePlayer() {
    boardView = new BoardView(playerTwo, playerOne, 8, "red");
    playerOne.setColor("White", boardView);
    ModelBoard modelBoard = new ModelBoard(playerOne, playerTwo, 8);
    playerOne.addModelBoard(modelBoard);
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerOne);
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardView);

    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    //Invoke Testing and checking results
    Object handleResponse = CuT.handle(request, response);
    if (handleResponse instanceof String) {
      String temp = (String) handleResponse;
      Message message = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, message.getType());
      assertEquals(PostResignGame.SUCCESS_RESIGN, message.getText());
    } else {
      fail();
    }
  }
}
