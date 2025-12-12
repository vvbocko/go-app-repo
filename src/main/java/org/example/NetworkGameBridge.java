package org.example;

import org.example.network.ClientHandler;

public class NetworkGameBridge {
    private GameController gameController;

    public NetworkGameBridge(GameController gameController){
        this.gameController = gameController;
    }

    public GameController getGameController(){
        return gameController;
    }

    public void handleClientInput(ClientHandler handler, String message) {
        Point p = parsePoint(message, gameController.getBoard().getSize());

        if (p == null) {
            handler.sendToClient("INVALID: wrong format (try A1)");
            return;
        }
        Stone color = handler.stoneColor;
        Move move = new Move(p.x(), p.y(), color);

        MoveResult result = gameController.playMove(move);
        if(result == MoveResult.OK) {
            handler.sendToClient("OK: " + color + " played " + message);
            handler.sendToClient(gameController.getBoardAscii());
        } else if(result == MoveResult.OCCUPIED) {
            handler.sendToClient("INVALID: cell occupied");
        } else if(result == MoveResult.SUICIDE) {
            handler.sendToClient("INVALID: suicide move");
        } else if(result == MoveResult.GAMEOVER) {
            handler.sendToClient("GAME OVER");
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
}
