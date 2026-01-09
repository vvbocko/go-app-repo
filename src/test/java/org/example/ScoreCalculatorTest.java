package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScoreCalculatorTest {

    @Test
    void shouldCalculateScore() {
        Board board = new Board(5);
        GameController controller = new GameController(board, new GameRules());

        ScoreCalculator calculator = new ScoreCalculator();
        Score score = calculator.calculate(board, controller);

        assertNotNull(score);
    }
}
