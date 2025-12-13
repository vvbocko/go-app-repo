package org.example;
import java.util.Scanner;

import org.example.network.GameServer;

public class MainServer {
    public static void main(String[] args){
        int port = 123;
        Scanner scanner = new Scanner(System.in);
        Board board = new Board(9);
        Rules rules = new GameRules();
        GameController gameController = new GameController(board, rules);
        NetworkGameBridge bridge = new NetworkGameBridge(gameController);
        GameServer server = new GameServer(port, bridge);

        server.start();
    }
}
