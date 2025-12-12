package org.example;

import org.example.network.ClientHandler;

public class NetworkGameBridge {
    private GameController gameController;

    public NetworkGameBridge(GameController gameController){
        this.gameController = gameController;
    }

    public void handleClientInput(ClientHandler handler, String message) {
        // tu trzeba to uzupełnić
        Point p = parsePoint(message, gameController.getBoard().getSize());

        if (p == null) {
            handler.sendToClient("INVALID: wrong format (try A1)");
            return;
        }
        PlayerColor color = handler.stoneColor;
        Move move = new Move(p.x(), p.y(), color);

        boolean ok = gameController.processMoveFromNetwork(move.getX(), move.getY(), move.getColor());

        if (ok) {
            String colorInfo;
            if (color == PlayerColor.BLACK){
                colorInfo="Black";
            }
            else {
                colorInfo = "White";
            }
            handler.sendToClient("OK: " + colorInfo + " made move: " + message);
        } else {
            handler.sendToClient("INVALID: ruch niemożliwy");
        }

    }
    private static Point parsePoint(String input, int size) {
        if (input == null || input.isBlank()) {
            System.out.println("Error: wpisz współrzędne (np. A1)");
            return null;
        }

        input = input.toUpperCase();

        if (!input.matches("^[A-Z]\\d+$")) {
            System.out.println("Erorr: zły format (poprawny to np. A1)");
            return null;
        }

        char colChar = input.charAt(0);
        int x = colChar - 'A';
        int y = Integer.parseInt(input.substring(1)) - 1;

        if (x < 0 || x >= size || y < 0 || y >= size) {
            System.out.println("Error: pole poza planszą");
            return null;
        }
        return new Point(x, y);
    }

    public boolean processMove(int x, int y, PlayerColor color) {
        return gameController.processMoveFromNetwork(x, y, color);
    }
    public String displayBoard(){
        return gameController.getBoardAscii();
    }
    public PlayerInterface getCurrentPlayer(){
        return gameController.getCurrentPlayer();
    }
}
