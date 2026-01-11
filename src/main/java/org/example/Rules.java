package org.example;

/**
 * Interfejs do implementacji zasad gry w Go.
 * Pozwala na tworzenie różnych wariantów zasad.
 */
public interface Rules {

    /**
     * Próbuje wykonać ruch na planszy zgodnie z zasadami.
     *
     * @param board plansza gry
     * @param move ruch do wykonania
     * @return rezultat próby wykonania ruchu
     */
    MoveResult play(Board board, Move move);
}
