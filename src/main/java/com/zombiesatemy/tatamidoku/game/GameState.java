package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Layout mLayout;
    private Placement mPlacement;

    public Layout getLayout() {
        return mLayout;
    }

    public void setLayout(Layout layout) {
        mLayout = layout;
    }

    public Placement getPlacement() {
        return mPlacement;
    }

    public void setPlacement(Placement placement) {
        mPlacement = placement;
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

    public List<CellCoordinate> getOrthogonalNeighbours(int col, int row) {
        final int sideLength = getLayout().getSideLength();
        List<CellCoordinate> result = new ArrayList<>(4);
        if (col > 0) {
            result.add(new CellCoordinate(col - 1, row));
        }
        if (row > 0) {
            result.add(new CellCoordinate(col, row - 1));
        }
        if (col < sideLength - 1) {
            result.add(new CellCoordinate(col + 1, row));
        }
        if (row < sideLength - 1) {
            result.add(new CellCoordinate(col, row + 1));
        }
        return result;
    }

    private boolean validateNeighbours(int[][] values) {
        for (int col = 0, colCount = values.length; col < colCount; ++col) {
            for (int row = 0, rowCount = values[col].length; row < rowCount; ++row) {
                final int value = values[col][row];
                if (value <= 0) {
                    continue;
                }
                List<CellCoordinate> neighbours = getOrthogonalNeighbours(col, row);
                for (final CellCoordinate neighbour : neighbours) {
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
