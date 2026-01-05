package org.example.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.example.Board;
import org.example.GameStateListener;
import org.example.Point;
import org.example.Stone;

import java.util.function.Consumer;

public class BoardView implements GameStateListener {

    private static final int CANVAS_SIZE = 600;

    private final Board board;
    private final Canvas canvas;
    private final double cell;

    private Consumer<Point> clickHandler;

    @Override
    public void onGameStateChanged() {
        draw();
    }

    public BoardView(Board board) {
        this.board = board;
        this.canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        this.cell = CANVAS_SIZE / (board.getSize() + 1.0);

        canvas.setOnMouseClicked(e -> handleClick(e.getX(), e.getY()));
        draw();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setOnBoardClick(Consumer<Point> handler) {
        this.clickHandler = handler;
    }

    private void handleClick(double mouseX, double mouseY) {
        int x = (int) Math.round(mouseX / cell) - 1;
        int y = (int) Math.round(mouseY / cell) - 1;

        if (x < 0 || y < 0 || x >= board.getSize() || y >= board.getSize()) {
            return;
        }

        if (clickHandler != null) {
            clickHandler.accept(new Point(x, y));
        }
    }

    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);

        drawGrid(gc);
        drawStones(gc);
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);

        for (int i = 1; i <= board.getSize(); i++) {
            gc.strokeLine(cell, i * cell, board.getSize() * cell, i * cell);
            gc.strokeLine(i * cell, cell, i * cell, board.getSize() * cell);
        }
    }

    private void drawStones(GraphicsContext gc) {
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                Stone s = board.getStone(new Point(x, y));
                if (s == Stone.NONE) continue;

                double cx = (x + 1) * cell;
                double cy = (y + 1) * cell;

                gc.setFill(s == Stone.BLACK ? Color.BLACK : Color.WHITE);
                gc.fillOval(cx - 15, cy - 15, 30, 30);
                gc.setStroke(Color.BLACK);
                gc.strokeOval(cx - 15, cy - 15, 30, 30);
            }
        }
    }
}
