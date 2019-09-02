package com.zombiesatemy.tatamidoku.game;

public class GameState {
    private Layout mLayout;
    private Placement mPlacement;

    public Layout getLayout() {
        return mLayout;
    }

    public GameState setLayout(Layout layout) {
        mLayout = layout;
        return this;
    }

    public Placement getPlacement() {
        return mPlacement;
    }

    public GameState setPlacement(Placement placement) {
        mPlacement = placement;
        return this;
    }

    public boolean isValid() {
        return false;
    }
}
