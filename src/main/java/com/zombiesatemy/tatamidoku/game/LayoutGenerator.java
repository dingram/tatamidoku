package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LayoutGenerator {
    private static List<Layout.Group> groupsFromIdList(char[] idList, int groupSize) {
        int sideLength = (int) Math.sqrt(idList.length);
        if (sideLength * sideLength != idList.length) {
            throw new IllegalArgumentException("Input ID list must be square");
        }
        if (sideLength % groupSize != 0) {
            throw new IllegalArgumentException("Input ID list must be an exact multiple of the group size");
        }

        List<Layout.Group> groups = new ArrayList<>(idList.length);

        // Find horizontal groups
        char prevGroupId = 0;
        int currentGroupCount = 0;
        int groupStartCol = -1;
        int groupStartRow = -1;
        for (int y = 0; y < sideLength; ++y) {
            for (int x = 0; x < sideLength; ++x) {
                int arrayPos = x + y * sideLength;
                char currentGroupId = idList[arrayPos];
                if (currentGroupId == prevGroupId) {
                    // Continue current group
                    ++currentGroupCount;
                } else {
                    // See if previous group has ended
                    if (prevGroupId != 0) {
                        if (currentGroupCount == groupSize) {
                            groups.add(new LayoutImpl.GroupImpl(prevGroupId, groupStartCol, groupStartRow, false,
                                                                groupSize
                            ));
                        } else if (currentGroupCount != 1) {
                            throw new IllegalStateException(String.format(
                                    "Found a group %x of size %d when group size is %d",
                                    (int)prevGroupId,
                                    currentGroupCount,
                                    groupSize
                            ));
                        }
                    }
                    // Record the start of a new group
                    prevGroupId = currentGroupId;
                    currentGroupCount = 1;
                    groupStartCol = x;
                    groupStartRow = y;
                }
            }
            if (prevGroupId != 0) {
                if (currentGroupCount == groupSize) {
                    groups.add(new LayoutImpl.GroupImpl(prevGroupId, groupStartCol, groupStartRow, false,
                                                        groupSize
                    ));
                } else if (currentGroupCount != 1) {
                    throw new IllegalStateException(String.format(
                            "Found a group %x of size %d when group size is %d",
                            (int)prevGroupId,
                            currentGroupCount,
                            groupSize
                    ));
                }
            }
            // Reset at the end of the row.
            prevGroupId = 0;
        }

        // Find vertical groups
        prevGroupId = 0;
        currentGroupCount = 0;
        groupStartCol = -1;
        groupStartRow = -1;
        for (int x = 0; x < sideLength; ++x) {
            for (int y = 0; y < sideLength; ++y) {
                int arrayPos = x + y * sideLength;
                char currentGroupId = idList[arrayPos];
                if (currentGroupId == prevGroupId) {
                    // Continue current group
                    ++currentGroupCount;
                } else {
                    // See if previous group has ended
                    if (prevGroupId != 0) {
                        if (currentGroupCount == groupSize) {
                            groups.add(new LayoutImpl.GroupImpl(prevGroupId, groupStartCol, groupStartRow, true,
                                                                groupSize
                            ));
                        } else if (currentGroupCount != 1) {
                            throw new IllegalStateException(String.format(
                                    "Found a group %x of size %d when group size is %d",
                                    (int)prevGroupId,
                                    currentGroupCount,
                                    groupSize
                            ));
                        }
                    }
                    // Record the start of a new group
                    prevGroupId = currentGroupId;
                    currentGroupCount = 1;
                    groupStartCol = x;
                    groupStartRow = y;
                }
            }
            if (prevGroupId != 0) {
                if (currentGroupCount == groupSize) {
                    groups.add(new LayoutImpl.GroupImpl(prevGroupId, groupStartCol, groupStartRow, true,
                                                        groupSize
                    ));
                } else if (currentGroupCount != 1) {
                    throw new IllegalStateException(String.format(
                            "Found a group %x of size %d when group size is %d",
                            (int)prevGroupId,
                            currentGroupCount,
                            groupSize
                    ));
                }
            }
            // Reset at the end of the row.
            prevGroupId = 0;
        }

        return groups;
    }

    public static Layout generateGroupless(int groupSize, int groupCount) {
        return new LayoutImpl(groupSize, groupCount, Collections.emptyList());
    }

    public static Layout generateAllHorizontalGroups(int groupSize, int groupCount) {
        final int sideLength = groupSize * groupCount;
        final char[] idList = new char[sideLength * sideLength];
        char groupId = 'a';
        for (int pos = 0; pos < idList.length; ++pos) {
            if (pos > 0 && pos % groupSize == 0) {
                ++groupId;
            }
            idList[pos] = groupId;
        }
        return new LayoutImpl(groupSize, groupCount, groupsFromIdList(idList, groupSize));
    }

    public static Layout generateAllVerticalGroups(int groupSize, int groupCount) {
        final int sideLength = groupSize * groupCount;
        final char[] idList = new char[sideLength * sideLength];
        char groupId = 'a';
        char groupOffset = 0;
        for (int pos = 0; pos < idList.length; ++pos) {
            if (pos > 0 && pos % (sideLength * groupSize) == 0) {
                ++groupOffset;
            }
            idList[pos] = (char) ((groupId + pos % sideLength) + groupOffset * sideLength);
        }
        return new LayoutImpl(groupSize, groupCount, groupsFromIdList(idList, groupSize));
    }
}
