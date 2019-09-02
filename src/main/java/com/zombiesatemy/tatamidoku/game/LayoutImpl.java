package com.zombiesatemy.tatamidoku.game;

import java.util.List;

public class LayoutImpl implements Layout {
    private final int mSideLength;
    private final int mGroupSize;

    public LayoutImpl(int sideLength, int groupSize) {
        mSideLength = sideLength;
        mGroupSize = groupSize;
    }

    @Override
    public int getSideLength() {
        return mSideLength;
    }

    @Override
    public int getGroupSize() {
        return mGroupSize;
    }

    @Override
    public List<Group> getGroups() {
        return null;
    }
}
