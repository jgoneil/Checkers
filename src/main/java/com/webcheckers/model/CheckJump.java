package com.webcheckers.model;

import com.webcheckers.appl.Player;
import com.webcheckers.appl.Space;

import java.util.HashMap;
import java.util.Map;

public class CheckJump {

    private ModelBoard board;

    public CheckJump(ModelBoard board){this.board = board;}

    private boolean isMovingDiagonal(Position start, Position end) {
        int xDiff = Math.abs(start.getRow() - end.getRow());
        int yDiff = Math.abs(start.getCell() - end.getCell());
        return xDiff == yDiff;
    }

    private boolean isMovingTwo(Position start, Position end) {
        int xDiff = Math.abs(start.getRow() - end.getRow());
        int yDiff = Math.abs(start.getCell() - end.getCell());
        return xDiff == 2 && yDiff == 2;
    }

    private boolean isMovingForward(Position start, Position end) {
        return end.getRow() < start.getRow();
    }

    public Map<Boolean, String> validateJump(Position start, Position target, Player player) {
        Map<Boolean, String> response = new HashMap<>();
        Space current;
        Space goal;
        if(player.getColor().equals("Red")) {
            current = board.getSpace(start.getRow(), start.getCell());
            goal = board.getSpace(target.getRow(), target.getCell());
        } else {
            current = board.getSpace(7 - start.getRow(), 7 - start.getCell());
            goal = board.getSpace(7 - target.getRow(), 7 - target.getCell());
        }
        if (goal.getColor().equals(Space.Color.WHITE)) {
            response.put(false, "Attempted to move a piece to a white space.");
        } else if (goal.isOccupied()) {
            response.put(false, "Attempted to move a piece to an already occupied space");
        } else if (!isMovingTwo(start, target)) {
            response.put(false, "Attempted to move piece too far.");
        } else if (!isMovingDiagonal(start, target)) {
            response.put(false, "Pieces can only move diagonally.");
        } else if (!isMovingForward(start, target)) {
            response.put(false, "Piece can only move forward");
        } else {

            board.addPieceToSpace(current.getPiece(), goal);
            current.unoccupy();
            response.put(true, "This move is valid.");
        }
        return response;
    }
}
