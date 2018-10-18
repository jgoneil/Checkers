package com.webcheckers.ui;

import com.webcheckers.model.Message;
import com.webcheckers.model.ModelBoard;
import com.webcheckers.model.Move;
import spark.*;

import java.util.Objects;
import java.util.Map;

import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import com.webcheckers.model.CheckMove;


/**
 * The UI Controller for handling the check to see if a move is valid or not
 */
public class PostMoveCheck implements Route {

  private static final String MOVE = "move";
  private static final String HAS_MOVED = "has_moved";
  private final Gson gson;
  private Player player;
  private CheckMove checkMove;

  /**
   * Main method for POST check of a valid move
   * @param gson the gson parser for JQuery Requests/Responses
   */
  public PostMoveCheck(final Gson gson) {

    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
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

    if (this.checkMove == null) {
      ModelBoard board = HTTPSession.attribute(GetGameRoute.MODEL_BOARD);
      this.checkMove = new CheckMove(board);
    }

    String customJson = request.body();
    Move move = gson.fromJson(customJson, Move.class);


    if (!player.getHasMoved()) {
      Map<Boolean, String> resultFromCheck = this.checkMove
          .validateMove(move.getStart(), move.getEnd());
      if (resultFromCheck.containsKey(true)) {
        player.setHasMoved(true);
        Message message = new Message(Message.Type.info, resultFromCheck.get(true));
        return gson.toJson(message);
      } else {
        Message message = new Message(Message.Type.error, resultFromCheck.get(false));
        return gson.toJson(message);
      }
    } else {
      Message message = new Message(Message.Type.error, "Can only do one move per turn");
      return gson.toJson(message);
    }
  }
}
