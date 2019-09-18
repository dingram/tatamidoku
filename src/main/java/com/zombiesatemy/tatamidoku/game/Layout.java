package com.zombiesatemy.tatamidoku.game;

import java.util.Collection;
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
    Collection<CellCoordinate> getOrthogonalNeighbours(int column, int row);
    Collection<CellCoordinate> getOrthogonalNeighbours(CellCoordinate coordinate);

    interface Group {
        char getId();
        int getFirstColumn();
        int getFirstRow();
        boolean isVertical();
        int getSize();
        Collection<CellCoordinate> getCellCoordinates();
    }
}
