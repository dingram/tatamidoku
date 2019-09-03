package com.zombiesatemy.tatamidoku.game;

import java.util.List;

public interface Layout {
    int getGroupCount();
    int getGroupSize();
    int getSideLength();
    List<Group> getGroups();

    interface Group {
        int getFirstColumn();
        int getFirstRow();
        boolean isVertical();
        int getSize();
        List<CellCoordinate> getCellCoordinates();
    }
}
