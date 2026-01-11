package org.example.ui;

import org.example.Board;
import org.example.Point;
import org.example.network.NetworkGameAdapter;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


/**
 * Kontroler głównego widoku gry.
 * Odpowiada za integrację interfejsu graficznego z logiką gry
 * oraz obsługę akcji użytkownika.
 */
public class GameViewController {
    /** Rozmiar planszy gry */
    private static final int BOARD_SIZE = 9;
    /** Adapter komunikacji z logiką gry */
    private NetworkGameAdapter gameAdapter;
    /** Główny kontener widoku */
    private final BorderPane root;
    /** Etykieta wyświetlająca status gry */
    private final Label statusLabel;
    /** Etykieta wyświetlająca liczbę zbitych kamieni czarnych */
    private final Label blackCapturesLabel;
    /** Etykieta wyświetlająca liczbę zbitych kamieni białych */
    private final Label whiteCapturesLabel;
    /** Model planszy gry */
    private final Board board;
    /** Widok planszy */
    private final BoardView boardView;
    /** Informacja czy aktualnie jest tura gracza */
    private boolean myTurn = false;


    /**
     * Tworzy kontroler widoku gry i inicjalizuje interfejs użytkownika.
     */
    public GameViewController() {
        board = new Board(BOARD_SIZE);
        boardView = new BoardView(board);
        statusLabel = new Label("Waiting for opponent...");
        blackCapturesLabel = new Label("BLACK captures: 0");
        whiteCapturesLabel = new Label("WHITE captures: 0");

        boardView.setOnBoardClick(this::handleBoardClick);

        Button passButton = new Button("PASS");
        passButton.setOnAction(e -> handlePass());

        Button surrenderButton = new Button("SURRENDER");
        surrenderButton.setOnAction(e -> handleSurrender());

        VBox rightPanel = new VBox(15, statusLabel, blackCapturesLabel, whiteCapturesLabel, passButton, surrenderButton);

        root = new BorderPane();
        root.setCenter(boardView.getCanvas());
        root.setRight(rightPanel);
    }


    /**
     * Obsługuje kliknięcie użytkownika na planszy.
     *
     * @param point punkt planszy
     */
    private void handleBoardClick(Point point) {
        gameAdapter.playMove(point);
    }


    /**
     * Obsługuje akcję pasu gracza.
     */
    private void handlePass() {
        gameAdapter.pass();
    }


    /**
     * Obsługuje akcję poddania gry przez gracza.
     */
    private void handleSurrender() {
        gameAdapter.surrender();
    }


    /**
     * Wyświetla komunikat informacyjny.
     *
     * @param text treść komunikatu
     */
    public void showMessage(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }


    /**
     * Wyświetla okno końca gry wraz z wynikami.
     *
     * @param blackScore wynik gracza czarnego
     * @param whiteScore wynik gracza białego
     * @param winner informacja o zwycięzcy
     */
    public void showGameOver(String blackScore, String whiteScore, String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over!");
        alert.setContentText(
                blackScore + "\n" +
                        whiteScore + "\n\n" +
                        winner
        );
        alert.showAndWait();
    }


    /**
     * Zwraca główny kontener widoku.
     *
     * @return obiekt BorderPane
     */
    public BorderPane getRoot() {
        return root;
    }


    /**
     * Zwraca widok planszy.
     *
     * @return obiekt BoardView
     */
    public BoardView getBoardView() {
        return boardView;
    }


    /**
     * Zwraca model planszy gry.
     *
     * @return obiekt Board
     */
    public Board getBoard() {
        return board;
    }


    /**
     * Ustawia informację o turze gracza.
     *
     * @param value true jeśli jest tura gracza
     */
    public void setMyTurn(boolean value) {
        myTurn = value;
    }


    /**
     * Sprawdza czy aktualnie jest tura gracza.
     *
     * @return true jeśli jest tura gracza
     */
    public boolean isMyTurn() {
        return myTurn;
    }


    /**
     * Odświeża widok planszy.
     */
    public void refresh() {
        boardView.draw();
    }


    /**
     * Ustawia tekst statusu gry.
     *
     * @param text treść statusu
     */
    public void setStatus(String text) {
        statusLabel.setText(text);
    }


    /**
     * Aktualizuje wyświetlaną liczbę zbitych kamieni.
     *
     * @param blackCaptures liczba zbitych kamieni czarnych
     * @param whiteCaptures liczba zbitych kamieni białych
     */
    public void setCapturesDisplay(int blackCaptures, int whiteCaptures) {
        blackCapturesLabel.setText("BLACK captures: " + blackCaptures);
        whiteCapturesLabel.setText("WHITE captures: " + whiteCaptures);
    }


    /**
     * Przypisuje adapter gry do kontrolera widoku.
     *
     * @param gameAdapter adapter komunikacji gry
     */
    public void setAdapter(NetworkGameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
        setMyTurn(false);
        statusLabel.setText("Waiting for server...");
    }
}