package org.example.network;

import org.example.Board;
import org.example.BoardUpdater;
import org.example.Point;
import org.example.Stone;
import org.example.ui.GameViewController;


/**
 * Adapter gry sieciowej.
 * Odpowiada za komunikację pomiędzy klientem sieciowym a interfejsem graficznym,
 * tłumacząc zdarzenia GUI na komunikaty sieciowe oraz komunikaty serwera
 * na aktualizacje stanu gry.
 */
public class NetworkGameAdapter implements GameAdapter, ServerMessageListener {

    /** Klient sieciowy odpowiedzialny za komunikację z serwerem */
    private final GameClient client;
    /** Kolor kamieni przypisany graczowi */
    private Stone playerColor;
    /** Plansza */
    private final Board board;
    /** Kontroler interfejsu graficznego gry */
    private final GameViewController gui;
    /** Narzędzie do aktualizacji planszy na podstawie danych tekstowych */
    private BoardUpdater boardUpdater;
    /** Flaga informująca o odbieraniu danych planszy z serwera */
    private boolean receivingBoard = false;
    /** Bufor przechowujący tekstową reprezentację planszy */
    private StringBuilder boardBuffer = new StringBuilder();
    /** Tymczasowy wynik punktowy gracza czarnego */
    private String pendingBlackScore = "";
    /** Tymczasowy wynik punktowy gracza białego */
    private String pendingWhiteScore = "";


    /**
     * Tworzy adapter gry sieciowej.
     * Rejestruje obiekt jako nasłuchujący komunikatów serwera
     * oraz inicjalizuje lokalną planszę gry.
     *
     * @param client klient sieciowy
     * @param gui kontroler interfejsu graficznego
     */
    public NetworkGameAdapter(GameClient client, GameViewController gui) {
        this.client = client;
        this.gui = gui;
        this.board = gui.getBoard();
        this.boardUpdater = new BoardUpdater(board);

        client.setServerMessageListener(this);
    }


    /**
     * Obsługuje komunikaty przychodzące z serwera.
     * Metoda aktualizuje stan planszy, interfejs użytkownika
     * oraz informacje o przebiegu rozgrywki.
     *
     * @param msg wiadomość otrzymana z serwera
     */
    @Override
    public void onServerMessage(String msg) {
        javafx.application.Platform.runLater(() -> {

            if (msg.equals("BOARD_START")) {
                receivingBoard = true;
                boardBuffer.setLength(0);
                return;
            }

            if (msg.equals("BOARD_END")) {
                receivingBoard = false;
                boardUpdater.updateFromAscii(boardBuffer.toString());
                gui.refresh();
                return;
            }

            if (receivingBoard) {
                boardBuffer.append(msg).append("\n");
                return;
            }

            if (msg.startsWith("You are playing as:")) {
                playerColor = msg.contains("BLACK") ? Stone.BLACK : Stone.WHITE;
                gui.setStatus("You play as: " + playerColor);
            }
            else if (msg.startsWith("Your turn")) {
                gui.setMyTurn(true);
                gui.setStatus("Your turn");
            }
            else if (msg.startsWith("Waiting")) {
                gui.setMyTurn(false);
                gui.setStatus(msg);
            }
            else if (msg.startsWith("CAPTURES:")) {
                String[] parts = msg.substring(9).split(",");
                int blackCaptures = Integer.parseInt(parts[0]);
                int whiteCaptures = Integer.parseInt(parts[1]);
                gui.setCapturesDisplay(blackCaptures, whiteCaptures);
            }
            else if (msg.equals("GAME_OVER")) {
                gui.setMyTurn(false);
                pendingBlackScore = "";
                pendingWhiteScore = "";
            }
            else if (msg.startsWith("SCORE_BLACK:")) {
                pendingBlackScore = "BLACK: " + msg.substring(12);
            }
            else if (msg.startsWith("SCORE_WHITE:")) {
                pendingWhiteScore = "WHITE: " + msg.substring(12);
            }
            else if (msg.startsWith("WINNER:")) {
                String winner = msg.substring(7);

                if (!pendingBlackScore.isEmpty() && !pendingWhiteScore.isEmpty()) {
                    gui.showGameOver(pendingBlackScore, pendingWhiteScore, "Winner: " + winner);
                } else {
                    gui.showMessage("GAME OVER\nWinner: " + winner);
                }
            }
            else if (msg.startsWith("INVALID")) {
                gui.showMessage(msg);
            }
        });
    }


    /**
     * Wysyła do serwera informację o ruchu gracza.
     * Ruch jest wysyłany tylko wtedy, gdy aktualnie jest tura gracza.
     *
     * @param p punkt, w którym gracz chce postawić kamień
     */
    @Override
    public void playMove(Point p) {
        if (!gui.isMyTurn()) {
            System.out.println("Not your turn");
            return;
        }
        String move = convertPoint(p);
        System.out.println("Sending move: " + move);
        client.sendMessage(move);
    }


    /**
     * Wysyła do serwera informację o pasie gracza.
     */
    @Override
    public void pass() {
        client.sendMessage("PASS");
    }


    /**
     * Wysyła do serwera informację o poddaniu gry przez gracza.
     */
    public void surrender() {
        client.sendMessage("SURRENDER");
    }


    /**
     * Zwraca kolor kamieni przypisany graczowi.
     *
     * @return kolor kamieni gracza
     */
    @Override
    public Stone getColor() {
        return playerColor;
    }


    /**
     * Konwertuje punkt planszy na reprezentację tekstową
     * zgodną z protokołem komunikacyjnym serwera (np. A1, C5).
     *
     * @param p punkt planszy
     * @return tekstowa reprezentacja punktu
     */
    private String convertPoint(Point p) {
        char col = (char)('A' + p.x());
        String row = String.valueOf(p.y() + 1);
        return col + row;
    }
}

