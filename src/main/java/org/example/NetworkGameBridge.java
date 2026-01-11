package org.example;

/**
 * Łączy warstwę sieciową z warstwą logiki gry.
 * Udostępnia metody pomocnicze do parsowania wejścia użytkownika
 * oraz dostępu do GameController.
 */
public class NetworkGameBridge {
    private GameController gameController;

    /**
     * Tworzy nowy most pomiędzy warstwą sieciową a logiką gry.
     *
     * @param gameController kontroler gry, z którym następuje połączenie
     */
    public NetworkGameBridge(GameController gameController){
        this.gameController = gameController;
    }

    /**
     * Zwraca kontroler gry.
     *
     * @return instancja kontrolera gry
     */
    public GameController getGameController(){
        return gameController;
    }

    /**
     * Parsuje punkt na planszy ciągu znaków podanych przez użytkownika (np. "A1", "B5").
     *
     * @param input ciąg wejściowy użytkownika
     * @param size rozmiar planszy do walidacji
     * @return sparsowany obiekt Point albo null jeśli dane są niepoprawne
     */
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
