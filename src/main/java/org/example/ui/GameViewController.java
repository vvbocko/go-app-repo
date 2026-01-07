package org.example.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example.*;

public class GameViewController {

    private static final int BOARD_SIZE = 9;
    private final GameController gameController;
    private final BorderPane root;
    private final Label statusLabel;

    public GameViewController() {
        Board board = new Board(BOARD_SIZE);
        Rules rules = new GameRules();
        gameController = new GameController(board, rules);

        BoardView boardView = new BoardView(board);
        gameController.addListener(boardView);

        statusLabel = new Label("Turn: " + gameController.getCurrentPlayer());
        gameController.addListener(() ->
                statusLabel.setText("Turn: " + gameController.getCurrentPlayer())
        );

        boardView.setOnBoardClick(this::handleBoardClick);

        Button passButton = new Button("PASS");
        passButton.setOnAction(e -> handlePass());

        VBox rightPanel = new VBox(15, statusLabel, passButton);

        root = new BorderPane();
        root.setCenter(boardView.getCanvas());
        root.setRight(rightPanel);
    }

    private void handleBoardClick(Point point) {
        MoveResult result = gameController.tryMove(point);
        handleResult(result);
    }

    private void handlePass() {
        MoveResult result = gameController.pass();
        handleResult(result);
    }

    private void handleResult(MoveResult result) {
        switch (result) {
            case OCCUPIED -> showMessage("INVALID: cell occupied");
            case SUICIDE -> showMessage("INVALID: suicide move");
            case KO -> showMessage("INVALID: Ko rule");
            case GAMEOVER -> showMessage("GAME OVER");
            default -> {}
        }
    }

    private void showMessage(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }

    public BorderPane getRoot() {
        return root;
    }
}
