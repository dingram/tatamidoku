package com.zombiesatemy.tatamidoku.game;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CellCoordinate that = (CellCoordinate) o;
        return mColumn == that.mColumn &&
                mRow == that.mRow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mColumn, mRow);
    }
}
