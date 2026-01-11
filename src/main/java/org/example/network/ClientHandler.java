package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.example.Stone;

/**
 * Obsługuje pojedynczego klienta połączonego z serwerem gry.
 * Klasa działa w osobnym wątku i odpowiada za:
 * - odbieranie ruchów od klienta
 * - przekazywanie ich do aktualnej sesji gry
 * - wysyłanie komunikatów zwrotnych do klienta
 */
public class ClientHandler implements Runnable {

    /** Gniazdo sieciowe połączone z klientem */
    private final Socket socket;
    /** Strumień wejściowy do odbierania danych od klienta */
    private BufferedReader in;
    /** Strumień wyjściowy do wysyłania danych do klienta */
    private PrintWriter out;
    /** Sesja gry, do której przypisany jest klient */
    private GameSession session;
    /** Kolor kamieni przypisany temu klientowi */
    public final Stone stoneColor;


    /**
     * Tworzy nowy obiekt obsługujący klienta.
     *
     * @param socket gniazdo sieciowe klienta
     * @param stoneColor kolor kamieni przypisany graczowi
     */
    public ClientHandler(Socket socket, Stone stoneColor) {
        this.socket = socket;
        this.stoneColor = stoneColor;
    }


    /**
     * Główna pętla wątku klienta.
     * Inicjalizuje strumienie komunikacyjne, czeka na przypisanie do sesji gry,
     * a następnie odbiera ruchy od klienta i przekazuje je do obiektu GameSession.
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            synchronized (this) {
                while (session == null) {
                    wait();
                }
            }

            String move;
            while ((move = in.readLine()) != null) {
                session.handleMove(this, move);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Client disconnected: " + socket);
        }
    }


    /**
     * Przypisuje klienta do sesji gry.
     * Metoda wybudza wątek oczekujący na rozpoczęcie rozgrywki.
     *
     * @param session sesja gry, do której należy klient
     */
    public synchronized void setSession(GameSession session) {
        this.session = session;
        notify(); 
    }


    /**
     * Wysyła wiadomość tekstową do klienta.
     *
     * @param message treść wiadomości do wysłania
     */
    public void sendToClient(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}
