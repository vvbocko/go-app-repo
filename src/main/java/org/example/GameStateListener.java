package org.example;

/**
 * Interfejs do powiadamiania o zmianach stanu gry.
 */
public interface GameStateListener {
    /**
     * Metoda wywoływana przy każdej zmianie stanu gry.
     * Używana głównie do odświeżania interfejsu użytkownika.
     */
    void onGameStateChanged();
}
