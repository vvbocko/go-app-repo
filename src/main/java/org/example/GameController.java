package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Główny kontroler gry zarządzający przebiegiem rozgrywki.
 * Odpowiada za kolejność graczy, wykonywanie ruchów, pasowanie,
 * poddawanie się oraz powiadamianie o zmianach stanu gry.
 */
public class GameController  {
    /** Plansza gry */
    private final Board board;
    /** Zasady gry */
    private final Rules rules;
    /** Kolor aktualnie grającego gracza */
    private Stone currentPlayer = Stone.BLACK;
    /** Licznik kolejnych pasów (gra kończy się po dwóch kolejnych pasach) */
    private int passCounter = 0;
    /** Liczba kamieni zbitych przez gracza czarnego */
    private int blackCaptures = 0;
    /** Liczba kamieni zbitych przez gracza białego */
    private int whiteCaptures = 0;
    /** Lista listenerów zmian stanu gry */
    private final List<GameStateListener> listeners = new ArrayList<>();

    /**
     * Konstruktor tworzący nowy kontroler gry.
     *
     * @param board plansza gry
     * @param rules zasady gry
     */
    public GameController(Board board, Rules rules) {
        this.board = board;
        this.rules = rules;
    }

    /**
     * Dodaje listenera zmian stanu gry.
     *
     * @param l listener do dodania
     */
    public void addListener(GameStateListener l) {
        listeners.add(l);
    }

    /**
     * Powiadamia wszystkich listenerów o zmianie stanu gry.
     */
    private void notifyListeners() {
        for (GameStateListener l : listeners) {
            l.onGameStateChanged();
        }
    }

    /**
     * Zwraca planszę gry.
     *
     * @return plansza
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Zwraca kolor aktualnie grającego gracza.
     *
     * @return kolor gracza
     */
    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Wykonuje ruch na planszy.
     * Jeśli ruch jest poprawny, zmienia kolejkę gracza i aktualizuje licznik zbitych kamieni.
     *
     * @param move ruch do wykonania
     * @return wynik wykonania ruchu
     */
    public MoveResult playMove(Move move) {
        MoveResult result = rules.play(board, move);
        if(result == MoveResult.OK) {
            passCounter = 0;

            int captured = board.getLastCapturedCount();
            if (captured > 0) {
                if (currentPlayer == Stone.BLACK) {
                    blackCaptures += captured;
                }
                else {
                    whiteCaptures += captured;
                }
            }

            currentPlayer = currentPlayer.opposite();
            notifyListeners();
        }
        return result;
    }

    /**
     * Wykonuje pas (rezygnację z ruchu).
     * Jeśli obaj gracze spasują kolejno, gra się kończy.
     *
     * @return GAMEOVER jeśli gra się kończy, PASS w przeciwnym przypadku
     */
    public MoveResult pass() {
        passCounter++;
        board.clearKo();

        if(passCounter >= 2) {
            notifyListeners();
            return MoveResult.GAMEOVER;
        }
        currentPlayer = currentPlayer.opposite();
        notifyListeners();
        return MoveResult.PASS;
    }

    /**
     * Wykonuje poddanie się przez aktualnego gracza.
     *
     * @return wynik SURRENDER
     */
    public MoveResult surrender() {
        passCounter = 0;
        notifyListeners();
        return MoveResult.SURRENDER;
    }

    /**
     * Zwraca zwycięzcę po poddaniu się.
     *
     * @return kolor gracza, który wygrał (przeciwnik tego, który się poddał)
     */
    public Stone getSurrenderWinner() {
        return currentPlayer.opposite();
    }

    /**
     * Zwraca reprezentację planszy w formacie ASCII.
     *
     * @return plansza jako String
     */
    public String getBoardAscii() {
        return board.toAscii();
    }

    /**
     * Zwraca liczbę kamieni zbitych przez gracza czarnego.
     *
     * @return liczba jeńców gracza czarnego
     */
    public int getBlackCaptures() {
        return blackCaptures;
    }

    /**
     * Zwraca liczbę kamieni zbitych przez gracza białego.
     *
     * @return liczba jeńców gracza białego
     */
    public int getWhiteCaptures() {
        return whiteCaptures;
    }

    /**
     * Określa zwycięzcę na podstawie wyniku końcowego.
     *
     * @param score wynik końcowy gry
     * @return kolor zwycięzcy lub NONE w przypadku remisu
     */
    public Stone getWinner(Score score) {
        if (score.black() > score.white()) return Stone.BLACK;
        if (score.white() > score.black()) return Stone.WHITE;
        return Stone.NONE;
    }

    /**
     * Sprawdza wynik próby wykonania ruchu w danym punkcie przez aktualnego gracza.
     *
     * @param p punkt, w którym ma być wykonany ruch
     * @return wynik próby wykonania ruchu
     */
    public MoveResult tryMove(Point p){
        Move move = new Move(p.x(), p.y(), currentPlayer);
        return playMove(move);
    }
}