package org.example;

import java.util.ArrayList;
import java.util.List;

import org.example.network.ClientHandler;
import org.example.network.GameSession;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameSessionTest {
    private TestClientHandler black;
    private TestClientHandler white;
    private GameSession session;

    static class TestClientHandler extends ClientHandler {
        List<String> messages = new ArrayList<>();

        TestClientHandler(Stone color) {
            super(null, color);
        }

        @Override
        public void sendToClient(String message) {
            messages.add(message);
        }
    }

    @BeforeEach
    void setup(){
        Board board = new Board(9);
        Rules rules = new GameRules();
        GameController controller = new GameController(board, rules);
        NetworkGameBridge bridge = new NetworkGameBridge(controller);

        black = new TestClientHandler(Stone.BLACK);
        white = new TestClientHandler(Stone.WHITE);

        session = new GameSession(black, white, bridge);
    }

    @Test
    void testMoveIfNotPlayersTurn() {
        session.handleMove(white, "A1");

        assertTrue(white.messages.contains("Wait for your turn."));
    }

    @Test
    void testValidMove(){
        session.handleMove(black, "A1");

        assertFalse(black.messages.isEmpty());
        assertFalse(white.messages.isEmpty());
    }

    @Test
    void testPass(){
        session.handleMove(black, "PASS");

        assertFalse(black.messages.isEmpty());
        assertFalse(white.messages.isEmpty());
    }

    @Test
    void testEndGame() {
        session.handleMove(black, "PASS");
        session.handleMove(white, "PASS");

        boolean gameOverBlack = false;
        for (int i = 0; i < black.messages.size(); i++) {
            if (black.messages.get(i).contains("GAME_OVER")) {
                gameOverBlack = true;
            }
        }

        boolean gameOverWhite = false;
        for (int i = 0; i < white.messages.size(); i++) {
            if (white.messages.get(i).contains("GAME_OVER")) {
                gameOverWhite = true;
            }
        }

        assertTrue(gameOverBlack);
        assertTrue(gameOverWhite);
    }
}
