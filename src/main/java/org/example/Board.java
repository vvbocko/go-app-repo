package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Board {
    private final Stone[][] grid;
    private final int size;
    private Stone[][] previousGrid;

    private int lastCapturedCount = 0;

    public Board(int size) {
        this.grid = new Stone[size][size];
        this.size = size;
        initialiseBoard();
    }

    private void initialiseBoard() {
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size; y++) {
                grid[x][y] = Stone.NONE;
            }
        }
    }

    public boolean isEmpty(Point point) {
        return grid[point.x()][point.y()] == Stone.NONE;
    }

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

    private List<Point> getGroup(Point startPoint) {
        // znajdź start point odpowiedniego koloru i od niego przeszukaj getNeighbours()
        // następnie zapisz points do listy checkedList, i points do listy toBeCheckedList
        List<Point> group = new ArrayList<>();

        Stone color = grid[startPoint.x()][startPoint.y()];
        if (color == Stone.NONE){
            return group;
        }

        boolean[][] visited = new boolean[size][size];
        Queue<Point> toBeVisited = new LinkedList<>();
        toBeVisited.add(startPoint);

        while(!toBeVisited.isEmpty()) {
            Point point = toBeVisited.poll(); //poll() - pobiera i usuwa element z kolejki

            if (visited[point.x()][point.y()]) { // zabezpieczenie bo ten sam punkt mógł zostać dodany z kilku sąsiadów
                continue;
            }
            visited[point.x()][point.y()] = true; // zaznaczamy jako odwiedzone i dodajemy do grupy
            group.add(point);

            for (Point n : getNeighbours(point)) {  //spośrod sąsiadów punktu
                if (visited[n.x()][n.y()]) {
                    continue;
                }
                if (grid[n.x()][n.y()] == color) { //sprawdzamy czy ma ten sam kolor co startPoint
                    toBeVisited.add(n);
                }
            }

        }
        return group;
    }

    private int countBreaths(List<Point> group) {
        Set<Point> breaths = new HashSet<>(); // biore Set bo jako zbiór bedzie ignorował duplikaty

        for (Point points : group) {
            for (Point n : getNeighbours(points)) {
                if (grid[n.x()][n.y()] == Stone.NONE) { // jak sąsiad jest pusty - dodaj oddech
                    breaths.add(n);
                }
            }
        }
        return breaths.size();
    }

    private int removeGroup(List<Point> group) {
        for (Point points : group) {
            grid[points.x()][points.y()] = Stone.NONE;
        }
        return group.size(); // bedzie zwracac liczbe jencow
    }

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
            lastCapturedCount += removeGroup(enemiesToKill); // ile kamieni zostalo zbitych w konkretnym ruchu
        }

        List<Point> myGroup = getGroup(point); //do sprawdzenia czy kamien nie wejdzie w "martwe pole"
        int myBreaths = countBreaths(myGroup);

        if (myBreaths == 0 && enemyGroupToKill.isEmpty()) // ruch na martwe pole jest okej jeżeli przy tym zabijamy kamienie przeciwnika
        {
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


    private Stone[][] copyGrid() {
        Stone[][] copy = new Stone[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                copy[x][y] = grid[x][y];
            }
        }
        return copy;
    }

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

    public void clearKo() {
        previousGrid = null;
    }


    public String toAscii() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("   ");
        for (int x = 0; x < size; x++) {
            stringBuilder.append(String.format("%-3s", (char)('A' + x)));
        }
        stringBuilder.append("\n");

        for (int y = 0; y < size; y++) {
            stringBuilder.append(String.format("%2d ", y + 1)); //zwsze szerokie na 2 znaki

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

    public int getSize() {
        return size;
    }

    public Stone getStone(Point p) {
        return grid[p.x()][p.y()];
    }

    public int getLastCapturedCount() {
        return lastCapturedCount;
    }
}
