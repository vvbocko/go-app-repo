package org.example;

public class Board {
    private final Stone[][] grid;
    private final int size;

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

    public MoveResult placeStone(Point point, Stone stone) {
        if(!isEmpty(point)) {
            return MoveResult.OCCUPIED;
        }
        grid[point.x()][point.y()] = stone;
        return MoveResult.OK;
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
}
