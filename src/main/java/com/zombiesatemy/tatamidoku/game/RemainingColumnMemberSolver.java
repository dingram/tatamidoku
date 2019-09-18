package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RemainingColumnMemberSolver implements Solver {
    @Override
    public Optional<Collection<Move>> getPossibleNextMoves(Layout layout, Placement placement) {
        final int groupCount = layout.getGroupCount();
        final int sideLength = placement.getSideLength();
        List<Move> moves = new ArrayList<>();
        ItemCounter<Integer> counter = new ItemCounter<>();
        BitSet places = new BitSet(sideLength);

        for (int col = 0; col < sideLength; ++col) {
            places.clear();
            counter.clear();
            for (int row = 0; row < sideLength; ++row) {
                final int value = placement.getValueAt(col, row);
                if (value > 0) {
                    counter.increment(value);
                }
                places.set(row, value != 0);
            }

            final int missingValueCount = sideLength - places.cardinality();
            if (missingValueCount == 0 || missingValueCount > 2) {
                // If the column is full or there are more than two missing values, we know there is nothing we can do.
                continue;
            }

            final Set<Integer> missingValues =
                    counter.allKeys().stream().filter(k -> counter.get(k) < groupCount).collect(Collectors.toSet());

            if (missingValues.size() != 1) {
                // More than one missing value; nothing we can do.
                continue;
            }
            final Integer missingValue = missingValues.stream().findAny().get();

            // Place values into the empty location(s) in this column.
            for (int row = places.nextClearBit(0); row < sideLength; row = places.nextClearBit(row+1)) {
                moves.add(new SolverMove(getClass(), col, row, missingValue));
            }
        }

        return Optional.of(moves);
    }
}
