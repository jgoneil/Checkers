package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.model.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * the ui controller for handling the backup of a Player move
 */
public class PostBackupMove implements Route {

  //Static final (constant) variables
  static final String SUCCESS_BACKUP_MOVE = "Your move was backed up successfully.";
  static final String ERROR_BACKUP_MOVE = "No move to backup.";
  //Gson engine for receiving/sending JSON information
  private final Gson gson;

  /**
   * Main method for POST check of turn
   *
   * @param gson the json system for handling JQuery response/request methods
   */
  public PostBackupMove(final Gson gson) {

    Objects.requireNonNull(gson, "gson cannot be null");

    this.gson = gson;
  }

  /**
   * Method to render AJAX response via gson for {@code POST /checkTurn}
   *
   * @param request the HTTP request
   * @param response the HTTP response
   * @return the AJAX response for the player's turn (true/false)
   */
  @Override
  public Object handle(Request request, Response response) {
    Session session = request.session();

    GameLobby gameLobby = session.attribute(GetGameRoute.GAMELOBBY);

    if (gameLobby.checkPendingMove()) {
      gameLobby.backupMove();
      Message message = new Message(Message.Type.info, SUCCESS_BACKUP_MOVE);
      return gson.toJson(message);
    } else {
      return gson.toJson(new Message(Message.Type.error, ERROR_BACKUP_MOVE));
    }
  }
}
