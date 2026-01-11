package org.example;

import org.example.network.GameClient;
import org.example.ui.GameViewController;


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
    }

    @BeforeAll
    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
        }
    }
 
    @Test
    void testPlayerColor() {
        TestGameClient client = new TestGameClient();
        TestGameViewController gui = new TestGameViewController();

        NetworkGameAdapter adapter = new NetworkGameAdapter(client, gui);

        adapter.onServerMessage("You are playing as: BLACK");
        assertEquals(Stone.BLACK, adapter.getColor());
        assertEquals("You play as: BLACK", gui.status);
    } 

    @Test
    void testYourTurn() {
        TestGameClient client = new TestGameClient();
        TestGameViewController gui = new TestGameViewController();
        NetworkGameAdapter adapter = new NetworkGameAdapter(client, gui);

        adapter.onServerMessage("Your turn.");
        assertTrue(gui.myTurn);
    } 
}
