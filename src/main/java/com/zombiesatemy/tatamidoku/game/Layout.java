package com.zombiesatemy.tatamidoku.game;

import java.util.List;

public interface Layout {
    int getSideLength();
    int getGroupSize();
    List<Group> getGroups();

    interface Group {
        int getFirstColumn();
        int getFirstRow();
        boolean isVertical();
        int getSize();
    }
}
