package com.zombiesatemy.tatamidoku.game;

public interface Move {
    Class<? extends Solver> getSource();
    int getColumn();
    int getRow();
    CellCoordinate getCoordinate();
    int getValue();
}
