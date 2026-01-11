package org.example;

/**
 * Reprezentuje możliwe rezultaty próby wykonania ruchu w grze Go.
 */
public enum MoveResult {
    /** Ruch został wykonany poprawnie */
    OK,
    /** Wybrane pole jest zajęte */
    OCCUPIED,
    /** Ruch jest samobójczy */
    SUICIDE,
    /** Gracz spasował */
    PASS,
    /** Ruch narusza zasadę Ko */
    KO,
    /** Gracz się poddał */
    SURRENDER,
    /** Gra została zakończona */
    GAMEOVER
}
