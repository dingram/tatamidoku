package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LayoutImpl implements Layout {
    private final int mGroupSize;
    private final int mGroupCount;
    private final List<Group> mGroups;
    private final int mSideLength;
    private final char[][] mGroupIdCache;
    private final Map<Character, Group> mGroupMap;

    LayoutImpl(int groupSize, int groupCount, List<Group> groups) {
        mGroupSize = groupSize;
        mGroupCount = groupCount;
        mGroups = Collections.unmodifiableList(groups);
        mSideLength = groupCount * groupSize;
        mGroupIdCache = new char[mSideLength][mSideLength];
        mGroupMap = new HashMap<>();
        cacheGroupLookups();
    }

    private void cacheGroupLookups() {
        for (final Group group : mGroups) {
            final char groupId = group.getId();
            if (mGroupMap.containsKey(groupId)) {
                throw new IllegalStateException(String.format("Multiple groups with the same ID %s", groupId));
            }
            mGroupMap.put(groupId, group);

            for (final CellCoordinate cellCoordinate : group.getCellCoordinates()) {
                if (mGroupIdCache[cellCoordinate.getColumn()][cellCoordinate.getRow()] != 0) {
                    throw new IllegalStateException(String.format(
                            "Groups %s and %s overlap at (%d, %d)",
                            mGroupIdCache[cellCoordinate.getColumn()][cellCoordinate.getRow()],
                            groupId,
                            cellCoordinate.getColumn(),
                            cellCoordinate.getRow()));
                }
                mGroupIdCache[cellCoordinate.getColumn()][cellCoordinate.getRow()] = groupId;
            }
        }
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

    @Override
    public Optional<Group> getGroupById(char id) {
        return Optional.ofNullable(mGroupMap.get(id));
    }

    @Override
    public Collection<CellCoordinate> getOrthogonalNeighbours(int column, int row) {
        final int sideLength = getSideLength();
        List<CellCoordinate> result = new ArrayList<>(4);
        if (column > 0) {
            result.add(new CellCoordinate(column - 1, row));
        }
        if (row > 0) {
            result.add(new CellCoordinate(column, row - 1));
        }
        if (column < sideLength - 1) {
            result.add(new CellCoordinate(column + 1, row));
        }
        if (row < sideLength - 1) {
            result.add(new CellCoordinate(column, row + 1));
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public Collection<CellCoordinate> getOrthogonalNeighbours(CellCoordinate coordinate) {
        return getOrthogonalNeighbours(coordinate.getColumn(), coordinate.getRow());
    }

    @Override
    public char getGroupIdAt(CellCoordinate coordinate) {
        return getGroupIdAt(coordinate.getColumn(), coordinate.getRow());
    }

    @Override
    public char getGroupIdAt(int column, int row) {
        return mGroupIdCache[column][row];
    }

    static class GroupImpl implements Layout.Group {
        private final char mId;
        private final int mFirstColumn;
        private final int mFirstRow;
        private final boolean mIsVertical;
        private final int mSize;

        public GroupImpl(char id, int firstColumn, int firstRow, boolean isVertical, int size) {
            mId = id;
            mFirstColumn = firstColumn;
            mFirstRow = firstRow;
            mIsVertical = isVertical;
            mSize = size;
        }

        @Override
        public char getId() {
            return mId;
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
        public Collection<CellCoordinate> getCellCoordinates() {
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
