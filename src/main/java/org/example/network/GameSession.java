package org.example.network;
import java.io.*;
import java.util.*;
import org.example.*;
public class GameSession {
    private ClientHandler black;
    private ClientHandler white;
    private ClientHandler currentPlayer;
    private GameController gameController;

    

    public GameSession(ClientHandler black, ClientHandler white, GameController gameController){
        this.black = black;
        this.white = white;
        this.gameController = gameController;
        this.currentPlayer = black;

        startGame();
    }

    private void startGame(){
        black.sendToClient("Game starts!");
        white.sendToClient("Game starts!");
        displayBoard();

        black.sendToClient("Your turn.");
        white.sendToClient("Waiting for BLACK's move...'");
    }
    public synchronized void handleMove(ClientHandler client, String move){
        Point p = NetworkGameBridge.parsePoint(move, gameController.getBoard().getSize());

        if (p == null) {
            client.sendToClient("INVALID: wrong format (try A1)");
            return;
        }
        if (client.stoneColor != gameController.getCurrentPlayer()) {
            client.sendToClient("Wait for your turn.");
            return;
        }

        MoveResult result = gameController.tryMove(p);

        switch (result) {
            case OK:
                displayBoard();
                switchTurn();
                break;
            case OCCUPIED:
                client.sendToClient("INVALID: cell occupied");
                break;
            case SUICIDE:
                client.sendToClient("INVALID: suicide move");
                break;
            case GAMEOVER:
                sendToBothClients("GAME OVER");
                break;
            default:
                break;
        }
    }

    private void sendToBothClients(String message) {
        black.sendToClient(message);
        white.sendToClient(message);
    }

    private void displayBoard() {
        String boardAscii = gameController.getBoardAscii();
        sendToBothClients(boardAscii);
    }

    private void switchTurn() {
        if (currentPlayer == black) {
            currentPlayer = white;
            black.sendToClient("Waiting for WHITE to play...");
            white.sendToClient("Your turn.");
        } else {
            currentPlayer = black;
            white.sendToClient("Waiting for BLACK to play...");
            black.sendToClient("Your turn.");
        }
    }

}
