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

    private ServerMessageListener listener;

    public void setServerMessageListener(ServerMessageListener listener) {
        this.listener = listener;
    }

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

    public void sendMessage(String message) {
        out.println(message);
    }

    public void close() {
        try { 
            socket.close(); 
        } catch (Exception ignored) {}
    }

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

    public void startListening() {
        new Thread(this::listen, "ServerListenerThread").start();
    }
}

