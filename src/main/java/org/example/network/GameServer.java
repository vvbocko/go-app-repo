package org.example.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.example.NetworkGameBridge;

public class GameServer {

    private int port;
    private NetworkGameBridge bridge;
    private List<ClientHandler> clients = new ArrayList<>();

    public GameServer(int port, NetworkGameBridge bridge) {
        this.port = port;
        this.bridge = bridge;
    }

    public void start() {
        System.out.println("Server listening on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {

                Socket playerSocket = serverSocket.accept();
                System.out.println("New client connected");


                ClientHandler handler = new ClientHandler(playerSocket, bridge);
                if (clients.isEmpty()) {
                    handler.stoneColor = PlayerColor.BLACK;
                    handler.sendToClient("You play BLACK");
                } else {
                    handler.stoneColor = PlayerColor.WHITE;
                    handler.sendToClient("You play WHITE");
                }

                clients.add(handler);
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
