package org.example.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.example.NetworkGameBridge;
import org.example.Stone;


/**
 * Serwer gry Go.
 * Odpowiada za przyjmowanie połączeń klientów,
 * przypisywanie im kolorów oraz inicjalizowanie sesji gry.
 */
public class GameServer {
    /** Numer portu, na którym działa serwer */
    private int port;
    /** Most łączący logikę gry z warstwą sieciową */
    private NetworkGameBridge bridge;
    /** Lista aktualnie podłączonych klientów */
    private List<ClientHandler> clients = new ArrayList<>();



    /**
     * Tworzy nowy serwer gry.
     *
     * @param port numer portu serwera
     * @param bridge obiekt pośredniczący między serwerem a logiką gry
     */
    public GameServer(int port, NetworkGameBridge bridge) {
        this.port = port;
        this.bridge = bridge;
    }


    /**
     * Uruchamia serwer i rozpoczyna nasłuchiwanie połączeń klientów.
     * Po podłączeniu dwóch graczy tworzona jest nowa sesja gry.
     */
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

                Stone color;
                if (clients.isEmpty()) {
                    color = Stone.BLACK;
                } else {
                    color = Stone.WHITE;
                }
                ClientHandler handler = new ClientHandler(playerSocket, color);
                clients.add(handler);

                new Thread(handler).start();

                if (clients.size() == 2) {
                    ClientHandler black = clients.get(0);
                    ClientHandler white = clients.get(1);

                    GameSession session = new GameSession(black, white, bridge);
                    black.setSession(session);
                    white.setSession(session);
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
