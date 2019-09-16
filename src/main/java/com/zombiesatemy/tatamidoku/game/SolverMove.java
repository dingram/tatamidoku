package com.zombiesatemy.tatamidoku.game;

public class SolverMove implements Move {
    private final int mColumn;
    private final int mRow;
    private final int mValue;

    SolverMove(CellCoordinate coordinate, int value) {
        this(coordinate.getColumn(), coordinate.getRow(), value);
    }

    SolverMove(int column, int row, int value) {
        mColumn = column;
        mRow = row;
        mValue = value;
    }

    @Override
    public int getColumn() {
        return mColumn;
    }

    @Override
    public int getRow() {
        return mRow;
    }

    @Override
    public CellCoordinate getCoordinate() {
        return new CellCoordinate(mColumn, mRow);
    }

    @Override
    public int getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return String.format("Place %d at (%d, %d)", mValue, mColumn, mRow);
    }
}
