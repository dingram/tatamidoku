package com.zombiesatemy.tatamidoku.game;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    static class GroupImpl implements Layout.Group {
        private final int mFirstColumn;
        private final int mFirstRow;
        private final boolean mIsVertical;
        private final int mSize;

        public GroupImpl(int firstColumn, int firstRow, boolean isVertical, int size) {
            mFirstColumn = firstColumn;
            mFirstRow = firstRow;
            mIsVertical = isVertical;
            mSize = size;
        }

        @Override
        public int getFirstColumn() {
            return mFirstColumn;
        }

        @Override
        public int getFirstRow() {
            return mFirstRow;
        }

        @Override
        public boolean isVertical() {
            return mIsVertical;
        }

        @Override
        public int getSize() {
            return mSize;
        }

        @Override
        public List<CellCoordinate> getCellCoordinates() {
            final List<CellCoordinate> coords;
            if (mIsVertical) {
                coords = IntStream.range(0, mSize)
                        .mapToObj(row -> new CellCoordinate(mFirstColumn, mFirstRow + row))
                        .collect(Collectors.toList());
            } else {
                coords = IntStream.range(0, mSize)
                        .mapToObj(col -> new CellCoordinate(mFirstColumn + col, mFirstRow))
                        .collect(Collectors.toList());
            }
            return Collections.unmodifiableList(coords);
        }
    }
}
