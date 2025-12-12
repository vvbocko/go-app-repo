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

    public MoveResult playMove(Point point) {
        MoveResult result = board.placeStone(point, currentPlayer);

        if(result == MoveResult.OK) {
            currentPlayer = currentPlayer.opposite();
        }
        return result;

    }

    public String getBoardAscii() {
        return board.toAscii();
    }
}