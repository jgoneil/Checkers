package com.webcheckers.ui;

import com.webcheckers.model.Message;
import com.webcheckers.appl.BoardView;
import com.webcheckers.appl.Player;
import spark.*;

import java.util.Objects;

import com.google.gson.Gson;

/**
 *
 */
public class PostResignGame implements Route {

  //Static final variables (constants)
  static final String ERROR_RESIGN = "Cannot resign due a move being made. Reverse the move if you would like to truly resign.";
  static final String SUCCESS_RESIGN = "You have successfully resigned from the game. You lost.";
  static final String OTHER_PLAYER_RESIGN = "Your opponent resigned from the game. You win!";
  static final String RESIGNED_PLAYER = "resignedPlayer";

  //Gson controller for reading and sending JSON information
  private final Gson gson;
  //Player that disconnected from the system
  private Player resignedPlayer;
  
  /**
   * Main method for POST resign game
   *
   * @param gson the gson parser for JQuery Requests/Responses
   */
  public PostResignGame(final Gson gson) {
    
    Objects.requireNonNull(gson, "Gson cannot be null.");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /resignGame}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's resignation
   */
  @Override
  public Object handle(Request request, Response response) {
    Session httpSession = request.session();

    Player player = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

    if(player.getHasMoved()) {
      Message message = new Message(Message.Type.error, ERROR_RESIGN);
      return gson.toJson(message);
    } else {
      Player player2;
      if (httpSession.attribute(GetGameRoute.BOARD) == null || player.getColor() == null) {
        Message message = new Message(Message.Type.info, OTHER_PLAYER_RESIGN);
        httpSession.attribute(RESIGNED_PLAYER, resignedPlayer);
        httpSession.attribute("message", message);
        return gson.toJson(message);
      }
      BoardView boardView = httpSession.attribute(GetGameRoute.BOARD);
      if (player.getColor().equals("Red")) {
        player2 = boardView.getWhitePlayer();
      } else {
        player2 = boardView.getRedPlayer();
      }
      player.gameEnd();
      player2.gameEnd();
      this.resignedPlayer = player;
      httpSession.removeAttribute(GetGameRoute.BOARD);
      httpSession.removeAttribute(GetGameRoute.MODEL_BOARD);
      Message message = new Message(Message.Type.info, SUCCESS_RESIGN);
      httpSession.attribute("message", message);
      return gson.toJson(message);
    }
  }
}
