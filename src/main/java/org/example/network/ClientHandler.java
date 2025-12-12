package org.example.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private NetworkGameBridge bridge;
    public PlayerColor stoneColor;

    public ClientHandler(Socket socket, NetworkGameBridge bridge) {
        this.socket = socket;
        this.bridge = bridge;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("ClientHandler started for player: " + socket);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received move: " + message);

                bridge.handleClientInput(this, message);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }

    public void sendToClient(String message) {
        out.println(message);
    }
}

