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
import java.util.NoSuchElementException;
import java.util.Scanner;

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
        final LayoutImpl layout = new LayoutImpl(3, 2);
        mGameState.setLayout(layout);
        mGameState.setPlacement(PlacementImpl.fromSideLength(layout.getSideLength()));
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
            final Scanner scanner = new Scanner(input);
            final String command = scanner.next();
            switch (command) {
                case "exit":
                    return false;
                case "print":
                    mRenderer.render(mGameState);
                    break;
                case "set": {
                    int column = scanner.nextInt();
                    int row = scanner.nextInt();
                    int value = scanner.nextInt();
                    mGameState.setPlacement(mGameState.getPlacement().withValueAt(column, row, value));
                    break;
                }
                case "clear": {
                    if (scanner.hasNext()) {
                        int column = scanner.nextInt();
                        int row = scanner.nextInt();
                        mGameState.setPlacement(mGameState.getPlacement().withNoValueAt(column, row));
                    } else {
                        // Reset the whole thing.
                        mGameState.setPlacement(PlacementImpl.fromSideLength(mGameState.getLayout().getSideLength()));
                    }
                    break;
                }
                default:
                    mWriter.format("ERROR: Unknown command \"%s\"\n", command);
                    break;
            }
            mWriter.flush();
        } catch (NoSuchElementException e) {
            mWriter.format("ERROR: Malformed command \"%s\"\n", input);
        } catch (Exception e) {
            mWriter.println("UNCAUGHT ERROR");
            e.printStackTrace(mWriter);
            mWriter.flush();
        }
        return true;
    }

}
