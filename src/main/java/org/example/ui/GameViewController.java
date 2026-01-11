package org.example.ui;

import org.example.Board;
import org.example.Point;
import org.example.network.NetworkGameAdapter;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GameViewController {

    private static final int BOARD_SIZE = 9;
    private NetworkGameAdapter gameAdapter;
    private final BorderPane root;
    private final Label statusLabel;
    private final Label blackCapturesLabel;
    private final Label whiteCapturesLabel;
    private final Board board;
    private final BoardView boardView;
    private boolean myTurn = false;

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

    private void handleBoardClick(Point point) {
        gameAdapter.playMove(point);
    }

    private void handlePass() {
        gameAdapter.pass();
    }

    private void handleSurrender() {
        gameAdapter.surrender();
    }

    public void showMessage(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }

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

    public BorderPane getRoot() {
        return root;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public Board getBoard() {
        return board;
    }

    public void setMyTurn(boolean value) {
        myTurn = value;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void refresh() {
        boardView.draw();
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void setCapturesDisplay(int blackCaptures, int whiteCaptures) {
        blackCapturesLabel.setText("BLACK captures: " + blackCaptures);
        whiteCapturesLabel.setText("WHITE captures: " + whiteCaptures);
    }

    public void setAdapter(NetworkGameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
        setMyTurn(false);
        statusLabel.setText("Waiting for server...");
    }
}