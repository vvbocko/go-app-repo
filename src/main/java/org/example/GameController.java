package org.example;

public class GameController  {
    private final Board board;
    private Stone currentPlayer = Stone.BLACK;
    private int passCounter = 0;
    private Rules rules;

    public GameController(Board board, Rules rules) {
        this.board = board;
        this.rules = rules;
    }

    public Board getBoard() {
        return board;
    }

    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    public MoveResult playMove(Move move) {
        MoveResult result = rules.play(board, move);
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

    public MoveResult tryMove(Point p){
        Move move = new Move(p.x(), p.y(), currentPlayer);
        return playMove(move);
    }
}
