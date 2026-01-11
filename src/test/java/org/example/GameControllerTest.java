package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameControllerTest {
    private Board board;
    private Rules rules;
    private GameController controller;

    @BeforeEach
    void setup() {
        board = new Board(3);
        rules = new Rules() {
        @Override
        public MoveResult play(Board board, Move move) {
            return board.placeStone(new Point(move.getX(), move.getY()), move.getColor());
        }};
        controller = new GameController(board, rules);
    }

    @Test 
    void testGetBoard(){
        assertEquals(board, controller.getBoard());
    }

    @Test
    void testGetCurrentPlayer(){
        assertEquals(Stone.BLACK, controller.getCurrentPlayer());
    }

    @Test
    void testTryMoveOK() {
        MoveResult result = controller.tryMove(new Point(0, 0));

        assertEquals(MoveResult.OK, result);
        assertEquals(Stone.BLACK, board.getStone(new Point(0, 0)));
        assertEquals(Stone.WHITE, controller.getCurrentPlayer());
    }

    @Test
    void testPass() {
        MoveResult result1 = controller.pass();
        MoveResult result2 = controller.pass();

        assertEquals(MoveResult.PASS, result1);
        assertEquals(Stone.WHITE, controller.getCurrentPlayer());

        assertEquals(MoveResult.GAMEOVER, result2);
    }

    @Test
    void testSurrender(){
        MoveResult result = controller.surrender();

        assertEquals(MoveResult.SURRENDER, result);
        assertEquals(Stone.WHITE, controller.getSurrenderWinner());
    }

    @Test
    void testCaptures(){
        controller.tryMove(new Point(0, 0));
        assertEquals(0, controller.getBlackCaptures());
        assertEquals(0, controller.getWhiteCaptures());
    }

    @Test
    void testGetWinner(){
        Score scoreBlack = new Score(5, 3);
        Score scoreWhite = new Score(2, 7);
        Score scoreDraw = new Score(4, 4);

        assertEquals(Stone.BLACK, controller.getWinner(scoreBlack));
        assertEquals(Stone.WHITE, controller.getWinner(scoreWhite));
        assertEquals(Stone.NONE, controller.getWinner(scoreDraw));
    }

}
