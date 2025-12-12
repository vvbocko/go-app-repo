package org.example;

public class GameController {
    private final Board board;
    private Stone currentPlayer = Stone.BLACK;
    private int passCounter = 0;

    public GameController(Board board) {
        this.board = board;
    }

    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    public MoveResult playMove(Point point) {
        MoveResult result = board.placeStone(point, currentPlayer);

        if(result == MoveResult.OK) {
            passCounter = 0;
            currentPlayer = currentPlayer.opposite();
        }
        return result;

    }

    public MoveResult pass() {
        passCounter++;
        if(passCounter >=2) {
            return MoveResult.GAMEOVER;
        }
        currentPlayer = currentPlayer.opposite();
        return MoveResult.PASS;
    }

    public String getBoardAscii() {
        return board.toAscii();
    }
}