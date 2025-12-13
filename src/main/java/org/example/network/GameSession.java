package org.example.network;
import org.example.MoveResult;
import org.example.NetworkGameBridge;
import org.example.Point;
public class GameSession {
    private ClientHandler black;
    private ClientHandler white;
    private ClientHandler currentPlayer;
    private NetworkGameBridge bridge;

    

    public GameSession(ClientHandler black, ClientHandler white, NetworkGameBridge bridge){
        this.black = black;
        this.white = white;
        this.bridge = bridge;
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
        Point p = NetworkGameBridge.parsePoint(move, bridge.getGameController().getBoard().getSize());

        if (p == null) {
            client.sendToClient("INVALID: wrong format (try A1)");
            return;
        }
        if (client.stoneColor != bridge.getGameController().getCurrentPlayer()) {
            client.sendToClient("Wait for your turn.");
            return;
        }

        MoveResult result = bridge.getGameController().tryMove(p);

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
        String boardAscii = bridge.getGameController().getBoardAscii();
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
