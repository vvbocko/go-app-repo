package org.example.network;

/**
 * Interfejs nasłuchujący komunikatów przychodzących z serwera.
 * Implementowany przez klasy reagujące na zdarzenia sieciowe.
 */
public interface ServerMessageListener {
    /**
     * Obsługuje pojedynczy komunikat otrzymany z serwera.
     *
     * @param message treść wiadomości
     */
    void onServerMessage(String message);
}
