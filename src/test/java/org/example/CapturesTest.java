package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CapturesTest {

    @Test
    void shouldCaptureSingleStone() {
        Board board = new Board(9);

        board.placeStone(new Point(4, 4), Stone.WHITE);
        board.placeStone(new Point(3, 4), Stone.BLACK);
        board.placeStone(new Point(5, 4), Stone.BLACK);
        board.placeStone(new Point(4, 3), Stone.BLACK);
        board.placeStone(new Point(4, 5), Stone.BLACK);

        assertEquals(Stone.NONE, board.getStone(new Point(4, 4)));
    }
}
