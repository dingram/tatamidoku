package com.zombiesatemy.tatamidoku.game;

import java.util.Arrays;

public interface Placement {
    int getSideLength();
    int getValueAt(CellCoordinate coordinate);
    int getValueAt(int column, int row);
    int[][] getAllValuesCopy();
    Placement withNoValueAt(CellCoordinate coordinate);
    Placement withNoValueAt(int column, int row);
    Placement withValueAt(CellCoordinate coordinate, int value);
    Placement withValueAt(int column, int row, int value);

    class Builder {
        private final int[][] mValues;

        public Builder(int sideLength) {
            mValues = new int[sideLength][sideLength];
        }

        public Builder(Placement placement) {
            mValues = placement.getAllValuesCopy();
        }

        public Builder withValueAt(CellCoordinate coordinate, int value) {
            mValues[coordinate.getColumn()][coordinate.getRow()] = value;
            return this;
        }

        public Builder withValueAt(int column, int row, int value) {
            mValues[column][row] = value;
            return this;
        }

        public int[][] toValuesCopy() {
            return Arrays.stream(mValues).map(int[]::clone).toArray(int[][]::new);
        }

        int[][] toValuesUnsafe() {
            return mValues;
        }
    }
}
