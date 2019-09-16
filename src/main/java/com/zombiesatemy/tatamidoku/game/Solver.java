package com.zombiesatemy.tatamidoku.game;

import java.util.Collection;
import java.util.Optional;

public interface Solver {
    /**
     * Generate solutions from the given layout and placement.
     *
     * @param layout Grid layout.
     * @param placement The placement to work from.
     *
     * @return Absent if the solver determined that a solution is impossible. Otherwise, a collection of the
     * immediate next moves that the solver can find (which may be empty). If multiple potential moves are returned,
     * they are alternative options.
     */
    Optional<Collection<Move>> getPossibleNextMoves(Layout layout, Placement placement);
}
