package org.example;

/**
 * Reprezentuje ruch w grze Go.
 * Zawiera współrzędne oraz kolor kamienia do postawienia.
 */
public class Move {
    private int x;
    private int y;
    private Stone color;

    /**
     * Tworzy nowy ruch.
     *
     * @param x współrzędna x (kolumna) ruchu
     * @param y współrzędna y (wiersz) ruchu
     * @param color kolor kamienia do postawienia
     */
    public Move(int x, int y, Stone color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Zwraca współrzędną x ruchu.
     *
     * @return współrzędna x
     */
    public int getX() {
        return x;
    }

    /**
     * Zwraca współrzędną y ruchu.
     *
     * @return współrzędna y
     */
    public int getY() {
        return y;
    }

    /**
     * Zwraca kolor kamienia do postawienia.
     *
     * @return kolor kamienia
     */
    public Stone getColor() {
        return color;
    }
}
