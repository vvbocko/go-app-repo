package org.example.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.example.NetworkGameBridge;
import org.example.Stone;

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

                if (clients.size() >=2){
                    PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
                    out.println("Game full, you can't join now.'");
                    playerSocket.close();
                    continue;
                }
                System.out.println("New client connected");


                ClientHandler handler = new ClientHandler(playerSocket, bridge);
                if (clients.isEmpty()) {
                    handler.stoneColor = Stone.BLACK;
                } else {
                    handler.stoneColor = Stone.WHITE;
                }
                

                clients.add(handler);
                new Thread(handler).start();

                if (clients.size() == 1) {
                    //handler.sendToClient("Waiting for second player...");
                }

                if (clients.size() == 2) {
                    clients.get(0).sendToClient("Second player joined. Game starts!");
                    clients.get(1).sendToClient("Game starts!");

                    clients.get(0).sendToClient(bridge.getGameController().getBoardAscii());
                    clients.get(1).sendToClient(bridge.getGameController().getBoardAscii());
                }
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
