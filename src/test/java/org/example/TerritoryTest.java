package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TerritoryTest {

    @Test
    void shouldCountSimpleTerritory() {
        Board board = new Board(5);

        board.placeStone(new Point(1, 1), Stone.BLACK);
        board.placeStone(new Point(1, 2), Stone.BLACK);
        board.placeStone(new Point(2, 1), Stone.BLACK);
        board.placeStone(new Point(2, 2), Stone.BLACK);

        TerritoryCount territory = board.calculateTerritory();

        assertTrue(territory.black() > 0);
    }
}
