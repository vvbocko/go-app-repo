package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Klient sieciowy gry.
 * Odpowiada za nawiązywanie połączenia z serwerem,
 * wysyłanie komunikatów oraz odbieranie wiadomości z serwera
 * w osobnym wątku.
 */
public class GameClient {
    /** Gniazdo połączenia z serwerem */
    private Socket socket;
    /** Strumień wyjściowy do wysyłania danych do serwera */
    private PrintWriter out;
    /** Strumień wejściowy do odbierania danych z serwera */
    private BufferedReader in;
    /** Obiekt nasłuchujący komunikatów przychodzących z serwera */
    private ServerMessageListener listener;


    /**
     * Ustawia obiekt nasłuchujący wiadomości z serwera.
     *
     * @param listener obiekt implementujący ServerMessageListener
     */
    public void setServerMessageListener(ServerMessageListener listener) {
        this.listener = listener;
    }



    /**
     * Nawiązuje połączenie z serwerem gry.
     *
     * @param ip adres IP serwera
     * @param port numer portu serwera
     */
    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to server.");

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }



    /**
     * Wysyła komunikat tekstowy do serwera.
     *
     * @param message treść wiadomości
     */
    public void sendMessage(String message) {
        out.println(message);
    }


    /**
     * Zamyka połączenie z serwerem.
     */
    public void close() {
        try { 
            socket.close(); 
        } catch (Exception ignored) {}
    }


    /**
     * Nasłuchuje komunikatów z serwera w pętli
     * i przekazuje je do zarejestrowanego nasłuchiwacza.
     */
    private void listen() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                if (listener != null) {
                    listener.onServerMessage(msg);
                }
            }
        } catch (IOException e) {
            System.out.println("Server connection lost.");
        }
    }


    /**
     * Uruchamia nasłuchiwanie komunikatów serwera w osobnym wątku.
     */
    public void startListening() {
        new Thread(this::listen, "ServerListenerThread").start();
    }
}

