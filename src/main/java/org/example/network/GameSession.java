package org.example.network;
import org.example.MoveResult;
import org.example.NetworkGameBridge;
import org.example.Point;
import org.example.Score;
import org.example.ScoreCalculator;
import org.example.Stone;;


/**
 * Reprezentuje pojedynczą sesję gry pomiędzy dwoma graczami.
 * Odpowiada za obsługę ruchów, synchronizację stanu gry
 * oraz komunikację z klientami.
 */
public class GameSession {
    /** Klient grający czarnymi */
    private ClientHandler black;
    /** Klient grający białymi */
    private ClientHandler white;
    /** Most łączący sesję z logiką gry */
    private NetworkGameBridge bridge;


    /**
     * Tworzy nową sesję gry dla dwóch graczy.
     *
     * @param black klient grający czarnymi
     * @param white klient grający białymi
     * @param bridge pośrednik logiki gry
     */
    public GameSession(ClientHandler black, ClientHandler white, NetworkGameBridge bridge){
        this.black = black;
        this.white = white;
        this.bridge = bridge;

        startGame();
    }


    /**
     * Inicjalizuje grę i wysyła informacje początkowe do graczy.
     */
    private void startGame(){
        black.sendToClient("You are playing as: BLACK");
        white.sendToClient("You are playing as: WHITE");

        sendToBothClients("Game starts!");
        displayBoard();
        sendCapturesUpdate();

        black.sendToClient("Your turn.");
        white.sendToClient("Waiting for BLACK to play...");
    }


    /**
     * Obsługuje ruch przesłany przez klienta.
     * Metoda waliduje ruch, aktualizuje stan gry
     * oraz wysyła odpowiednie komunikaty do graczy.
     *
     * @param client klient wykonujący ruch
     * @param move ruch w postaci tekstowej
     */
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


    /**
     * Wysyła komunikat do obu graczy.
     *
     * @param message treść wiadomości
     */
    private void sendToBothClients(String message) {
        black.sendToClient(message);
        white.sendToClient(message);
    }


    /**
     * Wysyła do graczy aktualną liczbę zbitych kamieni.
     */
    private void sendCapturesUpdate() {
        int blackCaptures = bridge.getGameController().getBlackCaptures();
        int whiteCaptures = bridge.getGameController().getWhiteCaptures();

        sendToBothClients("CAPTURES:" + blackCaptures + "," + whiteCaptures);
    }


    /**
     * Wysyła do graczy aktualny stan planszy w postaci ASCII.
     */
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


    /**
     * Przełącza turę pomiędzy graczami i informuje ich o stanie gry.
     */
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