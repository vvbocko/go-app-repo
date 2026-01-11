package org.example;

/**
 * Przechowuje liczbę punktów terytorium kontrolowanego przez obu graczy.
 *
 * @param black liczba punktów terytorium czarnego gracza
 * @param white liczba punktów terytorium białego gracza
 */
public record TerritoryCount(int black, int white) { }
