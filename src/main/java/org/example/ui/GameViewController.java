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
    private final Board board;
    private final BoardView boardView;
    private boolean myTurn = false;

    public GameViewController() {
        board = new Board(BOARD_SIZE);
        boardView = new BoardView(board);
        statusLabel = new Label("Waiting for opponent...");

        boardView.setOnBoardClick(this::handleBoardClick);

        Button passButton = new Button("PASS");
        passButton.setOnAction(e -> handlePass());

        VBox rightPanel = new VBox(15, statusLabel, passButton);

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

    public void showMessage(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
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

    public void setAdapter(NetworkGameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
        setMyTurn(false);
        statusLabel.setText("Waiting for server...");
    }
}
