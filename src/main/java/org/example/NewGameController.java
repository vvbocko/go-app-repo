package org.example;

public class NewGameController  {
    private Board board;
    private PlayerInterface black;
    private PlayerInterface white;
    private PlayerInterface currentPlayer;

    public NewGameController(Board board, PlayerInterface black, PlayerInterface white) {
        this.board = board;
        this.black = black;
        this.white = white;
        this.currentPlayer = black; //zaczyna czarny
    }

    public Board getBoard() {
        return board;
    }

    public PlayerInterface getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurn() {
        if (currentPlayer == black){
            currentPlayer = white;
        }
        else {
            currentPlayer = black;
        }
    }

    public boolean placeMove(Move move){
        return board.placeStone(move);
    }

    public boolean isMovePossible(Move move) {
        return board.isOnBoard(move.getX(), move.getY()) &&
               board.isEmpty(move.getX(), move.getY());
    }

    public boolean processMoveFromNetwork(int x, int y, PlayerColor color){
        Move move = new Move(x, y, color);
        if (isMovePossible(move)){
            placeMove(move);
            switchTurn();
            return true;
        }
        return false;
    }

    public String getBoardAscii() {
        return board.toAscii(); // to widziałam że masz, to przepisałam
    }

}
