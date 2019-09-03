package com.zombiesatemy.tatamidoku.game;

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
        return true;
    }
}
