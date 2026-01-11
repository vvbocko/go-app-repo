package org.example;
import java.util.Scanner;

import org.example.network.GameServer;


/**
 * Główna klasa uruchamiająca serwer gry.
 * Inicjalizuje logikę gry, most komunikacyjny
 * oraz rozpoczyna nasłuchiwanie połączeń klientów.
 */
public class MainServer {

    /**
     * Punkt wejścia aplikacji serwera.
     * Tworzy instancję serwera oraz uruchamia go na wybranym porcie.
     *
     * @param args argumenty linii poleceń
     */
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
