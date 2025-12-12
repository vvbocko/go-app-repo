package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to server.");

            ClientListener listener = new ClientListener(in, this);
            new Thread(listener).start();

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void receiveMessage(String message) {
        System.out.println("Server: " + message);
    }

    public void close() {
        try { 
            socket.close(); 
        } catch (Exception ignored) {}
    }
}

