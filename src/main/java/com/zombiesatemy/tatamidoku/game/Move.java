package com.zombiesatemy.tatamidoku.game;

public interface Move {
    int getColumn();
    int getRow();
    CellCoordinate getCoordinate();
    int getValue();
}
