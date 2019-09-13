package com.zombiesatemy.tatamidoku.game;

import java.util.Arrays;

public final class PlacementImpl implements Placement {
    private final int[][] mValues;

    private PlacementImpl(int[][] values) {
        mValues = values;
    }

    public static Placement fromSideLength(int sideLength) {
        return new PlacementImpl(new int[sideLength][sideLength]);
    }

    private static Placement fromValuesWithoutCopy(int[][] values) {
        return new PlacementImpl(values);
    }

    public static Placement fromValues(String values) {
        final int ZERO = '0';
        return fromValues(values.chars().map(i -> {
            if (i == '.') {
                return 0;
            }
            if (i < ZERO || i > ZERO + 9) {
                throw new IllegalArgumentException(i + " is out of range");
            }
            return i - ZERO;
        }).toArray());
    }

    public static Placement fromValues(int[] values) {
        // Ensure that the array is square.
        final int sideLength = (int) Math.sqrt(values.length);
        if (values.length != sideLength * sideLength) {
            throw new IllegalArgumentException("Values must be a square array");
        }
        int[][] valueMatrix = new int[sideLength][sideLength];
        for (int i = 0; i < values.length; i++) {
            valueMatrix[i % sideLength][i / sideLength] = values[i];
        }
        return fromValuesWithoutCopy(valueMatrix);
    }

    public static Placement fromValues(int[][] values) {
        // Ensure that the array is square.
        for (final int[] column : values) {
            if (column.length != values.length) {
                throw new IllegalArgumentException("Values must be a square array");
            }
        }
        // Copy the values array to be sure that we own it.
        return fromValuesWithoutCopy(Arrays.stream(values).map(int[]::clone).toArray(int[][]::new));
    }

    public static Placement fromBuilder(Placement.Builder builder) {
        return fromValues(builder.toValuesUnsafe());
    }

    @Override
    public int getSideLength() {
        return mValues.length;
    }

    @Override
    public int getValueAt(int column, int row) {
        return mValues[column][row];
    }

    @Override
    public int[][] getAllValuesCopy() {
        return Arrays.stream(mValues).map(int[]::clone).toArray(int[][]::new);
    }

    @Override
    public Placement withNoValueAt(int column, int row) {
        return withValueAt(column, row, 0);
    }

    @Override
    public Placement withValueAt(int column, int row, int value) {
        final int[][] values = getAllValuesCopy();
        values[column][row] = value;
        return fromValuesWithoutCopy(values);
    }
}
