package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Player;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostTurnCheckTest {

  PostTurnCheck CuT;

  //friendly objects//
  private Gson gson = new Gson();
  private Player redPlayer;
  private Player whitePlayer;

  //non-friendly objects//
  private Request request;
  private Response response;
  private Session session;
  private TemplateEngine templateEngine;


  @BeforeEach
  void setUp() {
    request = mock(Request.class);
    response = mock(Response.class);
    session = mock(Session.class);
    whitePlayer = new Player("two");
    redPlayer = new Player("one");
    BoardView redView = new BoardView(redPlayer, whitePlayer, 8, "Red");
    BoardView whiteView = new BoardView(redPlayer, whitePlayer, 8, "White");
    redPlayer.setColor("Red", redView);
    whitePlayer.setColor("White", whiteView);
    when(request.session()).thenReturn(session);
    templateEngine = mock(TemplateEngine.class);

    CuT = new PostTurnCheck(gson);
  }
}