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

            gameController.playMove(p);

            System.out.println(gameController.getBoardAscii());
        }
    }
    // zamiana małych liter na duże i zamiana podanego A-> 0 , B -> 1 itp aby potem dać to do Point (x,y)
    private static Point parsePoint(String input, int size) {
        input = input.toUpperCase();

        char colChar = input.charAt(0);
        int x = colChar - 'A';

        int y = Integer.parseInt(input.substring(1)) - 1;

        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException(" pole poza plansza");
        }
        return new Point(x, y);
    }
    // Dodać:
    // - czy podano poprawny input - długość stringa i czy podano litere jako kolumna
    // - ogolną obsługe komunikowania błędow uzytkownikowi
}