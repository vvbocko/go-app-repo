package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Board board = new Board(19);
        GameController gameController = new GameController(board);

        while(true) {
            System.out.println("Tura gracza: " + gameController.getCurrentPlayer());
            System.out.print("Podaj pole (wzor: A1): ");

            String input = scanner.nextLine().trim();
            Point p = parsePoint(input, board.getSize());

            if (p == null) {
                continue;
            }

            MoveResult result = gameController.playMove(p);

            if(result == MoveResult.OCCUPIED)
            {
                System.out.println("Error: wybrane pole jest zajęte");
                continue;
            }

            System.out.println(gameController.getBoardAscii());
        }
    }
    // zamiana małych liter na duże i zamiana podanego A-> 0 , B -> 1 itp aby potem dać to do Point (x,y)
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