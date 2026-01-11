package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Reprezentuje planszę do gry w Go.
 * Zarządza siatką kamieni, waliduje ruchy zgodnie z zasadami Go
 * oraz obsługuje mechanikę zbijania kamieni włącznie z wykrywaniem reguły Ko.
 */
public class Board {
    private final Stone[][] grid;
    private final int size;
    private Stone[][] previousGrid;
    private int lastCapturedCount = 0;

    /**
     * Tworzy nową planszę Go o określonym rozmiarze (9).
     *
     * @param size wymiary planszy
     */
    public Board(int size) {
        this.grid = new Stone[size][size];
        this.size = size;
        initialiseBoard();
    }

    /**
     * Inicjalizuje planszę ustawiając wszystkie pozycje jako puste (NONE).
     */
    private void initialiseBoard() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = Stone.NONE;
            }
        }
    }

    /**
     * Sprawdza czy pozycja na planszy jest pusta.
     *
     * @param point pozycja do sprawdzenia
     * @return true - pozycja jest pusta, false - w przeciwnym razie
     */
    public boolean isEmpty(Point point) {
        return grid[point.x()][point.y()] == Stone.NONE;
    }

    /**
     * Zwraca wszystkich sąsiadów danego punktu.
     *
     * @param point punkt którego sąsiadów chcemy pobrać
     * @return lista sąsiednich punktów dla point
     */
    private List<Point> getNeighbours(Point point) {
        List<Point> neighbourList = new ArrayList<>();

        int x = point.x();
        int y = point.y();

        if (x > 0) {
            neighbourList.add(new Point(x-1, y));
        }
        if (x < size-1) {
            neighbourList.add(new Point(x+1, y));
        }
        if (y > 0) {
            neighbourList.add(new Point(x, y-1));
        }
        if (y < size-1) {
            neighbourList.add(new Point(x, y+1));
        }
        return neighbourList;
    }

    /**
     * Znajduje wszystkie kamienie stykające się wspólnie z kamieniem w punkcie startowym.
     * Wykorzystuje algorytm przeszukiwania Breadth-First Search do znalezienia wszystkich kamieni
     * tego samego koloru tworzących połączoną grupę.
     *
     * @param startPoint punkt startowy
     * @return lista wszystkich punktów w grupie
     */
    private List<Point> getGroup(Point startPoint) {
        List<Point> group = new ArrayList<>();

        Stone color = grid[startPoint.x()][startPoint.y()];
        if (color == Stone.NONE){
            return group;
        }

        boolean[][] visited = new boolean[size][size];
        Queue<Point> toBeVisited = new LinkedList<>();
        toBeVisited.add(startPoint);

        while(!toBeVisited.isEmpty()) {
            Point point = toBeVisited.poll();

            if (visited[point.x()][point.y()]) {
                continue;
            }
            visited[point.x()][point.y()] = true;
            group.add(point);

            for (Point n : getNeighbours(point)) {
                if (visited[n.x()][n.y()]) {
                    continue;
                }
                if (grid[n.x()][n.y()] == color) {
                    toBeVisited.add(n);
                }
            }
        }
        return group;
    }

    /**
     * Liczy liczbę oddechów (pustych sąsiadujących pól) dla grupy kamieni.
     *
     * @param group grupa kamieni dla której liczone są oddechy
     * @return liczba oddechów grupy
     */
    private int countBreaths(List<Point> group) {
        Set<Point> breaths = new HashSet<>();

        for (Point points : group) {
            for (Point n : getNeighbours(points)) {
                if (grid[n.x()][n.y()] == Stone.NONE) {
                    breaths.add(n);
                }
            }
        }
        return breaths.size();
    }

    /**
     * Usuwa grupę kamieni z planszy.
     *
     * @param group grupa kamieni do usunięcia
     * @return liczba usuniętych kamieni (zdobytych jeńców)
     */
    private int removeGroup(List<Point> group) {
        for (Point points : group) {
            grid[points.x()][points.y()] = Stone.NONE;
        }
        return group.size();
    }

    /**
     * Próbuje umieścić kamień na planszy w danym punkcie.
     * Waliduje ruch zgodnie z zasadami:
     * - Pozycja musi być pusta
     * - nie można wykonać ruchu samobójczego (chyba, że zbijamy kamienie przeciwnika)
     * - nie można naruszać reguły Ko (wykonany ruch prowadzi do stanu poprzedniego planszy)
     *
     * @param point pozycja gdzie kamień ma być umieszczony
     * @param stone kolor kamienia do umieszczenia
     * @return wynik próby wykonania ruchu
     */
    public MoveResult placeStone(Point point, Stone stone) {
        lastCapturedCount = 0;

        if(!isEmpty(point)) {
            return MoveResult.OCCUPIED;
        }

        Stone[][] snapshotBeforeMove = copyGrid();
        grid[point.x()][point.y()] = stone;
        Stone enemyColor = stone.opposite();

        List<List<Point>> enemyGroup = new ArrayList<>();
        for (Point n : getNeighbours(point)) {
            if(grid[n.x()][n.y()] == enemyColor) {
                enemyGroup.add(getGroup(n));
            }
        }

        List<List<Point>> enemyGroupToKill = new ArrayList<>();
        for (List<Point> enemies : enemyGroup) {
            if(countBreaths(enemies) == 0) {
                enemyGroupToKill.add(enemies);
            }
        }

        for (List<Point> enemiesToKill : enemyGroupToKill) {
            lastCapturedCount += removeGroup(enemiesToKill);
        }

        List<Point> myGroup = getGroup(point);
        int myBreaths = countBreaths(myGroup);

        if (myBreaths == 0 && enemyGroupToKill.isEmpty()) {
            grid[point.x()][point.y()] = Stone.NONE;
            return MoveResult.SUICIDE;
        }

        if (previousGrid != null && gridsEqual(grid, previousGrid)){
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    grid[x][y] = snapshotBeforeMove[x][y];
                }
            }
            return MoveResult.KO;
        }
        previousGrid = snapshotBeforeMove;

        return MoveResult.OK;
    }

    /**
     * Ręcznie ustawia kamień w danym punkcie.
     * Używane do synchronizacji planszy w klient-serwer.
     *
     * @param p pozycja
     * @param stone kolor kamienia do umieszczenia
     */
    public void setStone(Point p, Stone stone){
        grid[p.x()][p.y()] = stone;
    }

    /**
     * Znajduje wszystkie puste grupy połączone z punktem startowym.
     * Ponowne wykorzystanie algorytmu Breadth-First Search
     *
     * @param startPoint startowa pusta pozycja
     * @return lista wszystkich połączonych pustych pozycji
     */
    private List<Point> getEmptyArea(Point startPoint) {
        List<Point> area = new ArrayList<>();

        if (grid[startPoint.x()][startPoint.y()] != Stone.NONE) {
            return area;
        }

        boolean[][] visited = new boolean[size][size];
        Queue<Point> queue = new LinkedList<>();
        queue.add(startPoint);

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (visited[p.x()][p.y()]) continue;
            visited[p.x()][p.y()] = true;
            area.add(p);

            for (Point n : getNeighbours(p)) {
                if (!visited[n.x()][n.y()] &&
                        grid[n.x()][n.y()] == Stone.NONE) {
                    queue.add(n);
                }
            }
        }
        return area;
    }

    /**
     * Określa, które kolory kamieni otaczają dany pusty obszar.
     *
     * @param area pusty obszar do sprawdzenia
     * @return zbiór kolorów kamieni otaczających obszar
     */
    private Set<Stone> getBorderColors(List<Point> area) {
        Set<Stone> colors = new HashSet<>();

        for (Point p : area) {
            for (Point n : getNeighbours(p)) {
                Stone s = grid[n.x()][n.y()];
                if (s != Stone.NONE) {
                    colors.add(s);
                }
            }
        }
        return colors;
    }

    /**
     * Oblicza terytorium graczy.
     * Terytorium liczone jest jako puste obszary całkowicie otoczone kamieniami jednego koloru.
     *
     * @return obiekt TerritoryCount zawieraja liczniki terytorium czarnego i białego
     */
    public TerritoryCount calculateTerritory() {
        boolean[][] visited = new boolean[size][size];
        int blackTerritory = 0;
        int whiteTerritory = 0;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point p = new Point(x, y);

                if (grid[x][y] == Stone.NONE && !visited[x][y]) {
                    List<Point> area = getEmptyArea(p);

                    for (Point ap : area) {
                        visited[ap.x()][ap.y()] = true;
                    }

                    Set<Stone> borders = getBorderColors(area);

                    if (borders.size() == 1) {
                        Stone owner = borders.iterator().next();
                        if (owner == Stone.BLACK) {
                            blackTerritory += area.size();
                        } else if (owner == Stone.WHITE) {
                            whiteTerritory += area.size();
                        }
                    }
                }
            }
        }
        return new TerritoryCount(blackTerritory, whiteTerritory);
    }

    /**
     * Tworzy kopię aktualnego stanu planszy.
     *
     * @return kopia stanu planszy
     */
    private Stone[][] copyGrid() {
        Stone[][] copy = new Stone[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                copy[x][y] = grid[x][y];
            }
        }
        return copy;
    }

    /**
     * Porównuje dwie siatki planszy pod kątem równości. (do reguły Ko)
     *
     * @param a pierwsza siatka
     * @param b druga siatka
     * @return true jeśli siatki są identyczne, false w przeciwnym razie
     */
    private boolean gridsEqual(Stone[][] a, Stone[][] b) {
        if (a == null || b == null) return false;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (a[x][y] != b[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Czyści stan reguły Ko, pozwalając na wcześniej zabronione ruchy.
     * Wywoływane gdy gracz pasuje.
     */
    public void clearKo() {
        previousGrid = null;
    }

    /**
     * Konwertuje planszę na ASCII do wyświetlania w konsoli. (do testów)
     *
     * @return tekstowa reprezentacja planszy ze współrzędnymi
     */
    public String toAscii() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("   ");
        for (int x = 0; x < size; x++) {
            stringBuilder.append(String.format("%-3s", (char)('A' + x)));
        }
        stringBuilder.append("\n");

        for (int y = 0; y < size; y++) {
            stringBuilder.append(String.format("%2d ", y + 1));

            for (int x = 0; x < size; x++) {
                Stone color = grid[x][y];
                String symbol =
                        color == Stone.BLACK ? "B" :
                                color == Stone.WHITE ? "W" : "+";

                stringBuilder.append(String.format("%-3s", symbol));
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Zwraca rozmiar planszy.
     *
     * @return rozmiar planszy
     */
    public int getSize() {
        return size;
    }

    /**
     * Zwraca kolor kamienia w danym punkcie.
     *
     * @param p pozycja do sprawdzenia
     * @return kolor kamienia w tej pozycji
     */
    public Stone getStone(Point p) {
        return grid[p.x()][p.y()];
    }

    /**
     * Zwraca liczbę kamieni zbitych w ostatnim ruchu.
     *
     * @return liczba zbitych kamieni
     */
    public int getLastCapturedCount() {
        return lastCapturedCount;
    }
}