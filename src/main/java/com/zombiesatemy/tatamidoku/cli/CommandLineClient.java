package com.zombiesatemy.tatamidoku.cli;

import com.zombiesatemy.tatamidoku.game.GameState;
import com.zombiesatemy.tatamidoku.game.LayoutImpl;
import com.zombiesatemy.tatamidoku.game.PlacementImpl;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintWriter;

public final class CommandLineClient {
    private static final int NO_COMMAND = 0;
    private static final int EXIT = 1;
    private static final int SUCCESS = 2;
    private final Terminal mTerminal;
    private final LineReader mLineReader;
    private final PrintWriter mWriter;
    private TextRenderer mRenderer;
    private GameState mGameState;

    public static void main(String[] args) {
        try {
            new CommandLineClient(args).startREPL();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private CommandLineClient(String[] args) throws IOException {
        mTerminal = TerminalBuilder.builder()
                .system(true)
                .build();
        mLineReader = LineReaderBuilder.builder()
                .terminal(mTerminal)
                .build();
        mWriter = mTerminal.writer();
    }

    private void startREPL() throws Exception {
        mRenderer = new TextRenderer(mTerminal);
        mGameState = new GameState();
        mGameState.setLayout(new LayoutImpl(6, 3));
        mGameState.setPlacement(new PlacementImpl());
        try {
            runREPL();
        } finally {
        }
        mWriter.println("Farewell!");
        mWriter.flush();
    }

    private void runREPL() {
        while (true) {
            switch (readAndInterpretLine()) {
                case NO_COMMAND:
                    break;
                case SUCCESS:
                    break;
                case EXIT:
                    return;
            }
        }
    }

    private int readAndInterpretLine() {
        final String prompt = "tatamidoku> ";
        String line;
        try {
            line = mLineReader.readLine(prompt);
        } catch (UserInterruptException e) {
            // Ignore
            return NO_COMMAND;
        } catch (EndOfFileException e) {
            return EXIT;
        }
        if (!interpretCommand(line.trim())) {
            return EXIT;
        }
        return SUCCESS;
    }

    private boolean interpretCommand(String input) {
        try {
            if (input.equals("exit")) {
                return false;
            } else if (input.equals("print")) {
                mRenderer.render(mGameState);
            } else {
              mWriter.format("ERROR: Unknown command \"%s\"\n", input);
            }
            mWriter.flush();
        } catch (Exception e) {
            mWriter.println("UNCAUGHT ERROR");
            e.printStackTrace(mWriter);
            mWriter.flush();
        }
        return true;
    }

}
