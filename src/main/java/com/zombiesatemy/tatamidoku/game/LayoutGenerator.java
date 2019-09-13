package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                if (currentGroupId == 0) {
                    throw new IllegalArgumentException("Group ID cannot be zero");
                }
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
            // Add end-of-row group.
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
                if (currentGroupId == 0) {
                    throw new IllegalArgumentException("Group ID cannot be zero");
                }
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
            // Add end-of-column group.
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
            // Reset at the end of the column.
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
        char groupId = 1;
        for (int pos = 0; pos < idList.length; ++pos) {
            if (pos > 0 && pos % groupSize == 0) {
                ++groupId;
            }
            idList[pos] = groupId;
        }
        return fromGroupIds(idList, groupSize, groupCount);
    }

    public static Layout generateAllVerticalGroups(int groupSize, int groupCount) {
        final int sideLength = groupSize * groupCount;
        final char[] idList = new char[sideLength * sideLength];
        char groupId = 1;
        char groupOffset = 0;
        for (int pos = 0; pos < idList.length; ++pos) {
            if (pos > 0 && pos % (sideLength * groupSize) == 0) {
                ++groupOffset;
            }
            idList[pos] = (char) ((groupId + pos % sideLength) + groupOffset * sideLength);
        }
        return fromGroupIds(idList, groupSize, groupCount);
    }

    public static Layout fromGroupIds(String groupIds) {
        return fromGroupIds(groupIds.toCharArray());
    }

    public static Layout fromGroupIds(char[] groupIds) {
        final int sideLength = (int) Math.sqrt(groupIds.length);
        final Map<Character, Integer> groupSizes = new HashMap<>();
        for (final char groupId : groupIds) {
            int count = groupSizes.getOrDefault(groupId, 0);
            groupSizes.put(groupId, count + 1);
        }
        int groupSize = 0;
        for (final Map.Entry<Character, Integer> groupSizeInfo : groupSizes.entrySet()) {
            if (groupSize == 0) {
                groupSize = groupSizeInfo.getValue();
            } else if (groupSize != groupSizeInfo.getValue()) {
                throw new IllegalStateException(String.format(
                        "Group %c has size %d, which differs from detected size %d",
                        groupSizeInfo.getKey(), groupSizeInfo.getValue(), groupSize));
            }
        }
        int groupCount = sideLength / groupSize;
        if (sideLength != groupCount * groupSize) {
            throw new IllegalStateException(String.format(
                    "Inconsistent layout! Side length %d != group size %d * group count %d",
                    sideLength, groupSize, groupCount));
        }
        return fromGroupIds(groupIds, groupSize, groupCount);
    }

    public static Layout fromGroupIds(String groupIds, int groupSize, int groupCount) {
        return fromGroupIds(groupIds.toCharArray(), groupSize, groupCount);
    }

    public static Layout fromGroupIds(char[] groupIds, int groupSize, int groupCount) {
        return new LayoutImpl(groupSize, groupCount, groupsFromIdList(groupIds, groupSize));
    }
}
