package com.zombiesatemy.tatamidoku.game;

public class PlacementImpl implements Placement {
    private final int mSideLength;

    public PlacementImpl(int sideLength) {
        mSideLength = sideLength;
    }

    @Override
    public int getSideLength() {
        return mSideLength;
    }

    @Override
    public int getValueAt(int column, int row) {
        return ((column + row) % (mSideLength / 2 + 1));
    }
}
