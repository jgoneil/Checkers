package com.webcheckers.ui;

import com.webcheckers.appl.BoardView;
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

  //Static final variables (constant)
  private static final String MOVE = "move";

  //Gson controller for reading and sending JSON information
  private final Gson gson;
  //The player moving the piece
  private Player player;
  //The model method for verifying the move
  private CheckMove checkMove;
  //The player that resigned for the game
  private Player resignedPlayer;

  /**
   * Main method for POST check of a valid move
   * @param gson the gson parser for JQuery Requests/Responses
   */
  public PostMoveCheck(final Gson gson) {

    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /validateMove}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's ability to move the piece (true/false)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session HTTPSession = request.session();

    this.player = HTTPSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

    if(!this.player.inGame()) {
      BoardView boardView = HTTPSession.attribute(GetGameRoute.BOARD);
      if(this.resignedPlayer == null) {
        if (boardView.getRedPlayer().equals(this.player)) {
          this.resignedPlayer = boardView.getWhitePlayer();
        } else {
          this.resignedPlayer = boardView.getRedPlayer();
        }
      }
      HTTPSession.removeAttribute(GetGameRoute.BOARD);
      HTTPSession.removeAttribute(GetGameRoute.MODEL_BOARD);
      HTTPSession.attribute(PostResignGame.RESIGNED_PLAYER, resignedPlayer);
      return gson.toJson(new Message(Message.Type.error, PostResignGame.OTHER_PLAYER_RESIGN));
    }

    ModelBoard board = HTTPSession.attribute(GetGameRoute.MODEL_BOARD);

    this.checkMove = new CheckMove(board);

    String customJson = request.body();
    Move move = gson.fromJson(customJson, Move.class);


    if (!board.checkMadeMove()) {
      Map<Boolean, String> resultFromCheck = this.checkMove
          .validateMove(move.getStart(), move.getEnd(), player);
      if (resultFromCheck.containsKey(true)) {
        board.madeMove(move);
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
