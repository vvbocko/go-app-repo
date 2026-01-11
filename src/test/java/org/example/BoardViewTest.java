package org.example;

import org.example.ui.BoardView;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;

public class BoardViewTest {

    @BeforeAll
    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        }
    }

    @Test
    void testCreateGetCanvas() {
        Board board = new Board(3);
        BoardView view = new BoardView(board);

        assertNotNull(view.getCanvas());
        assertEquals(600, view.getCanvas().getWidth());
        assertEquals(600, view.getCanvas().getHeight());
    }
}
