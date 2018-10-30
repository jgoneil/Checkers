package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Player;
import com.webcheckers.model.Message;
import com.webcheckers.model.ModelBoard;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import spark.*;

public class TestPostBackupMove {

  //Instance of PostBackupMove
  PostBackupMove Cut;

  //Friendly
  Player redPlayer;
  Player whitePlayer;
  ModelBoard modelBoard;
  Gson gson;

  //Mock Objects
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    templateEngine = mock(TemplateEngine.class);
    redPlayer = new Player("red");
    whitePlayer = new Player("white");
    BoardView rBoard = new BoardView(redPlayer, whitePlayer,8, "Red");
    BoardView wBoard = new BoardView(redPlayer, whitePlayer, 8, "White");
    redPlayer.setColor("Red", rBoard);
    whitePlayer.setColor("White", wBoard);
    when(request.session()).thenReturn(session);

    gson = new Gson();
    Cut = new PostBackupMove(gson);
  }

  @Test
  void moveMade() {
    ModelBoard modelBoard = new ModelBoard(redPlayer, whitePlayer, 8);
    modelBoard.madeMove(new Move(new Position(5, 0), new Position(6, 1)));
    modelBoard.setPendingMove(true);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(modelBoard);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object result = Cut.handle(request, response);
    if (result instanceof String) {
      String temp = (String) result;
      Message respondedMessage = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.info, respondedMessage.getType());
      assertEquals(PostBackupMove.SUCCESS_BACKUP_MOVE, respondedMessage.getText());
    }
  }

  @Test
  void moveNotMade() {
    ModelBoard modelBoard = new ModelBoard(redPlayer, whitePlayer, 8);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(modelBoard);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object result = Cut.handle(request, response);
    if (result instanceof String) {
      String temp = (String) result;
      Message respondedMessage = gson.fromJson(temp, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals(PostBackupMove.ERROR_BACKUP_MOVE, respondedMessage.getText());
    }
  }
}
