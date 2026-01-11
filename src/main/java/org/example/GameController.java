package org.example;

import java.util.ArrayList;
import java.util.List;

public class GameController  {
    private final Board board;
    private final Rules rules;
    private Stone currentPlayer = Stone.BLACK;
    private int passCounter = 0;
    private int blackCaptures = 0;
    private int whiteCaptures = 0;

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

            int captured = board.getLastCapturedCount();
            if (captured > 0) {
                if (currentPlayer == Stone.BLACK) {
                    blackCaptures += captured;
                }
                else {
                    whiteCaptures += captured;
                }
            }

            currentPlayer = currentPlayer.opposite();
            notifyListeners();
        }
        return result;
    }
    
    public MoveResult pass() {
        passCounter++;
        board.clearKo();

        if(passCounter >= 2) {
            notifyListeners();
            return MoveResult.GAMEOVER;
        }
        currentPlayer = currentPlayer.opposite();
        notifyListeners();
        return MoveResult.PASS;
    }

    public MoveResult surrender() {
        passCounter = 0;
        notifyListeners();
        return MoveResult.SURRENDER;
    }

    public Stone getSurrenderWinner() {
        return currentPlayer.opposite();
    }

    public String getBoardAscii() {
        return board.toAscii();
    }

    public int getBlackCaptures() {
        return blackCaptures;
    }

    public int getWhiteCaptures() {
        return whiteCaptures;
    }

    public Stone getWinner(Score score) {
        if (score.black() > score.white()) return Stone.BLACK;
        if (score.white() > score.black()) return Stone.WHITE;
        return Stone.NONE;
    }


    public MoveResult tryMove(Point p){
        Move move = new Move(p.x(), p.y(), currentPlayer);
        return playMove(move);
    }
}
