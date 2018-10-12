package com.webcheckers.ui;

import com.webcheckers.model.RecieveJson;
import spark.*;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.BoardView;
import com.webcheckers.model.CheckMove;
import static spark.Spark.halt;


/**
 * The UI Controller for handling the check to see if a move is valid or not
 */
public class PostMoveCheck implements Route {

  private final Gson gson;
  private Player player;
  private CheckMove checkMove;
  private final TemplateEngine templateEngine;

  private static final String MOVE = "move";

  /**
   * Main method for POST check of a valid move
   */
  public PostMoveCheck(final Gson gson, final TemplateEngine templateEngine) {

    Objects.requireNonNull(gson, "gson cannot be null");
    Objects.requireNonNull(templateEngine, "templateEngine is null");

    this.gson = gson;
    this.templateEngine= templateEngine;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /checkmove}
   *
   * @param request the HTTP request
   * @param request the HTTP response
   * @return the AJAX response for the player's ability to move the piece (true/false)
   */ 
  @Override
  public Object handle(Request request, Response response) {
    Session HTTPSession = request.session();
    
    this.player = HTTPSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

    String customJson = request.body();
    RecieveJson recieveJson = gson.fromJson(customJson, RecieveJson.class);

    Map<String, Object> vm  = new HashMap<>();
    vm.put("title", "Welcome!");

    BoardView boardView = player.getBoardView();

    vm.put("currentPlayer", player);
    vm.put("viewMode", "PLAY");
    vm.put("redPlayer", boardView.getRedPlayer());
    vm.put("whitePlayer", boardView.getWhitePlayer());
    if(boardView.redTurn()) {
      vm.put("activeColor", "RED");
    } else {
      vm.put("activeColor", "WHITE");
    }
    vm.put("boardView", boardView);

//    if(this.checkMove.validateMove(spaceStart, spaceEnd)) {
//      vm.put("message", new Message(Message.Type.normal, "Piece moved successfully."));
//    } else {
//      vm.put("message", new Message(Message.Type.error, "Piece cannot be moved."));
//    }

    return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW));
//
//    if (HTTPSession.attribute(MOVE) == null) {
//      this.checkMove = new CheckMove(player.getModelBoard());
//      HTTPSession.attribute(MOVE, checkMove);
//    } else {
//      this.checkMove = HTTPSession.attribute(MOVE);
//    }
//
//    BoardView boardView = this.player.getBoardView();
//    Row rowBeginning = boardView.getRow(rowStart);
//    Row rowEnding = boardView.getRow(rowEnd);
//    Space spaceStart = rowBeginning.getSpace(colStart);
//    Space spaceEnd = rowEnding.getSpace(colEnd);
//
//    if(this.checkMove.validateMove(spaceStart, spaceEnd)) {
//      HTTPSession.attribute("message", new Message(Message.Type.normal, "Piece moved successfully"));
//    } else {
//      HTTPSession.attribute("message", new Message(Message.Type.error, "Piece cannot be moved"));
//    }
//
//    response.redirect("/game");
//    halt();
//    return null;
  }
}
