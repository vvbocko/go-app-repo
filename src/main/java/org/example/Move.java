package org.example;

public class Move {
    private int x;
    private int y;
    private Stone color;

    public Move(int x, int y, Stone color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public int getX() { 
        return x; 
    }
    public int getY() { 
        return y; 
    }
    public Stone getColor() { 
        return color; 
    }
}
