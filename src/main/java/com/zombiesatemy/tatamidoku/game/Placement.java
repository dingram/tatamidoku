package com.zombiesatemy.tatamidoku.game;

public interface Placement {
    int getSideLength();
    int getValueAt(int column, int row);
}
