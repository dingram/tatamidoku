package com.zombiesatemy.tatamidoku.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemCounter<K> {
    private static final Integer ZERO = 0;
    private int mTotal = 0;
    private final Map<K, Integer> mCounts = new HashMap<>();

    public void increment(K key) {
        mCounts.put(key, get(key) + 1);
        ++mTotal;
    }

    public void decrement(K key) {
        mCounts.put(key, get(key) - 1);
        --mTotal;
    }

    public int get(K key) {
        return mCounts.getOrDefault(key, ZERO);
    }

    public void clear() {
        mCounts.clear();
        mTotal = 0;
    }

    public Set<K> allKeys() {
        return mCounts.keySet();
    }

    public int getTotal() {
        return mTotal;
    }
}
