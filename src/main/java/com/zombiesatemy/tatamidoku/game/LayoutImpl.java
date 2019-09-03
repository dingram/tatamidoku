package com.zombiesatemy.tatamidoku.game;

import java.util.Collections;
import java.util.List;

public class LayoutImpl implements Layout {
    private final int mGroupSize;
    private final int mGroupCount;
    private final List<Group> mGroups;
    private final int mSideLength;

    LayoutImpl(int groupSize, int groupCount, List<Group> groups) {
        mGroupSize = groupSize;
        mGroupCount = groupCount;
        mGroups = Collections.unmodifiableList(groups);
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
        return mGroups;
    }
}
