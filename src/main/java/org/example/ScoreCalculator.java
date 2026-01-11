package org.example;

/**
 * Oblicza końcowy wynik gry w Go.
 * Wynik jest określany na podstawie zajetego terytorium i liczby zbitych kamieni (jeńców).
 */
public class ScoreCalculator {

    /**
     * Oblicza końcowy wynik dla obu graczy.
     *
     * @param board plansza gry
     * @param controller kontroler gry zawierający liczbę zbitych kamieni
     * @return obiekt Score zawierający końcowe wyniki obu graczy
     */
    public Score calculate(Board board, GameController controller) {

        TerritoryCount territory = board.calculateTerritory();

        int blackScore = territory.black() + controller.getBlackCaptures();
        int whiteScore = territory.white() + controller.getWhiteCaptures();

        return new Score(blackScore, whiteScore);
    }
}
