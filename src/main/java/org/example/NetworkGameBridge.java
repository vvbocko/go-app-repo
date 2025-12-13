package org.example;

public class NetworkGameBridge {
    private GameController gameController;

    public NetworkGameBridge(GameController gameController){
        this.gameController = gameController;
    }

    public GameController getGameController(){
        return gameController;
    }

    public static Point parsePoint(String input, int size) {
        if (input == null || input.isBlank()) {
            System.out.println("Error: wpisz współrzędne (np. A1)");
            return null;
        }

        input = input.toUpperCase();

        if (!input.matches("^[A-Z]\\d+$")) {
            System.out.println("Error: zły format (poprawny to np. A1)");
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
