package com.zombiesatemy.tatamidoku.game;

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
        return validateColumns(values, groupSize, maxPerColumn) && validateRows(values, groupSize, maxPerColumn) && validateGroups(values);
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
