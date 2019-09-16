package com.zombiesatemy.tatamidoku.game;

import java.util.List;
import java.util.Optional;

public interface Layout {
    int getGroupCount();
    int getGroupSize();
    int getSideLength();
    List<Group> getGroups();
    char getGroupIdAt(int column, int row);
    char getGroupIdAt(CellCoordinate coordinate);
    Optional<Group> getGroupById(char id);

    interface Group {
        char getId();
        int getFirstColumn();
        int getFirstRow();
        boolean isVertical();
        int getSize();
        List<CellCoordinate> getCellCoordinates();
    }
}
