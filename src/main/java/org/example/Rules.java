package org.example;

public interface Rules {
    MoveResult play(Board board, Move move);
}