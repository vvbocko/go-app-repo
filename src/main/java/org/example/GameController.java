package org.example;

import java.util.ArrayList;
import java.util.List;

public class GameController  {
    private final Board board;
    private final Rules rules;
    private Stone currentPlayer = Stone.BLACK;
    private int passCounter = 0;

    private final List<GameStateListener> listeners = new ArrayList<>();

    public GameController(Board board, Rules rules) {
        this.board = board;
        this.rules = rules;
    }

    public void addListener(GameStateListener l) {
        listeners.add(l);
    }

    private void notifyListeners() {
        for (GameStateListener l : listeners) {
            l.onGameStateChanged();
        }
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
            notifyListeners();
        }
        return result;
    }
    
    public MoveResult pass() {
        passCounter++;
        if(passCounter >= 2) {
            notifyListeners();
            return MoveResult.GAMEOVER;
        }
        currentPlayer = currentPlayer.opposite();
        notifyListeners();
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
