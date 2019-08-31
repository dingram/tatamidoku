package com.zombiesatemy.tatamidoku.cli;

import com.zombiesatemy.tatamidoku.game.GameState;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.PrintWriter;

public class TextRenderer {
    private final Terminal mTerminal;

    public TextRenderer(Terminal terminal) {
        mTerminal = terminal;
    }

    public void render(GameState gameState) {
        final PrintWriter writer = mTerminal.writer();
        writer.println(
                new AttributedStringBuilder()
                        .style(AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.RED))
                        .append("Rendering not supported yet.")
                        .style(AttributedStyle.DEFAULT)
                        .toAnsi(mTerminal)
        );
    }
}
