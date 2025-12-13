package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.example.Stone;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GameSession session;
    public Stone stoneColor;

    public ClientHandler(Socket socket, Stone stoneColor) {
        this.socket = socket;
        this.stoneColor = stoneColor;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Informacja o kolorze
            sendToClient("You are playing as: " + stoneColor);

            // Jeśli sesja jeszcze nie istnieje, poczekaj
            if (session == null) {
                sendToClient("Waiting for second player to join...");
                // czekamy na sesję, zanim klient zacznie wysyłać ruchy
                synchronized (this) {
                    while (session == null) {
                        wait();
                    }
                }
            }

            // Sesja już istnieje, gra się zaczyna
            String message;
            while ((message = in.readLine()) != null) {
                session.handleMove(this, message);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Client disconnected: " + socket);
        }
    }

    public synchronized void setSession(GameSession session) {
        this.session = session;
        notify(); // obudzi wątek, który czekał na sesję
    }

    public void sendToClient(String message) {
        out.println(message);
    }
}
