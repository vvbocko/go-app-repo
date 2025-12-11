package org.example;

public class GameController {
    private final Board board;
    private Stone currentPlayer = Stone.BLACK;

    public GameController(Board board) {
        this.board = board;
    }

    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    public void playMove(Point point) {
        board.placeStone(point, currentPlayer);
        currentPlayer = currentPlayer.opposite();
    }

    public String getBoardAscii() {
        return board.toAscii();
    }
}
// Dodać:
// - sprawdzenie czy pole wolne
// - liczenie oddechów
// - usuwanie zbitych kamieni