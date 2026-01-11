package org.example;

/**
 * Reprezentuje trzy możliwe stany punktu na planszy Go.
 * Pole może zawierać czarny kamień, biały kamień lub być puste.
 */
public enum Stone {
    BLACK, WHITE, NONE;

    /**
     * Zwraca przeciwny kolor kamienia.
     *
     * @return WHITE jeśli to BLACK, BLACK jeśli to WHITE, NONE jeśli to NONE
     */
    public Stone opposite() {
        if (this == BLACK) return WHITE;
        if (this == WHITE) return BLACK;
        return NONE;
    }
}
