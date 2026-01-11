package org.example.network;
import org.example.MoveResult;
import org.example.NetworkGameBridge;
import org.example.Point;
import org.example.Score;
import org.example.ScoreCalculator;
import org.example.Stone;

public class GameSession {
    private ClientHandler black;
    private ClientHandler white;
    private NetworkGameBridge bridge;

    public GameSession(ClientHandler black, ClientHandler white, NetworkGameBridge bridge){
        this.black = black;
        this.white = white;
        this.bridge = bridge;

        startGame();
    }

    private void startGame(){
        black.sendToClient("You are playing as: BLACK");
        white.sendToClient("You are playing as: WHITE");

        sendToBothClients("Game starts!");
        displayBoard();
        sendCapturesUpdate();

        black.sendToClient("Your turn.");
        white.sendToClient("Waiting for BLACK to play...");
    }

    public synchronized void handleMove(ClientHandler client, String move) {

        if (move.equalsIgnoreCase("SURRENDER")) {
            bridge.getGameController().surrender();

            sendToBothClients("GAME_OVER");

            Stone winner = bridge.getGameController().getSurrenderWinner();
            sendToBothClients("WINNER:" + winner);
            return;
        }

        if (move.equalsIgnoreCase("PASS")) {
            MoveResult result = bridge.getGameController().pass();

            if (result == MoveResult.GAMEOVER) {
                displayBoard();

                ScoreCalculator calculator = new ScoreCalculator();
                Score score = calculator.calculate(
                        bridge.getGameController().getBoard(),
                        bridge.getGameController()
                );

                sendToBothClients("GAME_OVER");
                sendToBothClients("SCORE_BLACK:" + score.black());
                sendToBothClients("SCORE_WHITE:" + score.white());

                Stone winner = bridge.getGameController().getWinner(score);
                sendToBothClients("WINNER:" + winner);
            } else {
                displayBoard();
                sendCapturesUpdate();
                switchTurn();
            }
            return;
        }

        if (client.stoneColor != bridge.getGameController().getCurrentPlayer()) {
            client.sendToClient("Wait for your turn.");
            return;
        }
        
        Point p = NetworkGameBridge.parsePoint(
                move,
                bridge.getGameController().getBoard().getSize()
        );

        if (p == null) {
            client.sendToClient("INVALID: wrong format (try A1)");
            return;
        }

        MoveResult result = bridge.getGameController().tryMove(p);

        switch (result) {
            case OK -> {
                displayBoard();
                sendCapturesUpdate();
                switchTurn();
            }
            case OCCUPIED -> client.sendToClient("INVALID: cell occupied");
            case SUICIDE -> client.sendToClient("INVALID: suicide move");
            case KO -> client.sendToClient("INVALID: Ko rule");
            default -> {}
        }
    }

    private void sendToBothClients(String message) {
        black.sendToClient(message);
        white.sendToClient(message);
    }

    private void sendCapturesUpdate() {
        int blackCaptures = bridge.getGameController().getBlackCaptures();
        int whiteCaptures = bridge.getGameController().getWhiteCaptures();

        sendToBothClients("CAPTURES:" + blackCaptures + "," + whiteCaptures);
    }

    private void displayBoard() {
        String boardAscii = bridge.getGameController().getBoardAscii();

        black.sendToClient("BOARD_START");
        white.sendToClient("BOARD_START");

        for (String line : boardAscii.split("\n")) {
            black.sendToClient(line);
            white.sendToClient(line);
        }

        black.sendToClient("BOARD_END");
        white.sendToClient("BOARD_END");
    }

    private void switchTurn() {
        Stone current = bridge.getGameController().getCurrentPlayer();

        if (current == Stone.BLACK) {
            black.sendToClient("Your turn.");
            white.sendToClient("Waiting for BLACK...");
        } else {
            white.sendToClient("Your turn.");
            black.sendToClient("Waiting for WHITE...");
        }
    }
}