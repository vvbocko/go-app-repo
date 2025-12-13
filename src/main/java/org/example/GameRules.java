package org.example;

public class GameRules implements Rules {
    @Override
    public MoveResult play(Board board, Move move) {
        Point p = new Point(move.getX(), move.getY());
        return board.placeStone(p, move.getColor());
    }
}
