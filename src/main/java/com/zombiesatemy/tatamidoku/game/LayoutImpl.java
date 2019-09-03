package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayList;
import java.util.List;

public class LayoutImpl implements Layout {
    private final int mGroupSize;
    private final int mGroupCount;
    private final int mSideLength;

    public LayoutImpl(int groupSize, int groupCount) {
        mGroupSize = groupSize;
        mGroupCount = groupCount;
        mSideLength = groupCount * groupSize;
    }

    @Override
    public int getGroupSize() {
        return mGroupSize;
    }

    @Override
    public int getGroupCount() {
        return mGroupCount;
    }

    @Override
    public int getSideLength() {
        return mSideLength;
    }

    @Override
    public List<Group> getGroups() {
        return new ArrayList<>();
    }
}
