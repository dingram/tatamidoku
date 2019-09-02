package com.zombiesatemy.tatamidoku.cli;

import com.zombiesatemy.tatamidoku.game.GameState;
import com.zombiesatemy.tatamidoku.game.Placement;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.PrintWriter;

public class TextRenderer {
    private final Terminal mTerminal;
    private static final String CORNER_TL = "┏";
    private static final String CORNER_TR = "┓";
    private static final String CORNER_BR = "┛";
    private static final String CORNER_BL = "┗";

    private static final String H_LINE_LIGHT = "─";
    private static final String H_LINE_HEAVY = "━";
    private static final String V_LINE_LIGHT = "│";
    private static final String V_LINE_HEAVY = "┃";

    private static final String H_BORDER_TOP_LIGHT = "┯";
    private static final String H_BORDER_TOP_HEAVY = "┳";
    private static final String H_BORDER_BOTTOM_LIGHT = "┷";
    private static final String H_BORDER_BOTTOM_HEAVY = "┻";
    private static final String V_BORDER_LEFT_LIGHT = "┠";
    private static final String V_BORDER_LEFT_HEAVY = "┣";
    private static final String V_BORDER_RIGHT_LIGHT = "┨";
    private static final String V_BORDER_RIGHT_HEAVY = "┫";

    private static final String INSIDE_BORDER_tblr = "┼";
    private static final String INSIDE_BORDER_tbLr = "┽";
    private static final String INSIDE_BORDER_tblR = "┾";
    private static final String INSIDE_BORDER_tbLR = "┿";
    private static final String INSIDE_BORDER_Tblr = "╀";
    private static final String INSIDE_BORDER_tBlr = "╁";
    private static final String INSIDE_BORDER_TBlr = "╂";
    private static final String INSIDE_BORDER_TbLr = "╃";
    private static final String INSIDE_BORDER_TblR = "╄";
    private static final String INSIDE_BORDER_tBLr = "╅";
    private static final String INSIDE_BORDER_tBlR = "╆";
    private static final String INSIDE_BORDER_TbLR = "╇";
    private static final String INSIDE_BORDER_tBLR = "╈";
    private static final String INSIDE_BORDER_TBLr = "╉";
    private static final String INSIDE_BORDER_TBlR = "╊";
    private static final String INSIDE_BORDER_TBLR = "╋";

    public TextRenderer(Terminal terminal) {
        mTerminal = terminal;
    }

    public void render(GameState gameState) {
        final PrintWriter writer = mTerminal.writer();
        final AttributedStringBuilder builder = new AttributedStringBuilder();

        builder.style(AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.RED))
                .append("Rendering not supported yet.")
                .style(AttributedStyle.DEFAULT)
                .append("\n");

        final int sideLen = gameState.getLayout().getSideLength();
        final Placement placement = gameState.getPlacement();
        for (int y = 0; y < sideLen; ++y) {
            // border above cell
            for (int x = 0; x < sideLen; ++x) {
                if (x == 0 && y == 0) {
                    builder.append(CORNER_TL);
                } else if (x == 0) {
                    builder.append(V_BORDER_LEFT_LIGHT);
                } else if (y == 0) {
                    builder.append(H_BORDER_TOP_LIGHT);
                } else {
                    builder.append(INSIDE_BORDER_tblr);
                }
                if (y == 0) {
                    builder.append(H_LINE_HEAVY + H_LINE_HEAVY + H_LINE_HEAVY);
                } else {
                    builder.append(H_LINE_LIGHT + H_LINE_LIGHT + H_LINE_LIGHT);
                }
            }
            if (y == 0) {
                builder.append(CORNER_TR);
            } else {
                builder.append(V_BORDER_RIGHT_LIGHT);
            }
            builder.append("\n");

            // cell contents
            for (int x = 0; x < sideLen; ++x) {
                if (x == 0) {
                    builder.append(V_LINE_HEAVY);
                } else {
                    builder.append(V_LINE_LIGHT);
                }

                final int v = placement.getValueAt(x, y);
                if (v > 0) {
                    builder.append(" ").append(String.valueOf(v)).append(" ");
                } else {
                    builder.append("   ");
                }
                if (x == sideLen - 1) {
                    builder.append(V_LINE_HEAVY);
                }
            }
            builder.append("\n");

            // border below cell
            if (y == sideLen - 1) {
                for (int x = 0; x < sideLen; ++x) {
                    if (x == 0) {
                        builder.append(CORNER_BL);
                    } else {
                        builder.append(H_BORDER_BOTTOM_LIGHT);
                    }
                    builder.append(H_LINE_HEAVY + H_LINE_HEAVY + H_LINE_HEAVY);
                }
                builder.append(CORNER_BR + "\n");
            }
        }

        writer.println(builder.toAnsi(mTerminal));
    }
}
