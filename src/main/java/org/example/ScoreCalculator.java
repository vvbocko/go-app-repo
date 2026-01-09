package org.example;

public class ScoreCalculator {

    public Score calculate(Board board, GameController controller) {

        TerritoryCount territory = board.calculateTerritory();

        int blackScore = territory.black() + controller.getBlackCaptures();
        int whiteScore = territory.white() + controller.getWhiteCaptures();

        return new Score(blackScore, whiteScore);
    }
}
