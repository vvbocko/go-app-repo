package org.example;

public class NewGameController  {
    private final Board board;
    private Stone currentPlayer = Stone.BLACK;
    private int passCounter = 0;

    public NewGameController(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    public MoveResult playMove(Move move) {
        MoveResult result = board.placeStone(new Point(move.getX(), move.getY()), move.getColor());
        if(result == MoveResult.OK) {
            passCounter = 0;
            currentPlayer = currentPlayer.opposite();
        }
        return result;
    }
    
    public MoveResult pass() {
        passCounter++;
        if(passCounter >= 2) return MoveResult.GAMEOVER;
        currentPlayer = currentPlayer.opposite();
        return MoveResult.PASS;
    }

    public String getBoardAscii() {
        return board.toAscii();
    }

}
