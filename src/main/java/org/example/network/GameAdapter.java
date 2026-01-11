package org.example.network;

import org.example.Point;
import org.example.Stone;


/**
 * Interfejs pośredniczący pomiędzy logiką gry a sposobem komunikacji.
 * Umożliwia wykonywanie ruchów gracza niezależnie od tego,
 * czy gra jest lokalna, sieciowa czy testowa.
 */
public interface GameAdapter {

    /**
     * Wykonuje ruch gracza polegający na postawieniu kamienia
     * w podanym punkcie planszy.
     *
     * @param p punkt, w którym gracz chce postawić kamień
     */
    void playMove(Point p);


    /**
     * Wykonuje akcję pasu gracza.
     */
    void pass();


    /**
     * Zwraca kolor kamieni przypisany graczowi.
     *
     * @return kolor kamieni gracza
     */
    Stone getColor();

}
