package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class BoardUpdaterTest {

    @Test
    void testUpdateFromAscii() {
        Board board = new Board(3);
        BoardUpdater updater = new BoardUpdater(board);

        String ascii =
                    "   A B C\n" +
                    " 1 B . W\n" +
                    " 2 . W .\n" +
                    " 3 B B .\n";

        updater.updateFromAscii(ascii);

        assertEquals(Stone.BLACK, board.getStone(new Point(0, 0)));
        assertEquals(Stone.NONE,  board.getStone(new Point(1, 0)));
        assertEquals(Stone.WHITE, board.getStone(new Point(2, 0)));

        assertEquals(Stone.NONE,  board.getStone(new Point(0, 1)));
        assertEquals(Stone.WHITE, board.getStone(new Point(1, 1)));
        assertEquals(Stone.NONE,  board.getStone(new Point(2, 1)));

        assertEquals(Stone.BLACK, board.getStone(new Point(0, 2)));
        assertEquals(Stone.BLACK, board.getStone(new Point(1, 2)));
        assertEquals(Stone.NONE,  board.getStone(new Point(2, 2)));
    }

    @Test
    void TestUpdateFromAsciiWhenNull() {
        Board board = new Board(2);
        BoardUpdater updater = new BoardUpdater(board);

        updater.updateFromAscii(null);

        assertEquals(Stone.NONE, board.getStone(new Point(0, 0)));
        assertEquals(Stone.NONE, board.getStone(new Point(1, 1)));
    } 
}
