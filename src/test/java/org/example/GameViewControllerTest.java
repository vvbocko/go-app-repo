package org.example;

import org.example.ui.GameViewController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;

public class GameViewControllerTest {
    private GameViewController controller;

    @BeforeAll
    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        }
    }

    @BeforeEach
    void setup() {
        controller = new GameViewController();
    }

    @Test
    void testSetMyTurn() {
        controller.setMyTurn(true);
        assertTrue(controller.isMyTurn());
    }

    @Test
    void testInitialState() {
        assertNotNull(controller.getRoot());
        assertNotNull(controller.getBoardView());
        assertNotNull(controller.getBoard());
        assertFalse(controller.isMyTurn());
    }

    @Test
    void testGetBoard() {
        Board board = controller.getBoard();
        assertEquals(9, board.getSize());
    }

}
