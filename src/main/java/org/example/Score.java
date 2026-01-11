package org.example;

/**
 * Reprezentuje końcowy wynik gry w Go.
 *
 * @param black końcowy wynik czarnego gracza
 * @param white końcowy wynik białego gracza
 */
public record Score(int black, int white) { }
