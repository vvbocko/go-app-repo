package org.example;

public class BoardUpdater {

    private final Board board;

    public BoardUpdater(Board board) {
        this.board = board;
    }

    public void updateFromAscii(String ascii) {
        if (ascii == null || ascii.isBlank()) return;
        String[] lines = ascii.split("\n");

        if (lines.length < board.getSize() + 1) {
            System.out.println("Invalid board ascii:\n" + ascii);
            return;
        }

        for (int y = 0; y < board.getSize(); y++) {
            String line = lines[y + 1];

            if (line.length() < 3) continue;

            String row = line.substring(3);
            String[] cells = row.trim().split("\\s+");

            if (cells.length != board.getSize()) continue;

            for (int x = 0; x < board.getSize(); x++) {
                Stone s = switch (cells[x]) {
                    case "B" -> Stone.BLACK;
                    case "W" -> Stone.WHITE;
                    default -> Stone.NONE;
                };
                board.setStone(new Point(x, y), s);
            }
        }
    }

}

