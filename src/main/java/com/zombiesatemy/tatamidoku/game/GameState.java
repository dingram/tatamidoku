package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class GameState {
    private Layout mLayout;
    private final Deque<Placement> mPlacement = new ArrayDeque<>();

    public void reset() {
        mLayout = null;
        mPlacement.clear();
    }

    public Layout getLayout() {
        return mLayout;
    }

    public void setLayout(Layout layout) {
        reset();
        mLayout = layout;
    }

    public Placement getOriginalPlacement() {
        return mPlacement.peekLast();
    }

    public Placement getPlacement() {
        return mPlacement.peek();
    }

    public void pushPlacement(Placement placement) {
        mPlacement.push(placement);
    }

    public Placement popPlacement() {
        if (!canPop()) {
            throw new IllegalStateException("No more Placements to pop");
        }
        return mPlacement.pop();
    }

    public boolean canPop() {
        return mPlacement.size() > 1;
    }

    public void setPlacement(Placement placement) {
        mPlacement.clear();
        mPlacement.push(placement);
    }

    public boolean isValid() {
        final int[][] values = getPlacement().getAllValuesCopy();
        final int groupSize = getLayout().getGroupSize();
        final int maxPerColumn = getLayout().getGroupCount();
        return validateColumns(values, groupSize, maxPerColumn)
                && validateRows(values, groupSize, maxPerColumn)
                && validateNeighbours(values)
                && validateGroups(values);
    }

    private boolean validateColumns(int[][] values, int groupSize, int maxPerColumn) {
        for (int rowNum = 0; rowNum < values.length; rowNum++) {
            final int[] counts = new int[groupSize];
            for (final int[] column : values) {
                final int value = column[rowNum];
                if (value > 0 && ++counts[value - 1] > maxPerColumn) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateRows(int[][] values, int groupSize, int maxPerColumn) {
        for (final int[] column : values) {
            final int[] counts = new int[groupSize];
            for (final int value : column) {
                if (value > 0 && ++counts[value - 1] > maxPerColumn) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateNeighbours(int[][] values) {
        for (int col = 0, colCount = values.length; col < colCount; ++col) {
            for (int row = 0, rowCount = values[col].length; row < rowCount; ++row) {
                final int value = values[col][row];
                if (value <= 0) {
                    continue;
                }
                for (final CellCoordinate neighbour : getLayout().getOrthogonalNeighbours(col, row)) {
                    if (values[neighbour.getColumn()][neighbour.getRow()] == value) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean validateGroups(int[][] values) {
        List<Layout.Group> groups = getLayout().getGroups();
        for (final Layout.Group group : groups) {
            final int[] counts = new int[group.getSize()];
            for (final CellCoordinate cellCoordinate : group.getCellCoordinates()) {
                final int value = values[cellCoordinate.getColumn()][cellCoordinate.getRow()];
                if (value > 0 && counts[value - 1]++ > 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
