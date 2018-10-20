package com.webcheckers.ui;

import com.webcheckers.model.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import com.webcheckers.model.ModelBoard;

import java.util.Objects;

/**
 * The UI controller for handling the submission of a turn for a player
 */
public class PostSubmitTurn implements Route {

  //GSON engine for recieving/sending JSON information
  private final Gson gson;
  
  //Static final variables (constants)
  private static final String ERROR_SUBMIT_TURN = "Error on submission";
  private static final String SUCCESS_SUBMIT_TURN = "Submission successful";

  /**
   * Main method for POST submit move 
   *
   * @param gson the json system for handling JQuery response/request methods
   */
  public PostSubmitTurn(final Gson gson) {
    
    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /submitTurn}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's turn (true/false)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session session = request.session();

    ModelBoard modelBoard = session.attribute(GetGameRoute.MODEL_BOARD);

    if (modelBoard.checkMadeMove()) {
      modelBoard.submitMove();
      Message message = new Message(Message.Type.info, SUCCESS_SUBMIT_TURN);
      return gson.toJson(message);
    } else {
      return gson.toJson(new Message(Message.Type.error, ERROR_SUBMIT_TURN));
    }
  }
}
