package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void shouldPlaceStoneOnEmptyField() {
        Board board = new Board(9);

        MoveResult result = board.placeStone(new Point(4, 4), Stone.BLACK);

        assertEquals(MoveResult.OK, result);
        assertEquals(Stone.BLACK, board.getStone(new Point(4, 4)));
    }

    @Test 
    void testGetSize() {
        Board board = new Board(9);
        assertEquals(9, board.getSize());
    }

    @Test
    void testIsEmpty(){
        Board board = new Board(9);
        Point p = new Point(1, 1);

        assertTrue(board.isEmpty(p));
    }

    @Test
    void testSetGetStone() {
        Board board = new Board(9);
        Point p = new Point(1, 1);

        board.setStone(p, Stone.BLACK);

        assertEquals(Stone.BLACK, board.getStone(p));
        assertFalse(board.isEmpty(p));
    }

    @Test
    void testPlaceStoneOnOccupiedPlace(){
        Board board = new Board(9);
        Point p = new Point(1, 1);

        board.placeStone(p, Stone.BLACK);
        MoveResult result = board.placeStone(p, Stone.WHITE);

        assertEquals(MoveResult.OCCUPIED, result);
    }

    @Test
    void testSuicideMove(){
        Board board = new Board(9);
        Point p1 = new Point(0,1);
        Point p2 = new Point(1,0);
        Point p3 = new Point(2,1);
        Point p4 = new Point(1,2);
        Point suicidePoint = new Point(1, 1);

        board.setStone(p1, Stone.BLACK);
        board.setStone(p2, Stone.BLACK);
        board.setStone(p3, Stone.BLACK);
        board.setStone(p4, Stone.BLACK);

        MoveResult result = board.placeStone(suicidePoint, Stone.WHITE);

        assertEquals(MoveResult.SUICIDE, result);
        assertEquals(Stone.NONE, board.getStone(suicidePoint));
    }



}
