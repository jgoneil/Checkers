package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Player;
import com.webcheckers.model.Message;
import com.webcheckers.model.ModelBoard;
import com.webcheckers.model.Move;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class TestPostMoveCheck {

  PostMoveCheck CuT;

  //friendly objects
  private Gson gson = new Gson();
  private Player redPlayer;
  private Player whitePlayer;
  private Player notInGame;
  private ModelBoard board;
  private Position position34;
  private Position position45;

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
    notInGame = new Player("c");
    redPlayer = new Player("a");
    whitePlayer = new Player("b");
    BoardView boardViewRed = new BoardView(redPlayer, whitePlayer, 8, "Red");
    BoardView boardViewWhite = new BoardView(redPlayer, whitePlayer, 8, "White");
    board = new ModelBoard(redPlayer, whitePlayer, 8);
    redPlayer.setColor("Red", boardViewRed);
    whitePlayer.setColor("White", boardViewWhite);
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);
    position34 = new Position(4, 3);
    position45 = new Position(5, 4);

    CuT = new PostMoveCheck(gson);
    }

  @Test
  void redNotInGameMove() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(notInGame);
    BoardView whiteBoardView = whitePlayer.getBoardView();
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(whiteBoardView);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);

    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals(PostResignGame.OTHER_PLAYER_RESIGN, respondedMessage.getText());
    }
  }

  @Test
  void whiteNotInGameMove() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(notInGame);
    BoardView boardViewRed = new BoardView(notInGame, redPlayer, 8, "Red");
    when(request.session().attribute(GetGameRoute.BOARD)).thenReturn(boardViewRed);
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);

    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals(PostResignGame.OTHER_PLAYER_RESIGN, respondedMessage.getText());
    }
  }

  @Test
  void madeMove() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer);
    board.madeMove(new Move(position34, position45));
    board.setPendingMove(true);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(board);
    when(request.body()).thenReturn(gson.toJson(new Move(position34, new Position(0,3))));
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);

    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals("Can only do one move per turn", respondedMessage.getText());
    }
  }

  @Test
  void MakingMoveForward() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(board);
    when(request.body()).thenReturn(gson.toJson(new Move(position45, position34)));
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);

    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.info, respondedMessage.getType());
      assertEquals("This move is valid.", respondedMessage.getText());
    }
  }

  @Test
  void MakingWrongMove() {
    when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(redPlayer);
    when(request.session().attribute(GetGameRoute.MODEL_BOARD)).thenReturn(board);
    when(request.body()).thenReturn(gson.toJson(new Move(position34, position45)));
    final TemplateEngineTester testHelper = new TemplateEngineTester();
    when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

    Object info = CuT.handle(request, response);

    if (info instanceof String) {
      String temporaryInfo = (String) info;
      Message respondedMessage = gson.fromJson(temporaryInfo, Message.class);
      assertEquals(Message.Type.error, respondedMessage.getType());
      assertEquals("Attempted to move a piece to an already occupied space", respondedMessage.getText());
    }
  }
}
