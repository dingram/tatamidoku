package com.zombiesatemy.tatamidoku.game;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class RemainingColumnMemberSolver implements Solver {
    @Override
    public Optional<Collection<Move>> getPossibleNextMoves(Layout layout, Placement placement) {
        return Optional.of(Collections.emptyList());
    }
}
