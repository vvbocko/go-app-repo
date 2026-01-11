package org.example;

/**
 * Standardowa implementacja zasad gry w Go.
 * Deleguje walidację ruchów do klasy Board.
 */
public class GameRules implements Rules {

    /**
     * Wykonuje ruch na planszy.
     *
     * @param board plansza gry
     * @param move ruch do wykonania
     * @return rezultat próby wykonania ruchu
     */
    @Override
    public MoveResult play(Board board, Move move) {
        Point p = new Point(move.getX(), move.getY());
        return board.placeStone(p, move.getColor());
    }
}
