package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.PlayerBoardView;
import com.webcheckers.model.Player;
import com.webcheckers.model.Message;
import com.webcheckers.model.ModelBoard;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for submitting a turn
 */
public class TestPostSubmitTurn {

  PostSubmitTurn CuT;

  //friendly objects
  private Gson gson = new Gson();
  private Player redPlayer;
  private Player whitePlayer;

  //attributes holding mock objects (non-friendly)
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;

  @BeforeEach
  void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    redPlayer = new Player("a");
    whitePlayer = new Player("b");
    PlayerBoardView playerBoardViewRed = new PlayerBoardView(redPlayer, whitePlayer, 8, "Red");
    PlayerBoardView playerBoardViewWhite = new PlayerBoardView(redPlayer, whitePlayer, 8, "White");
    redPlayer.setColor("Red", playerBoardViewRed);
    whitePlayer.setColor("White", playerBoardViewWhite);
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);

    CuT = new PostSubmitTurn(gson);
  }

  @Test
  void successfulSubmit() {
    ModelBoard modelBoard = new ModelBoard(redPlayer, whitePlayer, 8);
    modelBoard.madeMove(new Move(new Position(5, 0), new Position(6, 1)));
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(modelBoard);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.info, respondedMessage.getType());
      assertEquals(PostSubmitTurn.SUCCESS_SUBMIT_TURN, respondedMessage.getText());
    }
  }

  @Test
  void unsuccessfulSubmit() {
    ModelBoard modelBoard = new ModelBoard(redPlayer, whitePlayer, 8);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(modelBoard);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);
    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals(PostSubmitTurn.ERROR_SUBMIT_TURN, respondedMessage.getText());
    }
  }
}
