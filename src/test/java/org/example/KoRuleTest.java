package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KoRuleTest {

    @Test
    void shouldBlockImmediateKoRecapture() {
        Board board = new Board(9);

        board.placeStone(new Point(1, 0), Stone.BLACK);
        board.placeStone(new Point(2, 0), Stone.WHITE);

        board.placeStone(new Point(2, 1), Stone.BLACK); // will lose breaths
        board.placeStone(new Point(3, 1), Stone.WHITE);

        board.placeStone(new Point(1, 2), Stone.BLACK);
        board.placeStone(new Point(2, 2), Stone.WHITE);

        board.placeStone(new Point(0, 1), Stone.BLACK);
        board.placeStone(new Point(1, 1), Stone.WHITE);


        MoveResult result = board.placeStone(new Point(2, 1), Stone.BLACK); // repeated - Ko move

        assertEquals(MoveResult.KO, result);
    }
}
