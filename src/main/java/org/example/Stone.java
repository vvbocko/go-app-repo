package org.example;

public enum Stone {
    BLACK, WHITE, NONE;

    public Stone opposite() {
        if (this == BLACK) return WHITE;
        if (this == WHITE) return BLACK;
        return NONE;
    }
}
