package com.zombiesatemy.tatamidoku.game;

public class LayoutGenerator {
    public static Layout generateGroupless(int groupSize, int groupCount) {
        return new LayoutImpl(groupSize, groupCount);
    }
}
