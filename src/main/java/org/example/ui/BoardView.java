package org.example.ui;

import java.util.function.Consumer;

import org.example.Board;
import org.example.GameStateListener;
import org.example.Point;
import org.example.Stone;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * Graficzna reprezentacja planszy do gry w Go.
 * Odpowiada za rysowanie siatki, kamieni oraz obsługę kliknięć użytkownika.
 */
public class BoardView implements GameStateListener {
    /** Rozmiar płótna planszy w pikselach */
    private static final int CANVAS_SIZE = 600;
    /** Model planszy gry */
    private final Board board;
    /** Płótno JavaFX, na którym rysowana jest plansza */
    private final Canvas canvas;
    /** Rozmiar pojedynczej komórki planszy w pikselach */
    private final double cell;
    /** Obsługa kliknięć na planszy */
    private Consumer<Point> clickHandler;


    /**
     * Reaguje na zmianę stanu gry.
     * Powoduje ponowne narysowanie planszy.
     */
    @Override
    public void onGameStateChanged() {
        draw();
    }


    /**
     * Tworzy widok planszy dla podanego modelu.
     *
     * @param board model planszy gry
     */
    public BoardView(Board board) {
        this.board = board;
        this.canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        this.cell = CANVAS_SIZE / (board.getSize() + 1.0);

        canvas.setOnMouseClicked(e -> handleClick(e.getX(), e.getY()));
        draw();
    }


    /**
     * Zwraca płótno, na którym rysowana jest plansza.
     *
     * @return obiekt Canvas
     */
    public Canvas getCanvas() {
        return canvas;
    }


    /**
     * Ustawia obsługę kliknięć użytkownika na planszy.
     *
     * @param handler funkcja wywoływana po kliknięciu pola planszy
     */
    public void setOnBoardClick(Consumer<Point> handler) {
        this.clickHandler = handler;
    }


    /**
     * Obsługuje kliknięcie myszą na planszy.
     * Przelicza współrzędne pikselowe na współrzędne planszy.
     *
     * @param mouseX współrzędna X kliknięcia
     * @param mouseY współrzędna Y kliknięcia
     */
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


    /**
     * Rysuje całą planszę gry.
     */
    public void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);

        drawGrid(gc);
        drawStones(gc);
    }


    /**
     * Rysuje siatkę planszy.
     *
     * @param gc kontekst graficzny
     */
    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);

        for (int i = 1; i <= board.getSize(); i++) {
            gc.strokeLine(cell, i * cell, board.getSize() * cell, i * cell);
            gc.strokeLine(i * cell, cell, i * cell, board.getSize() * cell);
        }
    }


    /**
     * Rysuje kamienie znajdujące się na planszy.
     *
     * @param gc kontekst graficzny
     */
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
