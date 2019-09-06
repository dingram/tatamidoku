package com.zombiesatemy.tatamidoku.cli;

import com.zombiesatemy.tatamidoku.game.GameState;
import com.zombiesatemy.tatamidoku.game.Layout;
import com.zombiesatemy.tatamidoku.game.Placement;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;

import java.io.PrintWriter;

public class TextRenderer {
    private static final char[] PRINTABLE_GROUP_IDS =
            "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final Terminal mTerminal;
    private static final String CORNER_TL = "┏";
    private static final String CORNER_TR = "┓";
    private static final String CORNER_BR = "┛";
    private static final String CORNER_BL = "┗";

    private static final String H_LINE_LIGHT = "─";
    private static final String H_LINE_HEAVY = "━";
    private static final String V_LINE_LIGHT = "│";
    private static final String V_LINE_HEAVY = "┃";

    private static final String H_EDGE_TOP_LIGHT = "┯";
    private static final String H_EDGE_TOP_HEAVY = "┳";
    private static final String H_EDGE_BOTTOM_LIGHT = "┷";
    private static final String H_EDGE_BOTTOM_HEAVY = "┻";
    private static final String V_EDGE_LEFT_LIGHT = "┠";
    private static final String V_EDGE_LEFT_HEAVY = "┣";
    private static final String V_EDGE_RIGHT_LIGHT = "┨";
    private static final String V_EDGE_RIGHT_HEAVY = "┫";

    private static final String INSIDE_CROSS_tblr = "┼";
    private static final String INSIDE_CROSS_tbLr = "┽";
    private static final String INSIDE_CROSS_tblR = "┾";
    private static final String INSIDE_CROSS_tbLR = "┿";
    private static final String INSIDE_CROSS_Tblr = "╀";
    private static final String INSIDE_CROSS_tBlr = "╁";
    private static final String INSIDE_CROSS_TBlr = "╂";
    private static final String INSIDE_CROSS_TbLr = "╃";
    private static final String INSIDE_CROSS_TblR = "╄";
    private static final String INSIDE_CROSS_tBLr = "╅";
    private static final String INSIDE_CROSS_tBlR = "╆";
    private static final String INSIDE_CROSS_TbLR = "╇";
    private static final String INSIDE_CROSS_tBLR = "╈";
    private static final String INSIDE_CROSS_TBLr = "╉";
    private static final String INSIDE_CROSS_TBlR = "╊";
    private static final String INSIDE_CROSS_TBLR = "╋";

    public TextRenderer(Terminal terminal) {
        mTerminal = terminal;
    }

    public void render(GameState gameState, boolean renderGroupId) {
        final PrintWriter writer = mTerminal.writer();
        final AttributedStringBuilder builder = new AttributedStringBuilder();

        final Layout layout = gameState.getLayout();
        final Placement placement = gameState.getPlacement();
        final int sideLen = layout.getSideLength();

        // Calculate border widths in advance.
        boolean[][] vBorders = new boolean[sideLen][sideLen];
        boolean[][] hBorders = new boolean[sideLen][sideLen];

        for (int col = 0; col < sideLen; ++col) {
            for (int row = 0; row < sideLen; ++row) {
                // vBorders[col][row] is whether there is a heavy (internal) border on the left of (col, row).
                vBorders[col][row] = col == 0 || layout.getGroupIdAt(col - 1, row) != layout.getGroupIdAt(col, row);
                // hBorders[col][row] is whether there is a heavy (internal) border above (col, row).
                hBorders[col][row] = row == 0 || layout.getGroupIdAt(col, row - 1) != layout.getGroupIdAt(col, row);
            }
        }

        final String[] crosses = {
                INSIDE_CROSS_tblr,
                INSIDE_CROSS_tblR,
                INSIDE_CROSS_tBlr,
                INSIDE_CROSS_tBlR,
                INSIDE_CROSS_tbLr,
                INSIDE_CROSS_tbLR,
                INSIDE_CROSS_tBLr,
                INSIDE_CROSS_tBLR,
                INSIDE_CROSS_Tblr,
                INSIDE_CROSS_TblR,
                INSIDE_CROSS_TBlr,
                INSIDE_CROSS_TBlR,
                INSIDE_CROSS_TbLr,
                INSIDE_CROSS_TbLR,
                INSIDE_CROSS_TBLr,
                INSIDE_CROSS_TBLR,
        };

        for (int y = 0; y < sideLen; ++y) {
            // border above cell
            for (int x = 0; x < sideLen; ++x) {
                if (x == 0 && y == 0) {
                    builder.append(CORNER_TL);
                } else if (x == 0) {
                    builder.append(hBorders[x][y] ? V_EDGE_LEFT_HEAVY : V_EDGE_LEFT_LIGHT);
                } else if (y == 0) {
                    builder.append(vBorders[x][y] ? H_EDGE_TOP_HEAVY : H_EDGE_TOP_LIGHT);
                } else {
                    // Select the appropriate "cross" glyph.
                    int crossType = 0;
                    crossType |= hBorders[x][y] ? 1 : 0; // is right heavy?
                    crossType |= vBorders[x][y] ? 2 : 0; // is bottom heavy?
                    crossType |= hBorders[x - 1][y] ? 4 : 0; // is left heavy?
                    crossType |= vBorders[x][y - 1] ? 8 : 0; // is top heavy?
                    builder.append(crosses[crossType]);
                }
                if (y == 0 || hBorders[x][y]) {
                    builder.append(H_LINE_HEAVY + H_LINE_HEAVY + H_LINE_HEAVY);
                } else {
                    builder.append(H_LINE_LIGHT + H_LINE_LIGHT + H_LINE_LIGHT);
                }
            }
            if (y == 0) {
                builder.append(CORNER_TR);
            } else {
                builder.append(hBorders[sideLen - 1][y] ? V_EDGE_RIGHT_HEAVY : V_EDGE_RIGHT_LIGHT);
            }
            builder.append("\n");

            // cell contents
            for (int x = 0; x < sideLen; ++x) {
                if (x == 0 || vBorders[x][y]) {
                    builder.append(V_LINE_HEAVY);
                } else {
                    builder.append(V_LINE_LIGHT);
                }

                final char groupId = layout.getGroupIdAt(x, y);
                final int v = placement.getValueAt(x, y);
                final String cellContents;
                if (renderGroupId) {
                    cellContents = groupId == 0 ? "⚠" : String.valueOf(PRINTABLE_GROUP_IDS[groupId - 1]);
                } else {
                    cellContents = v == 0 ? " " : String.valueOf(v);
                }
                builder.append(" ").append(cellContents).append(" ");
                if (x == sideLen - 1) {
                    builder.append(V_LINE_HEAVY);
                }
            }
            builder.append("\n");

            // bottom edge of entire grid
            if (y == sideLen - 1) {
                for (int x = 0; x < sideLen; ++x) {
                    if (x == 0) {
                        builder.append(CORNER_BL);
                    } else {
                        builder.append(vBorders[x][y] ? H_EDGE_BOTTOM_HEAVY : H_EDGE_BOTTOM_LIGHT);
                    }
                    builder.append(H_LINE_HEAVY + H_LINE_HEAVY + H_LINE_HEAVY);
                }
                builder.append(CORNER_BR + "\n");
            }
        }

        writer.println(builder.toAnsi(mTerminal));
    }
}
