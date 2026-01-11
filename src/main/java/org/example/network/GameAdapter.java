package org.example.network;

import org.example.Point;
import org.example.Stone;

public interface GameAdapter {
    void playMove(Point p);
    void pass();
    Stone getColor();

}
