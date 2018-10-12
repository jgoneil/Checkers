package com.webcheckers.ui;

import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.io.BufferedReader;
import java.util.Objects;

import com.google.gson.Gson;
import com.webcheckers.appl.Player;
import com.webcheckers.appl.Board;
import com.webcheckers.appl.Space;
import com.webcheckers.appl.Row;
import com.webcheckers.model.Message;
import com.webcheckers.model.CheckMove;


/**
 * The UI Controller for handling the check to see if a move is valid or not
 */
public class PostMoveCheck implements Route {

  private final Gson gson;
  private Player player;
  private CheckMove checkMove;

  private static final String MOVE = "move";

  /**
   * Main method for POST check of a valid move
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

    StringBuffer stringBuffer = new StringBuffer();
    String currentLine = null;
    try {
      BufferedReader reader = request.raw().getReader();
      while ((currentLine = reader.readLine()) != null) {
        stringBuffer.append(currentLine);
      }
    } catch (Exception e) {
      System.out.println(e);
    }

    String temp = gson.toJson(stringBuffer);

    int rowStart = Integer.parseInt(Character.toString(temp.charAt(21)));
    int colStart = Integer.parseInt(Character.toString(temp.charAt(32)));
    int rowEnd = Integer.parseInt(Character.toString(temp.charAt(52)));
    int colEnd = Integer.parseInt(Character.toString(temp.charAt(63)));

    if (HTTPSession.attribute(MOVE) == null) {
      this.checkMove = new CheckMove(player.getModelBoard());
      HTTPSession.attribute(MOVE, checkMove);
    } else {
      this.checkMove = HTTPSession.attribute(MOVE);
    }

    Board board = this.player.getBoard();
    Row rowBeginning = board.getRow(rowStart);
    Row rowEnding = board.getRow(rowEnd);
    Space spaceStart = rowBeginning.getSpace(colStart);
    Space spaceEnd = rowEnding.getSpace(colEnd);

    if(this.checkMove.validateMove(spaceStart, spaceEnd)) {
      return gson.toJson(new Message(Message.type.normal, "Piece moved successfully"));
    } else {
      return gson.toJson(new Message
          (Message.type.error, "Piece cannot be moved to that space"));
    }
  }
}
