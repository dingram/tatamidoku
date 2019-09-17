package com.zombiesatemy.tatamidoku.game;

public class SolverMove implements Move {
    private final Class<? extends Solver> mSource;
    private final int mColumn;
    private final int mRow;
    private final int mValue;

    SolverMove(Class<? extends Solver> source, CellCoordinate coordinate, int value) {
        this(source, coordinate.getColumn(), coordinate.getRow(), value);
    }

    SolverMove(Class<? extends Solver> source, int column, int row, int value) {
        mSource = source;
        mColumn = column;
        mRow = row;
        mValue = value;
    }

    @Override
    public Class<? extends Solver> getSource() {
        return mSource;
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
        return String.format("%s: Place %d at (%d, %d)", mSource.getSimpleName(), mValue, mColumn, mRow);
    }
}
