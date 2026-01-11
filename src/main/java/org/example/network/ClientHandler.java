package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.example.Stone;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GameSession session;
    public final Stone stoneColor;

    public ClientHandler(Socket socket, Stone stoneColor) {
        this.socket = socket;
        this.stoneColor = stoneColor;
    }

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

    public synchronized void setSession(GameSession session) {
        this.session = session;
        notify(); 
    }

    public void sendToClient(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}
