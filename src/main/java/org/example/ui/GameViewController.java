package org.example.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example.*;

public class GameViewController {

    private static final int BOARD_SIZE = 9;

    private final Board board;
    private final GameController gameController;
    private final BoardView boardView;

    private final BorderPane root;

    public GameViewController() {
        board = new Board(BOARD_SIZE);
        Rules rules = new GameRules();
        gameController = new GameController(board, rules);

        boardView = new BoardView(board);
        boardView.setOnBoardClick(this::handleBoardClick);

        Button passButton = new Button("PASS");
        passButton.setOnAction(e -> handlePass());

        VBox rightPanel = new VBox(10, passButton);

        root = new BorderPane();
        root.setCenter(boardView.getCanvas());
        root.setRight(rightPanel);
    }

    private void handleBoardClick(Point point) {
        MoveResult result = gameController.tryMove(point);

        if (result == MoveResult.OK) {
            boardView.draw();
        }
    }

    private void handlePass() {
        MoveResult result = gameController.pass();
        if (result == MoveResult.GAMEOVER) {
            System.out.println("GAME OVER");
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}
