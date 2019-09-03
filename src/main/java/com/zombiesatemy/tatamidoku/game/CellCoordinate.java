package com.zombiesatemy.tatamidoku.game;

public class CellCoordinate {
    private final int mColumn;
    private final int mRow;

    public CellCoordinate(int column, int row) {
        mColumn = column;
        mRow = row;
    }

    public int getColumn() {
        return mColumn;
    }

    public int getRow() {
        return mRow;
    }
}
