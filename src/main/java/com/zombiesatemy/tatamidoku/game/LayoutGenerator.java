package com.zombiesatemy.tatamidoku.game;

import java.util.Collections;

public class LayoutGenerator {
    public static Layout generateGroupless(int groupSize, int groupCount) {
        return new LayoutImpl(groupSize, groupCount, Collections.emptyList());
    }
}
