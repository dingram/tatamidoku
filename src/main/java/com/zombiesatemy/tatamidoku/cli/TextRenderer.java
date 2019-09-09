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

    // Alphabet is, in order:
    //  • Grid corners: Top left, top right, bottom left, bottom right
    //  • Grid edges: Top, top light, top heavy, bottom, bottom light, bottom heavy, left, left light, left heavy,
    //    right, right light, right heavy.
    //  • Intra-cell lines: Horizontal light, horizontal heavy, vertical light, vertical heavy
    //  • Intra-cell crosses: counting in binary with 0 = light, 1 = heavy; digit order (top, left, bottom, right). So:
    //      all light, right heavy, bottom heavy, right and bottom heavy, ...
    private static final String ALPHABET_LIGHT_HEAVY = "┏┓┗┛━┯┳━┷┻┃┠┣┃┨┫─━│┃┼┾╁╆┽┿╅╈╀╄╂╊╃╇╉╋";
    private static final String ALPHABET_SINGLE_DOUBLE = "╔╗╚╝═╤╦═╧╩║╟╠║╢╣─═│║┼++++╪++++╫++++╬";

    private static final int CH_CORNER_TL = 0;
    private static final int CH_CORNER_TR = 1;
    private static final int CH_CORNER_BL = 2;
    private static final int CH_CORNER_BR = 3;

    private static final int CH_TOP_EDGE = 4;
    private static final int CH_TOP_EDGE_V_LIGHT = 5;
    private static final int CH_TOP_EDGE_V_HEAVY = 6;
    private static final int CH_BOTTOM_EDGE = 7;
    private static final int CH_BOTTOM_EDGE_V_LIGHT = 8;
    private static final int CH_BOTTOM_EDGE_V_HEAVY = 9;
    private static final int CH_LEFT_EDGE = 10;
    private static final int CH_LEFT_EDGE_H_LIGHT = 11;
    private static final int CH_LEFT_EDGE_H_HEAVY = 12;
    private static final int CH_RIGHT_EDGE = 13;
    private static final int CH_RIGHT_EDGE_H_LIGHT = 14;
    private static final int CH_RIGHT_EDGE_H_HEAVY = 15;

    private static final int CH_LINE_H_LIGHT = 16;
    private static final int CH_LINE_H_HEAVY = 17;
    private static final int CH_LINE_V_LIGHT = 18;
    private static final int CH_LINE_V_HEAVY = 19;

    private static final int CH_CROSSES_FIRST_OFFSET = 20;
    private static final int CH_CROSS_tlbr = 20;
    private static final int CH_CROSS_tlbR = 21;
    private static final int CH_CROSS_tlBr = 22;
    private static final int CH_CROSS_tlBR = 23;
    private static final int CH_CROSS_tLbr = 24;
    private static final int CH_CROSS_tLbR = 25;
    private static final int CH_CROSS_tLBr = 26;
    private static final int CH_CROSS_tLBR = 27;
    private static final int CH_CROSS_Tlbr = 28;
    private static final int CH_CROSS_TlbR = 29;
    private static final int CH_CROSS_TlBr = 30;
    private static final int CH_CROSS_TlBR = 31;
    private static final int CH_CROSS_TLbr = 32;
    private static final int CH_CROSS_TLbR = 33;
    private static final int CH_CROSS_TLBr = 34;
    private static final int CH_CROSS_TLBR = 35;

    private static final String H_LINE_LIGHT = "─";
    private static final String H_LINE_HEAVY = "━";

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

        for (int y = 0; y < sideLen; ++y) {
            // border above cell
            for (int x = 0; x < sideLen; ++x) {
                if (x == 0 && y == 0) {
                    builder.append(getChar(CH_CORNER_TL));
                } else if (x == 0) {
                    builder.append(getChar(hBorders[x][y] ? CH_LEFT_EDGE_H_HEAVY : CH_LEFT_EDGE_H_LIGHT));
                } else if (y == 0) {
                    builder.append(getChar(vBorders[x][y] ? CH_TOP_EDGE_V_HEAVY : CH_TOP_EDGE_V_LIGHT));
                } else {
                    // Select the appropriate "cross" glyph.
                    final char cross = getCross(
                            vBorders[x][y - 1], hBorders[x - 1][y], vBorders[x][y], hBorders[x][y]
                    );
                    builder.append(cross);
                }
                if (y == 0) {
                    builder.append(generateHorizontalLine(CH_TOP_EDGE));
                } else {
                    builder.append(generateHorizontalLine(hBorders[x][y] ? CH_LINE_H_HEAVY : CH_LINE_H_LIGHT));
                }
            }
            if (y == 0) {
                builder.append(getChar(CH_CORNER_TR));
            } else {
                builder.append(getChar(hBorders[sideLen - 1][y] ? CH_RIGHT_EDGE_H_HEAVY : CH_RIGHT_EDGE_H_LIGHT));
            }
            builder.append("\n");

            // cell contents
            for (int x = 0; x < sideLen; ++x) {
                if (x == 0) {
                    builder.append(getChar(CH_LEFT_EDGE));
                } else {
                    builder.append(getChar(vBorders[x][y] ? CH_LINE_V_HEAVY : CH_LINE_V_LIGHT));
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
                    builder.append(getChar(CH_RIGHT_EDGE));
                }
            }
            builder.append("\n");

            // bottom edge of entire grid
            if (y == sideLen - 1) {
                for (int x = 0; x < sideLen; ++x) {
                    if (x == 0) {
                        builder.append(getChar(CH_CORNER_BL));
                    } else {
                        builder.append(getChar(vBorders[x][y] ? CH_BOTTOM_EDGE_V_HEAVY : CH_BOTTOM_EDGE_V_LIGHT));
                    }
                    builder.append(generateHorizontalLine(CH_BOTTOM_EDGE));
                }
                builder.append(getChar(CH_CORNER_BR)).append("\n");
            }
        }

        writer.println(builder.toAnsi(mTerminal));
    }

    private char getCross(boolean topHeavy, boolean leftHeavy, boolean bottomHeavy,
                          boolean rightHeavy) {
        int crossType = 0;
        crossType |= rightHeavy ? 1 : 0;
        crossType |= bottomHeavy ? 2 : 0;
        crossType |= leftHeavy ? 4 : 0;
        crossType |= topHeavy ? 8 : 0;
        return getChar(CH_CROSSES_FIRST_OFFSET + crossType);
    }

    private char getChar(int offset) {
        return ALPHABET_LIGHT_HEAVY.charAt(offset);
    }

    private String generateHorizontalLine(int offset) {
        final char c = getChar(offset);
        return new String(new char[]{c, c, c});
    }
}
