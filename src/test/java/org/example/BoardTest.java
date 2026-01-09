package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void shouldPlaceStoneOnEmptyField() {
        Board board = new Board(9);

        MoveResult result = board.placeStone(new Point(4, 4), Stone.BLACK);

        assertEquals(MoveResult.OK, result);
        assertEquals(Stone.BLACK, board.getStone(new Point(4, 4)));
    }
}
