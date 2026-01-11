package org.example;

import org.example.network.GameClient;
import org.example.network.NetworkGameAdapter;
import org.example.ui.GameViewController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NetworkGameAdapterTest {
    static class TestGameClient extends GameClient {
        @Override
        public void sendMessage(String message) {}
    }

    static class TestGameViewController extends GameViewController {

        boolean myTurn = false;
        String status = "";
        Board board = new Board(9);

        @Override
        public Board getBoard() {
            return board;
        }

        @Override
        public void setMyTurn(boolean value) {
            myTurn = value;
        }

        @Override
        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public void refresh() {}

        @Override
        public void showMessage(String msg) {
            status = msg;
        }

        @Override
        public void setCapturesDisplay(int blackCaptures, int whiteCaptures) { }

        @Override
        public void showGameOver(String blackScore, String whiteScore, String winner) { }
    }

    @BeforeAll
    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        }
    }

    @Test
    void testPlayerColor() throws InterruptedException {
        TestGameClient client = new TestGameClient();
        TestGameViewController gui = new TestGameViewController();

        NetworkGameAdapter adapter = new NetworkGameAdapter(client, gui);

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            adapter.onServerMessage("You are playing as: BLACK");
            latch.countDown();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS), "Timeout waiting for JavaFX thread");

        //waitfor Platform.runLater in onServerMessage to finish
        Thread.sleep(100);

        assertEquals(Stone.BLACK, adapter.getColor());
        assertEquals("You play as: BLACK", gui.status);
    }

    @Test
    void testYourTurn() throws InterruptedException {
        TestGameClient client = new TestGameClient();
        TestGameViewController gui = new TestGameViewController();
        NetworkGameAdapter adapter = new NetworkGameAdapter(client, gui);

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            adapter.onServerMessage("Your turn.");
            latch.countDown();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS), "wait for JavaFX thread");
        Thread.sleep(100);

        assertTrue(gui.myTurn);
    }
}