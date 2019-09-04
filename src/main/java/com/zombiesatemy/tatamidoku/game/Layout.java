package com.zombiesatemy.tatamidoku.game;

import java.util.List;

public interface Layout {
    int getGroupCount();
    int getGroupSize();
    int getSideLength();
    List<Group> getGroups();
    char getGroupIdAt(int column, int row);

    interface Group {
        char getId();
        int getFirstColumn();
        int getFirstRow();
        boolean isVertical();
        int getSize();
        List<CellCoordinate> getCellCoordinates();
    }
}
