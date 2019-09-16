package com.zombiesatemy.tatamidoku.game;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RemainingGroupMemberSolver implements Solver {
    @Override
    public Optional<Collection<Move>> getPossibleNextMoves(Layout layout, Placement placement) {
        final int groupSize = layout.getGroupSize();
        final int sideLength = placement.getSideLength();
        List<Move> moves = new ArrayList<>();

        // Calculate the values that are present for a group
        Map<Character, BitSet> groupState = new HashMap<>();
        for (int col = 0; col < sideLength; ++col) {
            for (int row = 0; row < sideLength; ++row) {
                // Subtract one to get the bit that we should set.
                int v = placement.getValueAt(col, row) - 1;
                if (v < 0) {
                    // Skip empty cells.
                    continue;
                }
                char g = layout.getGroupIdAt(col, row);
                BitSet bits = groupState.get(g);
                if (bits == null) {
                    bits = new BitSet(groupSize);
                    groupState.put(g, bits);
                }
                if (bits.get(v)) {
                    // We have somehow got the same value twice in a group; this is impossible.
                    return Optional.empty();
                }
                bits.set(v);
            }
        }

        // Check groups to see if any have a single missing value.
        for (final Map.Entry<Character, BitSet> entry : groupState.entrySet()) {
            final BitSet bits = entry.getValue();
            if (bits.cardinality() != groupSize - 1) {
                continue;
            }
            // One element missing! Let's add a move.
            //noinspection OptionalGetWithoutIsPresent
            final Layout.Group group = layout.getGroupById(entry.getKey()).get();
            for (final CellCoordinate cc : group.getCellCoordinates()) {
                if (placement.getValueAt(cc) == 0) {
                    moves.add(new SolverMove(cc.getColumn(), cc.getRow(), bits.nextClearBit(0) + 1));
                }
            }
        }

        return Optional.of(moves);
    }
}
