package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NetworkGameBridgeTest {
    private GameController controller;
    private NetworkGameBridge bridge;

    @BeforeEach
    void setup() {
        controller = new GameController(new Board(3), new Rules() {
            @Override
            public MoveResult play(Board board, Move move) {
                return board.placeStone(new Point(move.getX(), move.getY()), move.getColor());
            }
        });
        bridge = new NetworkGameBridge(controller);
    }
    
    @Test
    void testGetGameController() {
        assertEquals(controller, bridge.getGameController());
    }

    @Test
    void testParsePointValid() {
        Point p1 = NetworkGameBridge.parsePoint("A1", 3);
        Point p2 = NetworkGameBridge.parsePoint("B3", 3);

        assertNotNull(p1);
        assertEquals(0, p1.x());
        assertEquals(0, p1.y());

        assertNotNull(p2);
        assertEquals(1, p2.x());
        assertEquals(2, p2.y());
    }

}
